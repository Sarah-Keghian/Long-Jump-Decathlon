package fr.stageLIS.long_jump_serveur.service;

import fr.stageLIS.long_jump_serveur.DTO.DeDto;
import fr.stageLIS.long_jump_serveur.DTO.GroupeDesDto;
import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.models.GroupeDes;
import fr.stageLIS.long_jump_serveur.repositories.GroupeDesRepo;
import fr.stageLIS.long_jump_serveur.services.DeService;
import fr.stageLIS.long_jump_serveur.services.GroupeDesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupeDesServiceTests {


    @Mock
    GroupeDesRepo groupeDesRepo;
    @Mock
    DeService deService;

    @InjectMocks
    GroupeDesService groupeDesService;

    @Test
    public void createGroupe_Test() {

        Long idGroupe = 1L;
        Long idD1 = 1L;
        Long idD2 = 2L;
        List<Long> listeIds = Arrays.asList(idD1, idD2);
        GroupeDes groupeDesAvantSauve = GroupeDes.builder().id(idGroupe).build();
        De d1 = De.builder().id(idD1).frozen(false).idGroupe(idGroupe).build();
        De d2 = De.builder().id(idD2).frozen(false).idGroupe(idGroupe).build();
        GroupeDes groupeDesApres = GroupeDes.builder().id(idGroupe).listeDes(listeIds).build();

        when(groupeDesRepo.save(any())).thenReturn(groupeDesAvantSauve).thenReturn(groupeDesApres);
        when(deService.createDe(idGroupe)).thenReturn(d1, d2);

        GroupeDes groupeDesObtenu = groupeDesService.createGroupe();

        Assertions.assertNotNull(groupeDesObtenu);
        Assertions.assertEquals(idGroupe, groupeDesObtenu.getId());
        Assertions.assertEquals(listeIds, groupeDesObtenu.getListeDes());
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
        when(groupeDesRepo.findById(idFaux)).thenReturn(Optional.empty());


        Optional<GroupeDes> groupeObtenu = groupeDesService.getGroupe(id);
        Assertions.assertTrue(groupeObtenu.isPresent());
        Assertions.assertEquals(listeAttendue, groupeObtenu.get().getListeDes());

        Optional<GroupeDes> groupeDesOptional = groupeDesService.getGroupe(idFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
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

        Optional<GroupeDes> groupeObtenu = groupeDesService.updateGroupe(oldId, newGroupe);
        Assertions.assertTrue(groupeObtenu.isPresent());
        Assertions.assertEquals(newListeIds, groupeObtenu.get().getListeDes());
        Assertions.assertNotEquals(newGroupe.getId(), groupeObtenu.get().getId());

        Optional<GroupeDes> groupeDesOptional = groupeDesService.updateGroupe(idFaux, newGroupe);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
    }

    @Test
    public void deleteGroupe_Test() {

        Long id = 1L;
        Long idFaux = 2L;
        GroupeDes groupeDes = GroupeDes.builder().id(id).build();

        when(groupeDesRepo.findById(id)).thenReturn(Optional.of(groupeDes));
        when(groupeDesRepo.findById(idFaux)).thenReturn(Optional.empty());
        doNothing().when(groupeDesRepo).deleteById(id);

        Optional<GroupeDes> groupeObtenu = groupeDesService.deleteGroupe(id);
        Assertions.assertTrue(groupeObtenu.isPresent());

        Optional<GroupeDes> groupeDesOptional = groupeDesService.deleteGroupe(idFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
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

        Optional<GroupeDes> groupeObtenu = groupeDesService.throwGroupe(id);
        Assertions.assertTrue(groupeObtenu.isPresent());
        Assertions.assertEquals(id, groupeObtenu.get().getId());
        Assertions.assertEquals(listeDes.size(), groupeObtenu.get().getListeDes().size());

        Optional<GroupeDes> groupeDesOptional = groupeDesService.throwGroupe(idFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
    }

    @Test
    public void freezeDeGroupe_Test() {

        Long id = 1L;
        Long idFaux = 8L;
        Long idD1 = 3L;
        Long idD2 = 4L;
        Long idDFaux = 6L;
        Long idFreeze = 5L;


        List<Long> listeDes = Arrays.asList(idD1, idD2);
        List<Long> listeDesFreeze = Arrays.asList(idD1, idFreeze);

        GroupeDes groupe = GroupeDes.builder().id(id).listeDes(listeDes).build();
        GroupeDes groupeFreeze = GroupeDes.builder().id(id).listeDes(listeDesFreeze).build();

        when(groupeDesRepo.findById(id)).thenReturn(Optional.of(groupe));
        when(groupeDesRepo.save(groupe)).thenReturn(groupeFreeze);

        Optional<GroupeDes> groupeObtenu = groupeDesService.freezeDeGroupe(id, idD2);
        Assertions.assertTrue(groupeObtenu.isPresent());
        Assertions.assertEquals(listeDes.size(), groupeObtenu.get().getListeDes().size());
        Assertions.assertEquals(idFreeze, groupeObtenu.get().getListeDes().get(1));

        Optional<GroupeDes> groupeDesOptional1 = groupeDesService.freezeDeGroupe(idFaux, idD2);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional1);

        Optional<GroupeDes> groupeDesOptional2 = groupeDesService.freezeDeGroupe(id, idDFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional2);
    }


    @Test
    public void unFreezeDeGroupe_Test(){

        Long id = 1L;
        Long idFaux = 8L;
        Long idD1 = 3L;
        Long idD2 = 4L;
        Long idDFaux = 6L;

        List<Long> listeDes = Arrays.asList(idD1, idD2);
        GroupeDes groupeUnFreeze = GroupeDes.builder().id(id).listeDes(listeDes).build();

        when(groupeDesRepo.findById(id)).thenReturn(Optional.of(groupeUnFreeze));
        when(groupeDesRepo.save(groupeUnFreeze)).thenReturn(groupeUnFreeze);

        Optional<GroupeDes> groupeDesUnFreeze = groupeDesService.unFreezeDeGroupe(id, idD2);
        Assertions.assertTrue(groupeDesUnFreeze.isPresent());
        Assertions.assertEquals(listeDes.size(), groupeDesUnFreeze.get().getListeDes().size());
        Assertions.assertEquals(idD2, groupeDesUnFreeze.get().getListeDes().get(1));

        Optional<GroupeDes> groupeDesOptional1 = groupeDesService.unFreezeDeGroupe(idFaux, idD2);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional1);

        Optional<GroupeDes> groupeDesOptional2 = groupeDesService.unFreezeDeGroupe(id, idDFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional2);
    }


    @Test
    public void convertToDto_Test(){

        Long idD1 = 1L;
        Long idD2 = 2L;
        De d1 = De.builder().id(idD1).position(2).idGroupe(2L).frozen(false).build();
        De d2 = De.builder().id(idD2).position(4).idGroupe(2L).frozen(true).build();
        List<Long> listeIdDes = Arrays.asList(idD1, idD2);
        GroupeDes groupeDes = GroupeDes.builder()
                .id(1L)
                .listeDes(listeIdDes).build();

        when(deService.getDe(idD1)).thenReturn(Optional.of(d1));
        when(deService.getDe(idD2)).thenReturn(Optional.of(d2));

        GroupeDesDto groupeDesDto = groupeDesService.convertToDto(groupeDes);
        Assertions.assertNotNull(groupeDesDto);
        Assertions.assertEquals(GroupeDesDto.class, groupeDesDto.getClass());
        Assertions.assertEquals(groupeDes.getListeDes().size(), groupeDesDto.getListeDes().size());
    }

    @Test
    public void convertToEntity_Test(){

        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;
        Long idFaux = 4L;
        DeDto d1Dto = DeDto.builder().id(id1).idGroupe(2L).position(5).frozen(false).build();
        DeDto d2Dto = DeDto.builder().id(id2).idGroupe(2L).position(1).frozen(true).build();

        GroupeDes grp1 = GroupeDes.builder().id(id1).listeDes(Arrays.asList(id1, id2)).build();
        GroupeDes grp2 = GroupeDes.builder().id(id2).listeDes(List.of(id3)).build();

        List<DeDto> listeDtos = Arrays.asList(d1Dto, d2Dto);
        GroupeDesDto groupeDto = GroupeDesDto.builder()
                .id(id1)
                .listeDes(listeDtos).build();
        GroupeDesDto groupeDesDtoFaux = GroupeDesDto.builder()
                .id(idFaux)
                .listeDes(listeDtos).build();
        List<GroupeDes> listeGrpDes = Arrays.asList(grp1, grp2);
        when(groupeDesRepo.findAll()).thenReturn(listeGrpDes);

        Optional<GroupeDes> groupeDes = groupeDesService.convertToEntity(groupeDto);
        Assertions.assertNotNull(groupeDes);
        Assertions.assertEquals(GroupeDes.class, groupeDes.get().getClass());
        Assertions.assertEquals(groupeDto.getId(), groupeDes.get().getId());
        Assertions.assertEquals(groupeDto.getListeDes().stream().map(DeDto::getId).toList(), groupeDes.get().getListeDes());

        Optional<GroupeDes> groupeDesOptional = groupeDesService.convertToEntity(groupeDesDtoFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
    }
}
