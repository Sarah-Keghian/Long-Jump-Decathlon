package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.repositories.PartieRepo;
import fr.stageLIS.long_jump_serveur.services.PartieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartieServiceTests {

    @Mock
    private PartieRepo partieRepo;

    @InjectMocks
    private PartieService partieService;

    @Test
    public void createPartie_Test(){

        Long idJoueur = 1L;
        Long id = 2L;
        Partie partie = Partie.builder().idJoueur(idJoueur).id(id).build();

        when(partieRepo.save(partie)).thenReturn(partie);

        Partie partieObtenue = partieService.createPartie(idJoueur);

        Assertions.assertNotNull(partieObtenue);
        Assertions.assertEquals(idJoueur, partieObtenue.getIdJoueur());
        Assertions.assertNull(partieObtenue.getScoreFinal());
        Assertions.assertEquals(partie, partieObtenue);
    }

    @Test
    pubi
}
