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

import java.util.Optional;

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

//    @Test
//    public void addEssai_Test(){
//
//        Long idPartie1 = 1L;
//        Long id1 = 1L;
//        Long idFaux = 2L;
//        int score1 = 3;
//        int score2 = 4;
//        int score3 = 5;
//        Essais essais1 = Essais.builder().idPartie(idPartie1).id(id1).build();
//        Essais essais2 = Essais.builder().idPartie(idPartie1).id(id1).score1(score1).build();
//
//        when(essaisService.getEssais(id1)).thenReturn(Optional.of(essais1));
//        when(essaisRepo.save(essais1)).thenReturn(essais1);
//
//        // 0 scores remplis :
//        Optional<Essais> essaisObtenu1 = essaisService.addEssai(id1, score1);
//
//        Assertions.assertNotNull(essaisObtenu1);
//        Assertions.assertEquals(Optional.class, essaisObtenu1.getClass());
//        Assertions.assertNotNull(essais1.getScore1());
//        Assertions.assertNull(essais1.getScore2());
//        Assertions.assertNull(essais1.getScore3());
//        Assertions.assertEquals(score1, essaisObtenu1.get().getScore1());
//
//        // 1 score remplis :
//
//        Optional<Essais>
//    }

}
