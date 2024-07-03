package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.LeaderDto;
import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.repositories.PartieRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartieServiceTests {

    @Mock
    private PartieRepo partieRepo;
    @Mock
    private EssaisService essaisService;
    @Mock
    private JoueurService joueurService;

    @InjectMocks
    private PartieService partieService;

    Long id1 = 1L;
    Long id2 = 2L;
    int score1 = 0;
    int score2 = 3;
    Long idJoueur = 5L;
    Long idFaux = 20L;
    Partie partie1 = Partie.builder().idJoueur(idJoueur).scoreFinal(score1).build();
    Partie partie1Saved = Partie.builder().id(id1).idJoueur(idJoueur).scoreFinal(score1).build();
    Partie partie2 = Partie.builder().idJoueur(idJoueur).scoreFinal(score2).build();
    Partie partie2Saved = Partie.builder().id(id2).idJoueur(idJoueur).scoreFinal(score2).build();


    @Test
    public void createPartie_Test(){

        when(partieRepo.save(partie1)).thenReturn(partie1Saved);

        Partie partieObtenue = partieService.createPartie(idJoueur);

        Assertions.assertNotNull(partieObtenue);
        Assertions.assertEquals(score1, partieObtenue.getScoreFinal());
        Assertions.assertEquals(idJoueur, partieObtenue.getIdJoueur());
        Assertions.assertEquals(partie1Saved, partieObtenue);
    }


    @Test
    public void getPartie_Test(){

        when(partieRepo.findById(id1)).thenReturn(Optional.of(partie1Saved));
        when(partieRepo.findById(idFaux)).thenReturn(Optional.empty());

        Optional<Partie> partieObtenue = partieService.getPartie(id1);
        Optional<Partie> partieObtenueFaux = partieService.getPartie(idFaux);

        Assertions.assertTrue(partieObtenue.isPresent());
        Assertions.assertEquals(partie1Saved, partieObtenue.get());

        Assertions.assertEquals(Optional.empty(), partieObtenueFaux);
    }


    @Test
    public void addScoreFinalPartie_Test(){

        when(partieService.getPartie(id2)).thenReturn(Optional.of(partie2));
        when(partieService.getPartie(idFaux)).thenReturn(Optional.empty());
        when(partieRepo.save(partie2)).thenReturn(partie2Saved);

        Optional<Partie> partieObtenue = partieService.addScoreFinalPartie(id2, score2);
        Optional<Partie> partieObtenueFaux = partieService.getPartie(idFaux);

        Assertions.assertTrue(partieObtenue.isPresent());
        Assertions.assertEquals(score2, partieObtenue.get().getScoreFinal());
        Assertions.assertEquals(idJoueur, partieObtenue.get().getIdJoueur());
        Assertions.assertEquals(id2, partieObtenue.get().getId());

        Assertions.assertEquals(Optional.empty(), partieObtenueFaux);
    }


    @Test
    public void convertPartieToDto_Test(){

        PartieDto partieDtoAttendue = PartieDto.builder().id(id1).idJoueur(idJoueur).scoreFinal(score1).place(2).build();

        List<Partie> listePartiesTri = Arrays.asList(partie1Saved, partie2Saved);
        when(partieService.classerParties()).thenReturn(listePartiesTri);

        PartieDto partieDtoObtenue = partieService.convertPartieToDto(partie1Saved);

        Assertions.assertEquals(partie1Saved.getId(), partieDtoObtenue.getId());
        Assertions.assertEquals(partie1Saved.getIdJoueur(), partieDtoObtenue.getIdJoueur());
        Assertions.assertEquals(partie1Saved.getScoreFinal(), partieDtoObtenue.getScoreFinal());
        Assertions.assertEquals(partieDtoAttendue.getPlace(), partieDtoObtenue.getPlace());
    }


//    @Test
//    public void convertToLeaderDto_Test() {
//
//        Joueur joueur1 = Joueur.builder().nom("Joueur1").id(1L).build();
//        Joueur joueur2 = Joueur.builder().nom("Joueur2").id(2L).build();
//        Partie partie = Partie.builder().scoreFinal(23).idJoueur(1L).id(1L).build();
//        PartieDto partieDto = PartieDto.builder().scoreFinal(23).idJoueur(1L).id(1L).place(3).build();
//        List<Joueur> listeJoueurs = Arrays.asList(joueur1, joueur2);
//
//
//        when(joueurService.getAllJoueurs()).thenReturn(listeJoueurs);
//        when(partieService.convertPartieToDto(partie)).thenReturn(partieDto);
//
//        LeaderDto leaderDtoObtenu = partieService.convertToLeaderDto(partie);
//
//        Assertions.assertNotNull(leaderDtoObtenu);
//        Assertions.assertEquals(LeaderDto.class, leaderDtoObtenu.getClass());
//        Assertions.assertEquals(partieDto.getPlace(), leaderDtoObtenu.getPlace());
//        Assertions.assertEquals(joueur1.getNom(), leaderDtoObtenu.getNomJoueur());
//        Assertions.assertEquals(partie.getScoreFinal(), leaderDtoObtenu.getScore());
//    }


//    @Test
//    public void convertToLeaderDtoList_Test(){
//
//        Partie partie1 = Partie.builder().id(1L).idJoueur(2L).scoreFinal(3).build();
//        Partie partie2 = Partie.builder().id(2L).idJoueur(3L).scoreFinal(4).build();
//        Partie partie3 = Partie.builder().id(3L).idJoueur(4L).scoreFinal(0).build();
//        Partie partie4 = Partie.builder().id(4L).idJoueur(5L).scoreFinal(2).build();
//        LeaderDto leaderDto1 = LeaderDto.builder().score(3).nomJoueur("A").place(2).build();
//        LeaderDto leaderDto2 = LeaderDto.builder().score(4).nomJoueur("B").place(1).build();
//        LeaderDto leaderDto3 = LeaderDto.builder().score(0).nomJoueur("C").place(4).build();
//        LeaderDto leaderDto4 = LeaderDto.builder().score(2).nomJoueur("D").place(3).build();
//
//
//        List<Partie> listePartiesTri = Arrays.asList(partie2, partie1, partie4, partie3);
//
//        List<LeaderDto> leaderDtoList = Arrays.asList(leaderDto2, leaderDto1, leaderDto4, leaderDto3);
//
//        when(partieService.convertToLeaderDto(partie1)).thenReturn(leaderDto1);
//        when(partieService.convertToLeaderDto(partie2)).thenReturn(leaderDto2);
//        when(partieService.convertToLeaderDto(partie3)).thenReturn(leaderDto3);
//        when(partieService.convertToLeaderDto(partie4)).thenReturn(leaderDto4);
//
//        List<LeaderDto> listeObtenue = partieService.convertToLeaderDtoList(listePartiesTri);
//
//        Assertions.assertNotNull(listeObtenue);
//        Assertions.assertEquals(leaderDtoList, listeObtenue);


//    }


    @Test
    public void classerParties_Test(){

        Partie partie1 = Partie.builder().id(1L).idJoueur(2L).scoreFinal(3).build();
        Partie partie2 = Partie.builder().id(2L).idJoueur(3L).scoreFinal(4).build();
        Partie partie3 = Partie.builder().id(3L).idJoueur(4L).scoreFinal(0).build();
        Partie partie4 = Partie.builder().id(4L).idJoueur(5L).scoreFinal(2).build();

        List<Partie> listePartiesNonTri = Arrays.asList(partie1, partie2, partie3, partie4);
        List<Partie> listePartiesTri = Arrays.asList(partie2, partie1, partie4, partie3);

        when(partieRepo.findAll()).thenReturn(listePartiesNonTri);

        List<Partie> partiesListeObtenue = partieService.classerParties();

        Assertions.assertNotNull(partiesListeObtenue);
        Assertions.assertEquals(4, partiesListeObtenue.size());
        Assertions.assertEquals(listePartiesTri, partiesListeObtenue);
    }

//    @Test
//    public void getLeadersParties_Test(){
//
//        Partie partie16 = Partie.builder().id(1L).idJoueur(2L).scoreFinal(1).build();
//        Partie partie15 = Partie.builder().id(2L).idJoueur(2L).scoreFinal(3).build();
//        Partie partie14 = Partie.builder().id(3L).idJoueur(3L).scoreFinal(4).build();
//        Partie partie13 = Partie.builder().id(4L).idJoueur(3L).scoreFinal(5).build();
//        Partie partie12 = Partie.builder().id(5L).idJoueur(4L).scoreFinal(6).build();
//        Partie partie11 = Partie.builder().id(6L).idJoueur(4L).scoreFinal(10).build();
//        Partie partie10 = Partie.builder().id(7L).idJoueur(4L).scoreFinal(11).build();
//        Partie partie9 = Partie.builder().id(8L).idJoueur(2L).scoreFinal(12).build();
//        Partie partie8 = Partie.builder().id(9L).idJoueur(8L).scoreFinal(13).build();
//        Partie partie7 = Partie.builder().id(10L).idJoueur(8L).scoreFinal(14).build();
//        Partie partie6 = Partie.builder().id(11L).idJoueur(3L).scoreFinal(15).build();
//        Partie partie5 = Partie.builder().id(12L).idJoueur(6L).scoreFinal(16).build();
//        Partie partie4 = Partie.builder().id(13L).idJoueur(6L).scoreFinal(17).build();
//        Partie partie3 = Partie.builder().id(14L).idJoueur(4L).scoreFinal(18).build();
//        Partie partie2 = Partie.builder().id(15L).idJoueur(9L).scoreFinal(19).build();
//        Partie partie1 = Partie.builder().id(16L).idJoueur(5L).scoreFinal(20).build();
//
//        LeaderDto leaderDto15 = LeaderDto.builder().nomJoueur("A").place(15).score(3).build();
//        LeaderDto leaderDto14 = LeaderDto.builder().nomJoueur("B").place(14).score(4).build();
//        LeaderDto leaderDto13 = LeaderDto.builder().nomJoueur("C").place(13).score(5).build();
//        LeaderDto leaderDto12 = LeaderDto.builder().nomJoueur("D").place(12).score(6).build();
//        LeaderDto leaderDto11 = LeaderDto.builder().nomJoueur("E").place(11).score(10).build();
//        LeaderDto leaderDto10 = LeaderDto.builder().nomJoueur("F").place(10).score(11).build();
//        LeaderDto leaderDto9 = LeaderDto.builder().nomJoueur("G").place(9).score(12).build();
//        LeaderDto leaderDto8 = LeaderDto.builder().nomJoueur("H").place(8).score(13).build();
//        LeaderDto leaderDto7 = LeaderDto.builder().nomJoueur("I").place(7).score(14).build();
//        LeaderDto leaderDto6 = LeaderDto.builder().nomJoueur("J").place(6).score(15).build();
//        LeaderDto leaderDto5 = LeaderDto.builder().nomJoueur("K").place(5).score(16).build();
//        LeaderDto leaderDto4 = LeaderDto.builder().nomJoueur("L").place(4).score(17).build();
//        LeaderDto leaderDto3 = LeaderDto.builder().nomJoueur("M").place(3).score(18).build();
//        LeaderDto leaderDto2 = LeaderDto.builder().nomJoueur("N").place(2).score(19).build();
//        LeaderDto leaderDto1 = LeaderDto.builder().nomJoueur("O").place(1).score(20).build();
//
//        List<Partie> listeAllParties = Arrays.asList(partie1, partie2, partie3, partie4, partie5, partie6, partie7, partie11, partie9, partie13, partie16, partie10, partie14, partie12, partie15, partie8);
//        List<Partie> listePartiesTri = Arrays.asList(partie1, partie2, partie3, partie4, partie5, partie6, partie7, partie8, partie9, partie10, partie11, partie12, partie13, partie14, partie15, partie16);
//        List<Partie> listePartiesLeaders = Arrays.asList(partie1, partie2, partie3, partie4, partie5, partie6, partie7, partie8, partie9, partie10, partie11, partie12, partie13, partie14, partie15);
//        List<LeaderDto> listeLeaderDtos = Arrays.asList(leaderDto1, leaderDto2, leaderDto3, leaderDto4, leaderDto5, leaderDto6, leaderDto7, leaderDto8, leaderDto9, leaderDto10, leaderDto11, leaderDto12, leaderDto13, leaderDto14, leaderDto15);
//
//        when(partieRepo.findAll()).thenReturn(listeAllParties);
//        when(partieService.classerParties()).thenReturn(listePartiesTri);
//        when(partieService.convertToLeaderDtoList(listePartiesLeaders)).thenReturn(listeLeaderDtos);
//
//        List<LeaderDto> leadersListeObtenue = partieService.getLeadersParties();
//
//        Assertions.assertEquals(listeLeaderDtos, leadersListeObtenue);
//    }


//    @Test
//    public void getLeadersParties_Test(){
//
//        Partie partie1 = Partie.builder().id(1L).idJoueur(2L).scoreFinal(1).build();
//        Partie partie2 = Partie.builder().id(2L).idJoueur(2L).scoreFinal(4).build();
//        Partie partie3 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(13).build();
//        Partie partie4 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(12).build();
//        Partie partie5 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(25).build();
//        Partie partie6 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(30).build();
//        Partie partie7 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(23).build();
//        Partie partie8 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(23).build();
//        Partie partie9 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(21).build();
//        Partie partie10 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(20).build();
//        Partie partie11 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(19).build();
//        Partie partie12 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(18).build();
//        Partie partie13 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(17).build();
//        Partie partie14 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(16).build();
//        Partie partie15 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(15).build();
//        Partie partie16 = Partie.builder().id(3L).idJoueur(5L).scoreFinal(14).build();
//
//        Lis1t<Partie>
//
//    }

}
