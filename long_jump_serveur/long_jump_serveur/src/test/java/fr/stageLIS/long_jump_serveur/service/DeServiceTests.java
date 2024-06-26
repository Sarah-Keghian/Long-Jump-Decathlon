package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.DTO.DeDto;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeServiceTests {

    @Mock
    private DeRepo deRepo;

    @InjectMocks
    private DeService deService;

    @Test
    public void createDe_Test(){

        Long idGroupe = 1L;
        boolean frozenIni = false;
        De d1 = new De();
        d1.setIdGroupe(idGroupe);
        d1.setFrozen(frozenIni);

        when(deRepo.save(d1)).thenReturn(d1);

        De deObtenu = deService.createDe(idGroupe);

        Assertions.assertNotNull(deObtenu);
        Assertions.assertEquals(idGroupe, deObtenu.getIdGroupe());
        Assertions.assertEquals(frozenIni, deObtenu.isFrozen());
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


        when(deRepo.existsById(id1)).thenReturn(true);
        when(deRepo.existsById(idFaux)).thenReturn(false);
        doNothing().when(deRepo).deleteById(id1);

        Assertions.assertDoesNotThrow(() -> deService.deleteDe(id1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> deService.deleteDe(idFaux));
    }

    @Test
    public void throwDe_Test(){

        Long id1 = 1L;
        Long id2 = 2L;
        Long idFaux = 3L;
        boolean frozen = true;
        boolean notFrozen = false;

        De d1 = De.builder().id(id1).frozen(notFrozen).build();
        De d2 = De.builder().id(id2).frozen(frozen).build();

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.findById(id2)).thenReturn(Optional.of(d2));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(deRepo.save(d1)).thenReturn(d1);

        De deLance1 = deService.throwDe(id1);
        Assertions.assertNotNull(deLance1);
        Assertions.assertNotNull(deLance1.getPosition());
        Assertions.assertFalse(deLance1.isFrozen());
        Assertions.assertEquals(id1, deLance1.getId());
        Assertions.assertTrue(1 <= deLance1.getPosition());
        Assertions.assertTrue(deLance1.getPosition() <= 6);

        Assertions.assertEquals(d2, deService.throwDe(id2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> deService.throwDe(idFaux));
    }

    @Test
    public void freezeDe_Test(){

        Long id1 = 1L;
        Long id2 = 2L;

        Long idFaux = 3L;
        boolean frozen = true;
        boolean notFrozen = false;

        De d1 = De.builder().id(id1).frozen(notFrozen).build();
        De d2 = De.builder().id(id2).frozen(frozen).build();

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.findById(id2)).thenReturn(Optional.of(d2));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(deRepo.save(d1)).thenReturn(d1);

        De deFreeze1 = deService.freezeDe(id1);
        Assertions.assertNotNull(deFreeze1);
        Assertions.assertTrue(deFreeze1.isFrozen());

        Assertions.assertThrows(IllegalStateException.class, () -> deService.freezeDe(id2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> deService.freezeDe(idFaux));
    }

    @Test
    public void convertToDTO_Test(){
        De de = De.builder().id(1L).idGroupe(2L).position(2).frozen(true).build();

        DeDto deDto = deService.convertToDTO(de);

        Assertions.assertNotNull(deDto);
        Assertions.assertEquals(DeDto.class, deDto.getClass());
        Assertions.assertEquals(de.getId(), deDto.getId());
        Assertions.assertEquals(de.getIdGroupe(), deDto.getIdGroupe());
        Assertions.assertEquals(de.getPosition(), deDto.getPosition());
        Assertions.assertEquals(de.isFrozen(), deDto.isFrozen());
    }

    @Test
    public void convertToEntity_Test(){
        Long id1 = 1L;
        DeDto deDto = DeDto.builder()
                .id(id1).idGroupe(3L)
                .position(6)
                .frozen(false).build();
        De d1 = De.builder().id(id1).idGroupe(3L).position(6).frozen(false).build();
        De d2 = De.builder().id(2L).idGroupe(3L).position(2).frozen(false).build();

        when(deRepo.findAll()).thenReturn(Arrays.asList(d1,d2));

        De de = deService.convertToEntity(deDto);

        Assertions.assertNotNull(de);
        Assertions.assertEquals(De.class, de.getClass());
        Assertions.assertEquals(deDto.getId(), de.getId());
        Assertions.assertEquals(deDto.getIdGroupe(), de.getIdGroupe());
        Assertions.assertEquals(deDto.getPosition(), de.getPosition());
        Assertions.assertEquals(deDto.isFrozen(), de.isFrozen());

    }

}
