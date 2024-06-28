package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.DTO.JoueurDto;
import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.repositories.JoueurRepo;
import fr.stageLIS.long_jump_serveur.services.JoueurService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
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

    String nom1 = "A";
    String nom2 = "B";
    String nom3 = "C";
    Long id1 = 1L;
    Long id2 = 2L;
    Long idFaux = 20L;

    Joueur joueur1SansId = Joueur.builder().nom(nom1).build();
    Joueur joueur1 = Joueur.builder().id(id1).nom(nom1).build();
    Joueur joueur2 = Joueur.builder().id(id2).nom(nom2).build();


    @Test
    public void createJoueur_Test(){

        when(joueurRepo.save(joueur1SansId)).thenReturn(joueur1);

        Joueur joueurObtenu = joueurService.createJoueur(nom1);

        Assertions.assertNotNull(joueurObtenu);
        Assertions.assertEquals(joueur1, joueurObtenu);
    }


    @Test
    public void existsByNom_Test(){

        List<Joueur> listeJoueurs = Arrays.asList(joueur1, joueur2);

        when(joueurRepo.findAll()).thenReturn(listeJoueurs);

        Optional<Joueur> joueurOptional = joueurService.existsByNom(nom1);
        Optional<Joueur> joueurEmpty = joueurService.existsByNom(nom3);

        Assertions.assertTrue(joueurOptional.isPresent());
        Assertions.assertTrue(joueurEmpty.isEmpty());
    }


    @Test
    public void getAllJoueurs_Test(){


        List<Joueur> listeJoueurs = Arrays.asList(joueur1, joueur2);

        when(joueurRepo.findAll()).thenReturn(listeJoueurs);

        List<Joueur> listeObteunue = joueurService.getAllJoueurs();
        Assertions.assertNotNull(listeObteunue);
        Assertions.assertEquals(listeObteunue, listeJoueurs);
    }


    @Test
    public void getJoueurByNom_Test(){

        List<Joueur> listeJoueurs = Arrays.asList(joueur1, joueur2);
        when(joueurRepo.findAll()).thenReturn(listeJoueurs);

        Optional<Joueur> joueurObtenu = joueurService.getJoueurByNom(nom1);
        Assertions.assertTrue(joueurObtenu.isPresent());
        Assertions.assertEquals(joueur1, joueurObtenu.get());
        Assertions.assertTrue(joueurService.getJoueurByNom(nom3).isEmpty());
    }


    @Test
    public void convertJoueurToDto_Test(){


        JoueurDto joueurDto = joueurService.convertJoueurToDto(joueur1);
        Assertions.assertNotNull(joueurDto);
        Assertions.assertEquals(JoueurDto.class, joueurDto.getClass());
        Assertions.assertEquals(joueur1.getNom(), joueurDto.getNom());
        Assertions.assertEquals(joueur1.getId(), joueurDto.getId());
    }

}
