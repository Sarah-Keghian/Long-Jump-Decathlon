package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.repositories.EssaisRepo;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EssaisServiceTests {

    @Mock
    private EssaisRepo essaisRepo;

    @InjectMocks
    private EssaisService essaisService;

    @Test
    public void createEssais_Test(){

        Long idPartie1 = 1L;
        Long id1 = 2L;
        Essais essais1 = Essais.builder()
                .id(id1).idPartie(idPartie1).build();
        Essais essais0 = Essais.builder().idPartie(idPartie1).build();

        when(essaisRepo.save(essais0)).thenReturn(essais1);

        Essais essaisObtenu = essaisService.createEssais(idPartie1);
        Assertions.assertNotNull(essaisObtenu);
        Assertions.assertEquals(idPartie1, essaisObtenu.getIdPartie());
        Assertions.assertNull(essaisObtenu.getScore1());
        Assertions.assertNull(essaisObtenu.getScore2());
        Assertions.assertNull(essaisObtenu.getScore3());
    }

    @Test
    public void getEssais_Test(){

        Long id1 = 2L;
        Long idFaux = 3L;
        Essais essais1 = Essais.builder().id(id1).build();

        when(essaisRepo.findById(id1)).thenReturn(Optional.of(essais1));
        when(essaisRepo.findById(idFaux)).thenReturn(Optional.empty());

        Optional<Essais> essaisObtenu1 = essaisService.getEssais(id1);
        Optional<Essais> essaisObtenu2 = essaisService.getEssais(idFaux);

        Assertions.assertNotNull(essaisObtenu1);
        Assertions.assertNotNull(essaisObtenu2);
        Assertions.assertEquals(Optional.of(essais1), essaisObtenu1);
        Assertions.assertEquals(Optional.empty(), essaisObtenu2);
    }

    @Test
    public void addEssai_Test(){

        Long idPartie1 = 1L;
        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;
        Long id4 = 4L;
        Long idFaux = 5L;
        int score1 = 3;
        int score2 = 4;
        int score3 = 5;
        Essais essais1 = Essais.builder().idPartie(idPartie1).id(id1).build();
        Essais essais2 = Essais.builder().idPartie(idPartie1).id(id2).score1(score1).build();
        Essais essais3 = Essais.builder().idPartie(idPartie1).id(id3)
                .score1(score1).score2(score2).build();
        Essais essais4 = Essais.builder().idPartie(idPartie1).id(id3)
                .score1(score1).score2(score2).score3(score3).build();

        when(essaisService.getEssais(id1)).thenReturn(Optional.of(essais1));
        when(essaisService.getEssais(id2)).thenReturn(Optional.of(essais2));
        when(essaisService.getEssais(id3)).thenReturn(Optional.of(essais3));
        when(essaisService.getEssais(id4)).thenReturn(Optional.of(essais4));

        when(essaisRepo.save(essais1)).thenReturn(essais1);
        when(essaisRepo.save(essais2)).thenReturn(essais2);
        when(essaisRepo.save(essais3)).thenReturn(essais3);

        // 0 scores remplis :
        Optional<Essais> essaisObtenu1 = essaisService.addEssai(id1, score1);

        Assertions.assertNotNull(essaisObtenu1);
        Assertions.assertEquals(Optional.class, essaisObtenu1.getClass());
        Assertions.assertEquals(score1, essaisObtenu1.get().getScore1());
        Assertions.assertNull(essaisObtenu1.get().getScore2());
        Assertions.assertNull(essaisObtenu1.get().getScore3());

        // 1 score remplis :

        Optional<Essais> essaisObtenu2 = essaisService.addEssai(id2, score2);

        Assertions.assertNotNull(essaisObtenu2);
        Assertions.assertEquals(Optional.class, essaisObtenu2.getClass());
        Assertions.assertEquals(score1, essaisObtenu2.get().getScore1());
        Assertions.assertEquals(score2, essaisObtenu2.get().getScore2());
        Assertions.assertNull(essais1.getScore3());

        // 2 scores remplis :

        Optional<Essais> essaisObtenu3 = essaisService.addEssai(id3, score3);

        Assertions.assertNotNull(essaisObtenu3);
        Assertions.assertEquals(Optional.class, essaisObtenu3.getClass());
        Assertions.assertEquals(score1, essaisObtenu3.get().getScore1());
        Assertions.assertEquals(score2, essaisObtenu3.get().getScore2());
        Assertions.assertEquals(score3, essaisObtenu3.get().getScore3());

        // 3 scores remplis :

        Optional<Essais> essaisObtenu4 = essaisService.addEssai(id4, 12);

        Assertions.assertNotNull(essaisObtenu4);
        Assertions.assertEquals(Optional.of(essais4), essaisObtenu4);
        Assertions.assertEquals(Optional.class, essaisObtenu4.getClass());
        Assertions.assertEquals(score1, essaisObtenu4.get().getScore1());
        Assertions.assertEquals(score2, essaisObtenu4.get().getScore2());
        Assertions.assertEquals(score3, essaisObtenu4.get().getScore3());
    }

    @Test
    public void deleteEssai_Test(){

        Long idPartie1 = 1L;
        Long id1 = 1L;
        Long idFaux = 2L;
        Essais essais1 = Essais.builder().idPartie(idPartie1).build();

        when(essaisService.getEssais(id1)).thenReturn(Optional.of(essais1));
        when(essaisService.getEssais(idFaux)).thenReturn(Optional.empty());
        doNothing().when(essaisRepo).deleteById(id1);

        Optional<Essais> essaisObtenu1 = essaisService.deleteEssais(id1);

        Assertions.assertNotNull(essaisObtenu1);
        Assertions.assertEquals(Optional.class, essaisObtenu1.getClass());
        Assertions.assertEquals(essais1, essaisObtenu1.get());

        Optional<Essais> essaisFaux = essaisService.deleteEssais(idFaux);
        Assertions.assertNotNull(essaisFaux);
        Assertions.assertEquals(Optional.class, essaisFaux.getClass());
        Assertions.assertEquals(Optional.empty(), essaisFaux);
    }
}
