package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.DeDto;
import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.repositories.DeRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeServiceTests {

    @Mock
    private DeRepo deRepo;

    @InjectMocks
    private DeService deService;

    Long id1 = 1L;
    Long id2 = 2L;
    Long idFaux = 20L;
    Long idGroupe = 3L;
    boolean frozen = true;
    boolean notFrozen = false;

    De d1SansId = De.builder().idGroupe(idGroupe).frozen(notFrozen).build();
    De d1 = De.builder().id(id1).idGroupe(idGroupe).frozen(notFrozen).build();
    De d2 = De.builder().id(id2).idGroupe(idGroupe).frozen(frozen).build();

    @Test
    public void createDe_Test(){


        when(deRepo.save(d1SansId)).thenReturn(d1);

        De deObtenu = deService.createDe(idGroupe);

        Assertions.assertNotNull(deObtenu);
        Assertions.assertEquals(idGroupe, deObtenu.getIdGroupe());
        Assertions.assertFalse(deObtenu.isFrozen());
        Assertions.assertNull(deObtenu.getPosition());
    }


    @Test
    public void getDe_Test(){

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());

        Optional<De> deObtenu = deService.getDe(id1);
        Optional<De> deOptional = deService.getDe(idFaux);

        Assertions.assertNotNull(deObtenu);
        Assertions.assertTrue(deObtenu.isPresent());
        Assertions.assertEquals(d1, deObtenu.get());
        Assertions.assertNotNull(deObtenu.get().getId());

        Assertions.assertEquals(Optional.empty(), deOptional);
    }


    @Test
    public void deleteDe_Test(){

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());
        doNothing().when(deRepo).deleteById(id1);

        Optional<De> deObtenu = deService.deleteDe(id1);
        Optional<De> deOptional = deService.deleteDe(idFaux);
        Assertions.assertTrue(deObtenu.isPresent());
        Assertions.assertEquals(Optional.empty(), deOptional);
    }


    @Test
    public void throwDe_Test(){

        De d1 = De.builder().id(id1).frozen(notFrozen).build();
        De d2 = De.builder().id(id2).frozen(frozen).build();

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.findById(id2)).thenReturn(Optional.of(d2));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(deRepo.save(d1)).thenReturn(d1);

        Optional<De> deLance1 = deService.throwDe(id1);
        Assertions.assertTrue(deLance1.isPresent());
        Assertions.assertNotNull(deLance1.get().getPosition());
        Assertions.assertFalse(deLance1.get().isFrozen());
        Assertions.assertEquals(id1, deLance1.get().getId());
        Assertions.assertTrue(1 <= deLance1.get().getPosition());
        Assertions.assertTrue(deLance1.get().getPosition() <= 6);

        Optional<De> deLance2 = deService.throwDe(id2);
        Assertions.assertTrue(deLance2.isPresent());
        Assertions.assertEquals(d2, deLance2.get());

        Optional<De> deOptional = deService.throwDe(idFaux);
        Assertions.assertEquals(Optional.empty(), deOptional);
    }


    @Test
    public void freezeDe_Test(){

        De d1 = De.builder().id(id1).frozen(notFrozen).build();
        De d2 = De.builder().id(id2).frozen(frozen).build();

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.findById(id2)).thenReturn(Optional.of(d2));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(deRepo.save(d2)).thenReturn(d2);
        when(deRepo.save(d1)).thenReturn(d1);

        Optional<De> deFreeze1 = deService.freezeDe(id1);
        Assertions.assertTrue(deFreeze1.isPresent());
        Assertions.assertTrue(deFreeze1.get().isFrozen());

        Optional<De> deFreeze2 = deService.freezeDe(id2);
        Assertions.assertTrue(deFreeze2.isPresent());
        Assertions.assertTrue(deFreeze2.get().isFrozen());

        Optional<De> deFaux = deService.freezeDe(idFaux);
        Assertions.assertEquals(Optional.empty(), deFaux);
    }


    @Test
    public void unFreeze_Test(){

        De d1 = De.builder().id(id1).frozen(frozen).build();
        De d2 = De.builder().id(id2).frozen(notFrozen).build();

        when(deRepo.findById(id1)).thenReturn(Optional.of(d1));
        when(deRepo.findById(id2)).thenReturn(Optional.of(d2));
        when(deRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(deRepo.save(d1)).thenReturn(d1);
        when(deRepo.save(d2)).thenReturn(d2);

        Optional<De> deUnFreeze1 = deService.unFreezeDe(id1);
        Assertions.assertTrue(deUnFreeze1.isPresent());
        Assertions.assertFalse(deUnFreeze1.get().isFrozen());

        Optional<De> deUnFreeze2 = deService.unFreezeDe(id2);
        Assertions.assertTrue(deUnFreeze2.isPresent());
        Assertions.assertFalse(deUnFreeze2.get().isFrozen());

        Optional<De> deFaux = deService.unFreezeDe(idFaux);
        Assertions.assertEquals(Optional.empty(), deFaux);
    }


    @Test
    public void convertToDTO_Test(){

        DeDto deDto = deService.convertToDTO(d1);

        Assertions.assertNotNull(deDto);
        Assertions.assertEquals(DeDto.class, deDto.getClass());
        Assertions.assertEquals(d1.getId(), deDto.getId());
        Assertions.assertEquals(d1.getIdGroupe(), deDto.getIdGroupe());
        Assertions.assertEquals(d1.getPosition(), deDto.getPosition());
        Assertions.assertEquals(d1.isFrozen(), deDto.isFrozen());
    }
}
