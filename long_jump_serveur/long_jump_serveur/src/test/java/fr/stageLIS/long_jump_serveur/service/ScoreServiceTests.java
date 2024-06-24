package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.models.Score;
import fr.stageLIS.long_jump_serveur.repositories.ScoreRepo;
import fr.stageLIS.long_jump_serveur.services.JoueurService;
import fr.stageLIS.long_jump_serveur.services.ScoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ScoreServiceTests {

    @Mock
    private ScoreRepo scoreRepo;
    @Mock
    private JoueurService joueurService;

    @InjectMocks
    private ScoreService scoreService;


    @Test
    public void createScore_Test(){
        Long idJoueur = 1L;

        Score scoreAttendu = new Score();
        scoreAttendu.setIdJoueur(idJoueur);
        scoreAttendu.setScoreFinal(0);

        when(scoreRepo.save(scoreAttendu)).thenReturn(scoreAttendu);

        Score scoreObtenu = scoreService.createScore(idJoueur);

        Assertions.assertNotNull(scoreObtenu);
        Assertions.assertEquals(scoreAttendu.getScoreFinal(), scoreObtenu.getScoreFinal());
        Assertions.assertEquals(scoreAttendu.getIdJoueur(), scoreObtenu.getIdJoueur());

    }

    @Test
    public void getScoreById_Test(){
        Long idJoueur = 3L;
        Long id = 1L;
        Long idFaux = 2L;

        Score scoreAttendu = Score.builder()
                .id(id)
                .idJoueur(idJoueur)
                .scoreFinal(32).build();

        when(scoreRepo.findById(id)).thenReturn(Optional.of(scoreAttendu));
        when(scoreRepo.findById(idFaux)).thenReturn(Optional.empty());

        Score scoreObtenu = scoreService.getScoreById(id);

        Assertions.assertNotNull(scoreObtenu);
        Assertions.assertEquals(scoreAttendu, scoreObtenu);

        Assertions.assertThrows(IllegalArgumentException.class, () -> scoreService.getScoreById(idFaux));
    }

    @Test
    public void updateScoreById_Test(){

        Long oldId = 1L;
        Long newId = 5L;
        int oldScoreFinal = 32;
        int newScoreFinal = 23;
        Long oldIdJoueur = 3L;
        Long newIdJoueur = 4L;
        Long idFaux = 2L;

        Score oldScore = Score.builder()
                .scoreFinal(oldScoreFinal)
                .idJoueur(oldIdJoueur)
                .id(oldId).build();
        Score newScore = Score.builder()
                .scoreFinal(newScoreFinal)
                .idJoueur(newIdJoueur)
                .id(newId).build();
        Score newOldScore = Score.builder()
                .scoreFinal(newScoreFinal)
                .idJoueur(newIdJoueur)
                .id(oldId).build();


        when(scoreRepo.findById(oldId)).thenReturn(Optional.of(oldScore));
        when(scoreRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(scoreRepo.save(newOldScore)).thenReturn(newOldScore);

        Score scoreObtenu = scoreService.updateScoreById(oldId, newScore);

        Assertions.assertNotNull(scoreObtenu);
        Assertions.assertEquals(newScoreFinal, scoreObtenu.getScoreFinal());
        Assertions.assertEquals(newIdJoueur, scoreObtenu.getIdJoueur());
        Assertions.assertEquals(oldId, scoreObtenu.getId());

        Assertions.assertThrows(IllegalArgumentException.class, () -> scoreService.updateScoreById(idFaux, newScore));
    }

    @Test
    public void deleteScoreById_Test(){
        Long id = 1L;
        Long idFaux = 2L;

        doNothing().when(scoreRepo).deleteById(id);
        when(scoreRepo.existsById(id)).thenReturn(true);
        when(scoreRepo.existsById(idFaux)).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> scoreService.deleteScoreById(id));
        Assertions.assertThrows(IllegalArgumentException.class, () -> scoreService.deleteScoreById(idFaux));
    }
}
