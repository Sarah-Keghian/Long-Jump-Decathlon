package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.DTO.LeaderDto;
import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.repositories.JoueurRepo;
import fr.stageLIS.long_jump_serveur.repositories.PartieRepo;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import fr.stageLIS.long_jump_serveur.services.JoueurService;
import fr.stageLIS.long_jump_serveur.services.PartieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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


    @Test
    public void createPartie_Test(){

        Long idJoueur = 1L;
        Partie partie = Partie.builder().idJoueur(idJoueur).scoreFinal(0).build();


        when(partieRepo.save(partie)).thenReturn(partie);

        Partie partieObtenue = partieService.createPartie(idJoueur);

        Assertions.assertNotNull(partieObtenue);
        Assertions.assertEquals(0, partieObtenue.getScoreFinal());
        Assertions.assertEquals(idJoueur, partieObtenue.getIdJoueur());
        Assertions.assertEquals(partie, partieObtenue);
    }


    @Test
    public void getPartie_Test(){

        Long idJoueur = 1L;
        Long id = 2L;
        Long idFaux = 3L;
        Partie partie = Partie.builder().id(id).idJoueur(idJoueur).build();

        when(partieRepo.findById(id)).thenReturn(Optional.of(partie));
        when(partieRepo.findById(idFaux)).thenReturn(Optional.empty());

        Optional<Partie> partieObtenue = partieService.getPartie(id);
        Optional<Partie> partieObtenueFaux = partieService.getPartie(idFaux);

        Assertions.assertTrue(partieObtenue.isPresent());
        Assertions.assertEquals(partie, partieObtenue.get());

        Assertions.assertEquals(Optional.empty(), partieObtenueFaux);
    }


    @Test
    public void addScoreFinalPartie2_Test(){}


    @Test
    public void addScoreFinalPartie_Test(){

        Long idPartie1 = 1L;
        Long idPartie2 = 2L;
        Long idFaux = 3L;
        Long idEssais1 = 3L;
        Long idEssais2 = 4L;
        int score1 = 1;
        int score2 = 2;
        int score3 = 3;
        int score4 = 4;
        int scoreFinal1 = 3;
        int scoreFinal2 = 4;

        Essais essais1 = Essais.builder().id(idEssais1).idPartie(idPartie1)
                .score1(score1).score2(score2).score3(score3).build();
        Essais essais2 = Essais.builder().id(idEssais2).idPartie(idPartie2)
                .score1(score1).score2(score4).score3(score2).build();

        List<Essais> listeEssais = Arrays.asList(essais1, essais2);


        Partie partie1 = Partie.builder().id(idPartie1).scoreFinal(scoreFinal1).build();
        Partie partie2 = Partie.builder().id(idPartie2).scoreFinal(scoreFinal2).build();

        when(partieService.getPartie(idPartie1)).thenReturn(Optional.of(partie1));
        when(partieService.getPartie(idPartie2)).thenReturn(Optional.of(partie2));
        when(essaisService.getAllEssais()).thenReturn(listeEssais);
        when(partieRepo.save(partie1)).thenReturn(partie1);
        when(partieRepo.save(partie2)).thenReturn(partie2);

        Optional<Partie> partieObtenue1 = partieService.addScoreFinalPartie(idPartie1);
        Optional<Partie> partieObtenue2 = partieService.addScoreFinalPartie(idPartie2);
        Optional<Partie> partieOptional = partieService.addScoreFinalPartie(idFaux);

        Assertions.assertTrue(partieObtenue1.isPresent());
        Assertions.assertEquals(scoreFinal1, partieObtenue1.get().getScoreFinal());
        Assertions.assertEquals(Optional.empty(), partieOptional);

        Assertions.assertTrue(partieObtenue2.isPresent());
        Assertions.assertEquals(scoreFinal2, partieObtenue2.get().getScoreFinal());
    }


    @Test
    public void deletePartie_Test(){
        Long idPartie = 1L;
        Long idFaux = 2L;
        Partie partie = Partie.builder().id(idPartie).build();

        when(partieService.getPartie(idPartie)).thenReturn(Optional.of(partie));
        when(partieService.getPartie(idFaux)).thenReturn(Optional.empty());
        doNothing().when(partieRepo).deleteById(idPartie);

        Optional<Partie> partieSupprim = partieService.deletePartie(idPartie);
        Optional<Partie> partieSupprimFaux = partieService.deletePartie(idFaux);

        Assertions.assertNotNull(partieSupprim);
        Assertions.assertEquals(Optional.of(partie), partieSupprim);
        Assertions.assertNotNull(partieSupprimFaux);
        Assertions.assertEquals(Optional.empty(), partieSupprimFaux);
    }


    @Test
    public void convertPartieToDto_Test(){

        Partie partie1 = Partie.builder().id(1L).idJoueur(2L).scoreFinal(4).build();
        Partie partie2 = Partie.builder().id(2L).idJoueur(3L).scoreFinal(3).build();
        PartieDto partieDtoAttendue = PartieDto.builder().id(1L).idJoueur(2L).scoreFinal(3).place(1).build();

        List<Partie> listePartiesTri = Arrays.asList(partie1, partie2);
        when(partieService.classerParties()).thenReturn(listePartiesTri);

        PartieDto partieDtoObtenue = partieService.convertPartieToDto(partie1);

        Assertions.assertEquals(partie1.getId(), partieDtoObtenue.getId());
        Assertions.assertEquals(partie1.getIdJoueur(), partieDtoObtenue.getIdJoueur());
        Assertions.assertEquals(partie1.getScoreFinal(), partieDtoObtenue.getScoreFinal());
        Assertions.assertEquals(partieDtoAttendue.getPlace(), partieDtoObtenue.getPlace());
    }


    @Test
    public void convertToLeaderDto_Test() {

        Joueur joueur1 = Joueur.builder().nom("Joueur1").id(1L).build();
        Joueur joueur2 = Joueur.builder().nom("Joueur2").id(2L).build();
        Partie partie = Partie.builder().scoreFinal(23).idJoueur(1L).id(1L).build();
        PartieDto partieDto = PartieDto.builder().scoreFinal(23).idJoueur(1L).id(1L).place(3).build();
        List<Joueur> listeJoueurs = Arrays.asList(joueur1, joueur2);


        when(joueurService.getAllJoueurs()).thenReturn(listeJoueurs);
        when(partieService.convertPartieToDto(partie)).thenReturn(partieDto);

        LeaderDto leaderDtoObtenu = partieService.convertToLeaderDto(partie);

        Assertions.assertNotNull(leaderDtoObtenu);
        Assertions.assertEquals(LeaderDto.class, leaderDtoObtenu.getClass());
        Assertions.assertEquals(partieDto.getPlace(), leaderDtoObtenu.getPlace());
        Assertions.assertEquals(joueur1.getNom(), leaderDtoObtenu.getNomJoueur());
        Assertions.assertEquals(partie.getScoreFinal(), leaderDtoObtenu.getScore());
    }


    @Test
    public void convertToLeaderDtoList_Test(){}


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

    @Test
    public void getLeadersParties_Test(){

        Partie partie16 = Partie.builder().id(1L).idJoueur(2L).scoreFinal(1).build();
        Partie partie15 = Partie.builder().id(2L).idJoueur(2L).scoreFinal(3).build();
        Partie partie14 = Partie.builder().id(3L).idJoueur(3L).scoreFinal(4).build();
        Partie partie13 = Partie.builder().id(4L).idJoueur(3L).scoreFinal(5).build();
        Partie partie12 = Partie.builder().id(5L).idJoueur(4L).scoreFinal(6).build();
        Partie partie11 = Partie.builder().id(6L).idJoueur(4L).scoreFinal(10).build();
        Partie partie10 = Partie.builder().id(7L).idJoueur(4L).scoreFinal(11).build();
        Partie partie9 = Partie.builder().id(8L).idJoueur(2L).scoreFinal(12).build();
        Partie partie8 = Partie.builder().id(9L).idJoueur(8L).scoreFinal(13).build();
        Partie partie7 = Partie.builder().id(10L).idJoueur(8L).scoreFinal(14).build();
        Partie partie6 = Partie.builder().id(11L).idJoueur(3L).scoreFinal(15).build();
        Partie partie5 = Partie.builder().id(12L).idJoueur(6L).scoreFinal(16).build();
        Partie partie4 = Partie.builder().id(13L).idJoueur(6L).scoreFinal(17).build();
        Partie partie3 = Partie.builder().id(14L).idJoueur(4L).scoreFinal(18).build();
        Partie partie2 = Partie.builder().id(15L).idJoueur(9L).scoreFinal(19).build();
        Partie partie1 = Partie.builder().id(16L).idJoueur(5L).scoreFinal(20).build();

        LeaderDto leaderDto15 = LeaderDto.builder().nomJoueur("A").place(15).score(3).build();
        LeaderDto leaderDto14 = LeaderDto.builder().nomJoueur("B").place(14).score(4).build();
        LeaderDto leaderDto13 = LeaderDto.builder().nomJoueur("C").place(13).score(5).build();
        LeaderDto leaderDto12 = LeaderDto.builder().nomJoueur("D").place(12).score(6).build();
        LeaderDto leaderDto11 = LeaderDto.builder().nomJoueur("E").place(11).score(10).build();
        LeaderDto leaderDto10 = LeaderDto.builder().nomJoueur("F").place(10).score(11).build();
        LeaderDto leaderDto9 = LeaderDto.builder().nomJoueur("G").place(9).score(12).build();
        LeaderDto leaderDto8 = LeaderDto.builder().nomJoueur("H").place(8).score(13).build();
        LeaderDto leaderDto7 = LeaderDto.builder().nomJoueur("I").place(7).score(14).build();
        LeaderDto leaderDto6 = LeaderDto.builder().nomJoueur("J").place(6).score(15).build();
        LeaderDto leaderDto5 = LeaderDto.builder().nomJoueur("K").place(5).score(16).build();
        LeaderDto leaderDto4 = LeaderDto.builder().nomJoueur("L").place(4).score(17).build();
        LeaderDto leaderDto3 = LeaderDto.builder().nomJoueur("M").place(3).score(18).build();
        LeaderDto leaderDto2 = LeaderDto.builder().nomJoueur("N").place(2).score(19).build();
        LeaderDto leaderDto1 = LeaderDto.builder().nomJoueur("O").place(1).score(20).build();

        List<Partie> listeAllParties = Arrays.asList(partie1, partie2, partie3, partie4, partie5, partie6, partie7, partie11, partie9, partie13, partie16, partie10, partie14, partie12, partie15, partie8);
        List<Partie> listePartiesTri = Arrays.asList(partie1, partie2, partie3, partie4, partie5, partie6, partie7, partie8, partie9, partie10, partie11, partie12, partie13, partie14, partie15, partie16);
        List<Partie> listePartiesLeaders = Arrays.asList(partie1, partie2, partie3, partie4, partie5, partie6, partie7, partie8, partie9, partie10, partie11, partie12, partie13, partie14, partie15);
        List<LeaderDto> listeLeaderDtos = Arrays.asList(leaderDto1, leaderDto2, leaderDto3, leaderDto4, leaderDto5, leaderDto6, leaderDto7, leaderDto8, leaderDto9, leaderDto10, leaderDto11, leaderDto12, leaderDto13, leaderDto14, leaderDto15);

        when(partieRepo.findAll()).thenReturn(listeAllParties);
        when(partieService.classerParties()).thenReturn(listePartiesTri);
        when(partieService.convertToLeaderDtoList(listePartiesLeaders)).thenReturn(listeLeaderDtos);

        List<LeaderDto> leadersListeObtenue = partieService.getLeadersParties();

        Assertions.assertEquals(listeLeaderDtos, leadersListeObtenue);
    }


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
