package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.repositories.JoueurRepo;
import fr.stageLIS.long_jump_serveur.services.JoueurService;
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
public class JoueurServiceTest {

    @Mock
    JoueurRepo joueurRepo;

    @InjectMocks
    JoueurService joueurService;

    @Test
    public void createJoueur_Test(){

        String nom = "A";
        Joueur joueur = Joueur.builder().nom(nom).build();

        when(joueurRepo.save(joueur)).thenReturn(joueur);

        Joueur joueurObtenu = joueurService.createJoueur(nom);

        Assertions.assertNotNull(joueurObtenu);
        Assertions.assertEquals(joueur, joueurObtenu);
    }

    @Test
    public void getJoueurById_Test(){

        Long id = 1L;
        Long idFaux = 2L;
        Joueur joueur = Joueur.builder().id(id).build();

        when(joueurRepo.findById(id)).thenReturn(Optional.of(joueur));
        when(joueurRepo.findById(idFaux)).thenReturn(Optional.empty());

        Joueur joueurObtenu = joueurService.getJoueurById(id);
        Assertions.assertNotNull(joueurObtenu);
        Assertions.assertEquals(joueur, joueurObtenu);
        Assertions.assertThrows(IllegalArgumentException.class, () -> joueurService.getJoueurById(idFaux));
    }

    @Test
    public void getAllJoueurs_Test(){

        String nom1 = "A";
        String nom2 = "B";
        Long id1 = 1L;
        Long id2 = 2L;
        Joueur j1 = Joueur.builder().id(id1).nom(nom1).build();
        Joueur j2 = Joueur.builder().id(id2).nom(nom2).build();
        List<Joueur> listeJoueurs = Arrays.asList(j1, j2);

        when(joueurRepo.findAll()).thenReturn(listeJoueurs);

        List<Joueur> listeObteunue = joueurService.getAllJoueurs();
        Assertions.assertNotNull(listeObteunue);
        Assertions.assertEquals(listeObteunue, listeJoueurs);
    }

    @Test
    public void getJoueurByNom_Test(){

        String nom1 = "A";
        String nom2 = "B";
        Long id1 = 1L;
        Joueur j1 = Joueur.builder().id(id1).nom(nom1).build();
        List<Joueur> listeJoueurs = Arrays.asList(j1);
        when(joueurRepo.findAll()).thenReturn(listeJoueurs);

        Joueur joueurObtenu = joueurService.getJoueurByNom(nom1);
        Assertions.assertNotNull(joueurObtenu);
        Assertions.assertEquals(j1, joueurObtenu);
        Assertions.assertThrows(IllegalArgumentException.class, () -> joueurService.getJoueurByNom(nom2));
    }

    @Test
    public void deleteJoueurById_Test(){

        Long id = 1L;
        Long idFaux = 2L;

        when(joueurRepo.existsById(id)).thenReturn(true);
        when(joueurRepo.existsById(idFaux)).thenReturn(false);
        doNothing().when(joueurRepo).deleteById(id);

        Assertions.assertDoesNotThrow(()->joueurService.deleteJoueurById(id));
        Assertions.assertThrows(IllegalArgumentException.class, () -> joueurService.deleteJoueurById(idFaux));
    }

    @Test
    public void deleteJoueurByNom_Test(){

        String nom1 = "A";
        String nom2 = "B";
        Long id1 = 1L;
        Long idFaux = 2L;
        Joueur j1 = Joueur.builder().id(id1).nom(nom1).build();
        List<Joueur> listeJoueurs = Arrays.asList(j1);

        when(joueurRepo.findAll()).thenReturn(listeJoueurs);


        when(joueurRepo.existsById(id1)).thenReturn(true);
        doNothing().when(joueurRepo).deleteById(id1);
        when(joueurRepo.findAll()).thenReturn(listeJoueurs);

        Assertions.assertDoesNotThrow(()->joueurService.deleteJoueurByNom(nom1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> joueurService.deleteJoueurByNom(nom2));
    }
}
