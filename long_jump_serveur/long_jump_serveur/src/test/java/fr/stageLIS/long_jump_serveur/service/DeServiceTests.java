package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.repositories.DeRepo;
import fr.stageLIS.long_jump_serveur.services.DeService;
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
public class DeServiceTests {

    @Mock
    private DeRepo deRepo;

    @InjectMocks
    private DeService deService;

    @Test
    public void createDe_Test(){

        Long idGroupe = 1L;
        De d1 = new De();
        d1.setIdGroupe(idGroupe);

        when(deRepo.save(d1)).thenReturn(d1);

        De deObtenu = deService.createDe(idGroupe);

        Assertions.assertNotNull(deObtenu);
        Assertions.assertEquals(d1, deObtenu);
        Assertions.assertNull(deObtenu.getPosition());
    }

    @Test
    public void getDe_Test(){

        Long id = 1L;
        Long idFaux = 2L;
        De d1 = new De();
        d1.setId(id);
        d1.setIdGroupe(1L);

        when(deRepo.findById(id)).thenReturn(Optional.of(d1));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());

        try {
            De deObtenu = deService.getDe(id);
            Assertions.assertNotNull(deObtenu);
            Assertions.assertEquals(d1, deObtenu);
            Assertions.assertNotNull(deObtenu.getId());

        } catch(IllegalArgumentException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertThrows(IllegalArgumentException.class, () -> deService.getDe(idFaux));


    }

    @Test
    public void updateDe_Test(){

        Long id1 = 1L;
        Long id2 = 2L;
        Long groupeId1 = 1L;
        Long idFaux = 5L;
        De d1 = De.builder()
                .id(id1)
                .position(1)
                .idGroupe(id1).build();

        De d2 = De.builder()
                .id(id2)
                .idGroupe(groupeId1)
                .position(2).build();

        De d3 = De.builder()
                .id(id1)
                .idGroupe(groupeId1)
                .position(2).build();

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.save(d3)).thenReturn(d3);



        try {
            De deObtenu = deService.updateDe(id1, d2);
            Assertions.assertNotNull(deObtenu);
            Assertions.assertEquals(deObtenu, d3);
        } catch(IllegalArgumentException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertThrows(IllegalArgumentException.class, () -> deService.updateDe(idFaux, d2));
    }

    @Test
    public void deleteDe_Test(){
        Long id1 = 1L;
        Long idFaux = 2L;
        De d1 = De.builder()
                .id(id1)
                .position(1)
                .idGroupe(id1).build();

        when(deRepo.existsById(id1)).thenReturn(true);
        when(deRepo.existsById(idFaux)).thenReturn(false);
        doNothing().when(deRepo).deleteById(id1);

        Assertions.assertDoesNotThrow(() -> deService.deleteDe(id1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> deService.deleteDe(idFaux));
    }
}
