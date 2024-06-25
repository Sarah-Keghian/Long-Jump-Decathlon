package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.DTO.DeDto;
import fr.stageLIS.long_jump_serveur.DTO.GroupeDesDto;
import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.models.GroupeDes;
import fr.stageLIS.long_jump_serveur.repositories.DeRepo;
import fr.stageLIS.long_jump_serveur.repositories.GroupeDesRepo;
import fr.stageLIS.long_jump_serveur.services.DeService;
import fr.stageLIS.long_jump_serveur.services.GroupeDesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GroupeDesServiceTests {

    @Mock
    DeRepo deRepo;
    @Mock
    GroupeDesRepo groupeDesRepo;
    @Mock
    DeService deService;

    @InjectMocks
    GroupeDesService groupeDesService;

    @Test
    public void createGroupe_Test() {

        Long id = 1L;

        Long idD1 = 1L;
        Long idD2 = 2L;
        Long idD = 3L;
        Long idNonGroupe = 2L;

        List<Long> listeAttendue = Arrays.asList(idD1, idD2);

        De d1 = De.builder().id(idD1).idGroupe(id).build();
        De d2 = De.builder().id(idD2).idGroupe(id).build();
        De de = De.builder().id(idD).idGroupe(idNonGroupe).build();
        GroupeDes groupeDes = GroupeDes.builder()
                .id(id)
                .listeDes(Arrays.asList(idD1, idD2)).build();

        when(deService.getAllDe()).thenReturn(Arrays.asList(d1, d2, de));
        when(groupeDesRepo.save(groupeDes)).thenReturn(groupeDes);

        GroupeDes groupeObtenu = groupeDesService.createGroupe(id);

        Assertions.assertNotNull(groupeObtenu);
        Assertions.assertEquals(listeAttendue, groupeObtenu.getListeDes());
    }

    @Test
    public void getGroupe_Test() {

        Long id = 1L;
        Long idD1 = 1L;
        Long idD2 = 2L;
        Long idFaux = 5L;

        List<Long> listeAttendue = Arrays.asList(idD1, idD2);

        GroupeDes groupeD1 = GroupeDes.builder().id(id)
                .listeDes(listeAttendue).build();

        when(groupeDesRepo.findById(id)).thenReturn(Optional.of(groupeD1));


        GroupeDes groupeObtenu = groupeDesService.getGroupe(id);
        Assertions.assertNotNull(groupeObtenu);
        Assertions.assertEquals(listeAttendue, groupeObtenu.getListeDes());


        Assertions.assertThrows(IllegalArgumentException.class, () -> groupeDesService.getGroupe(idFaux));
    }

    @Test
    public void updateGroupe_Test() {

        Long oldId = 1L;
        Long newId = 8L;
        Long idD1 = 2L;
        Long idD2 = 3L;
        Long idD3 = 4L;
        Long idD4 = 5L;
        Long idFaux = 7L;
        List<Long> oldListeIds = Arrays.asList(idD3, idD4);
        List<Long> newListeIds = Arrays.asList(idD1, idD2);

        GroupeDes oldGroupe = GroupeDes.builder().id(oldId).listeDes(oldListeIds).build();
        GroupeDes newGroupe = GroupeDes.builder().id(newId).listeDes(newListeIds).build();

        when(groupeDesRepo.findById(oldId)).thenReturn(Optional.of(oldGroupe));
        when(groupeDesRepo.save(oldGroupe)).thenReturn(oldGroupe);

        GroupeDes groupeObtenu = groupeDesService.updateGroupe(oldId, newGroupe);
        Assertions.assertNotNull(groupeObtenu);
        Assertions.assertEquals(newListeIds, groupeObtenu.getListeDes());
        Assertions.assertNotEquals(newGroupe.getId(), groupeObtenu.getId());


        Assertions.assertThrows(IllegalArgumentException.class, () -> groupeDesService.updateGroupe(idFaux, newGroupe));
    }

    @Test
    public void deleteGroupe_Test() {
        Long id = 1L;
        Long idFaux = 2L;


        when(groupeDesRepo.existsById(id)).thenReturn(true);
        when(groupeDesRepo.existsById(idFaux)).thenReturn(false);
        doNothing().when(groupeDesRepo).deleteById(id);

        Assertions
                .assertDoesNotThrow(() -> groupeDesService.deleteGroupe(id));
        Assertions
                .assertThrows(IllegalArgumentException.class, () -> groupeDesService.deleteGroupe(idFaux));
    }

    @Test
    public void throwGroupe_Test() {

        Long id = 1L;
        Long idFaux = 2L;
        Long idD1 = 3L;
        Long idD2 = 4L;

        List<Long> listeDes = Arrays.asList(idD1, idD2);

        GroupeDes groupeAttendu = GroupeDes.builder().id(id).listeDes(listeDes).build();

        when(groupeDesRepo.findById(id)).thenReturn(Optional.of(groupeAttendu));
        when(groupeDesRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(groupeDesRepo.save(any())).thenReturn(groupeAttendu);

        GroupeDes groupeObtenu = groupeDesService.throwGroupe(id);
        Assertions.assertNotNull(groupeObtenu);
        Assertions.assertEquals(id, groupeObtenu.getId());
        Assertions.assertEquals(listeDes.size(), groupeObtenu.getListeDes().size());

        Assertions.assertThrows(IllegalArgumentException.class, () -> groupeDesService.throwGroupe(idFaux));
    }

    @Test
    public void freezeDeGroupe_Test() {


        Long id = 1L;
        Long idD1 = 3L;
        Long idD2 = 4L;
        Long idFreeze = 5L;

        List<Long> listeDes = Arrays.asList(idD1, idD2);
        List<Long> listeDesFreeze = Arrays.asList(idD1, idFreeze);

        GroupeDes groupe = GroupeDes.builder().id(id).listeDes(listeDes).build();
        GroupeDes groupeFreeze = GroupeDes.builder().id(id).listeDes(listeDesFreeze).build();

        when(groupeDesRepo.findById(id)).thenReturn(Optional.of(groupe));
        when(groupeDesRepo.save(groupe)).thenReturn(groupeFreeze);

        GroupeDes groupeObtenu = groupeDesService.freezeDeGroupe(id, idD2);
        Assertions.assertNotNull(groupeObtenu);
        Assertions.assertEquals(listeDes.size(), groupeObtenu.getListeDes().size());
        Assertions.assertEquals(idFreeze, groupeObtenu.getListeDes().get(1));
    }


    @Test
    public void convertToDto_Test(){

        Long id1 = 1L;
        Long id2 = 2L;
        De d1 = De.builder().id(id1).position(2).idGroupe(2L).frozen(false).build();
        De d2 = De.builder().id(id2).position(4).idGroupe(2L).frozen(true).build();
        List<Long> listeIdDes = Arrays.asList(id1, id2);
        GroupeDes groupeDes = GroupeDes.builder()
                .id(1L)
                .listeDes(listeIdDes).build();


        GroupeDesDto groupeDesDto = groupeDesService.convertToDto(groupeDes);
        Assertions.assertNotNull(groupeDesDto);
        Assertions.assertEquals(GroupeDesDto.class, groupeDesDto.getClass());
        Assertions.assertEquals(groupeDes.getListeDes().size(), groupeDesDto.getListeDes().size());
//        Assertions.assertNotNull(groupeDesDto.getListeDes().get(1));
//        Assertions.assertNotNull(groupeDesDto.getListeDes().get(0));

    }

    @Test
    public void convertToEntity_Test(){

        DeDto d1Dto = DeDto.builder().id(1L).idGroupe(2L).position(5).frozen(false).build();
        DeDto d2Dto = DeDto.builder().id(2L).idGroupe(2L).position(1).frozen(true).build();
        List<DeDto> listeDtos = Arrays.asList(d1Dto, d2Dto);
        GroupeDesDto groupeDto= GroupeDesDto.builder()
                .id(1L)
                .listeDes(listeDtos).build();

        GroupeDes groupeDes = groupeDesService.convertToEntity(groupeDto);
        Assertions.assertNotNull(groupeDes);
        Assertions.assertEquals(GroupeDes.class, groupeDes.getClass());
        Assertions.assertEquals(groupeDto.getId(), groupeDes.getId());
        Assertions.assertEquals(groupeDto.getListeDes().stream().map(DeDto::getId).toList(), groupeDes.getListeDes());

    }
}
