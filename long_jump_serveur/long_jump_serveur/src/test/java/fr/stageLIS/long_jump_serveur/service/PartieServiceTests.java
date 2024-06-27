package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.repositories.PartieRepo;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartieServiceTests {

    @Mock
    private PartieRepo partieRepo;
    @Mock
    private EssaisService essaisService;

    @InjectMocks
    private PartieService partieService;

    @Test
    public void createPartie_Test(){

        Long idJoueur = 1L;
        Partie partie = Partie.builder().idJoueur(idJoueur).build();


        when(partieRepo.save(partie)).thenReturn(partie);

        Partie partieObtenue = partieService.createPartie(idJoueur);

        Assertions.assertNotNull(partieObtenue);
        Assertions.assertEquals(idJoueur, partieObtenue.getIdJoueur());
        Assertions.assertNull(partieObtenue.getScoreFinal());
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

        Partie partie = Partie.builder().id(1L).idJoueur(2L).scoreFinal(3).build();

        PartieDto partieDtoObtenue = partieService.convertPartieToDto(partie);

        Assertions.assertEquals(partie.getId(), partieDtoObtenue.getId());
        Assertions.assertEquals(partie.getIdJoueur(), partieDtoObtenue.getIdJoueur());
        Assertions.assertEquals(partie.getScoreFinal(), partieDtoObtenue.getScoreFinal());
    }

    @Test
    public void convertDtoToPartie_Test(){

        Long id1 = 1L;
        Long id2 = 2L;
        Long idFaux = 3L;
        PartieDto partieDto1 = PartieDto.builder().id(id1).idJoueur(2L).scoreFinal(3).build();
        PartieDto partieDtoFaux = PartieDto.builder().id(idFaux).idJoueur(2L).scoreFinal(3).build();
        Partie partie1 = Partie.builder().id(id1).idJoueur(2L).scoreFinal(3).build();
        Partie partie2 = Partie.builder().id(id2).scoreFinal(3).build();
        List<Partie> listeParties = Arrays.asList(partie1, partie2);


        when(partieRepo.findAll()).thenReturn(listeParties);

        Optional<Partie> partieObtenue1 = partieService.convertDtoToPartie(partieDto1);
        Optional<Partie> partieObtenueFaux = partieService.convertDtoToPartie(partieDtoFaux);

        Assertions.assertNotNull(partieObtenue1);
        Assertions.assertTrue(partieObtenue1.isPresent());
        Assertions.assertEquals(partie1, partieObtenue1.get());

        Assertions.assertNotNull(partieObtenueFaux);
        Assertions.assertEquals(Optional.empty(), partieObtenueFaux);
    }

}
