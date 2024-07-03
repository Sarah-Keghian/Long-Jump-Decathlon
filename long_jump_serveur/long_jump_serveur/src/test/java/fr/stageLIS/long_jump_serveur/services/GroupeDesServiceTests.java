package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.GroupeDesDto;
import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.models.GroupeDes;
import fr.stageLIS.long_jump_serveur.repositories.GroupeDesRepo;
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

    Long idGroupe = 1L;
    Long idD1 = 1L;
    Long idD2 = 2L;
    Long idFaux = 20L;
    De d1 = De.builder().id(idD1).position(2).idGroupe(2L).frozen(false).build();
    De d2 = De.builder().id(idD2).position(4).idGroupe(2L).frozen(true).build();

    @Test
    public void createGroupe_Test() {


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

        List<Long> listeAttendue = Arrays.asList(idD1, idD2);

        GroupeDes groupeD1 = GroupeDes.builder().id(idGroupe)
                .listeDes(listeAttendue).build();

        when(groupeDesRepo.findById(idGroupe)).thenReturn(Optional.of(groupeD1));
        when(groupeDesRepo.findById(idFaux)).thenReturn(Optional.empty());


        Optional<GroupeDes> groupeObtenu = groupeDesService.getGroupe(idGroupe);
        Assertions.assertTrue(groupeObtenu.isPresent());
        Assertions.assertEquals(listeAttendue, groupeObtenu.get().getListeDes());

        Optional<GroupeDes> groupeDesOptional = groupeDesService.getGroupe(idFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
    }


    @Test
    public void deleteGroupe_Test() {


        List<Long> listeIdDes = Arrays.asList(idD1,idD2);
        GroupeDes groupeDes = GroupeDes.builder().listeDes(listeIdDes).id(idGroupe).build();

        when(groupeDesRepo.findById(idGroupe)).thenReturn(Optional.of(groupeDes));
        when(groupeDesRepo.findById(idFaux)).thenReturn(Optional.empty());
        doNothing().when(groupeDesRepo).deleteById(idGroupe);

        Optional<GroupeDes> groupeObtenu = groupeDesService.deleteGroupe(idGroupe);
        Assertions.assertTrue(groupeObtenu.isPresent());


        Optional<GroupeDes> groupeDesOptional = groupeDesService.deleteGroupe(idFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
    }


    @Test
    public void throwGroupe_Test() {

        List<Long> listeDes = Arrays.asList(idD1, idD2);

        GroupeDes groupeAttendu = GroupeDes.builder().id(idGroupe).listeDes(listeDes).build();

        when(groupeDesRepo.findById(idGroupe)).thenReturn(Optional.of(groupeAttendu));
        when(groupeDesRepo.findById(idFaux)).thenReturn(Optional.empty());
        when(groupeDesRepo.save(any())).thenReturn(groupeAttendu);

        Optional<GroupeDes> groupeObtenu = groupeDesService.throwGroupe(idGroupe);
        Assertions.assertTrue(groupeObtenu.isPresent());
        Assertions.assertEquals(idGroupe, groupeObtenu.get().getId());
        Assertions.assertEquals(listeDes.size(), groupeObtenu.get().getListeDes().size());

        Optional<GroupeDes> groupeDesOptional = groupeDesService.throwGroupe(idFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional);
    }


    @Test
    public void freezeDeGroupe_Test() {

        Long idDFaux = 6L;
        Long idFreeze = 5L;

        List<Long> listeDes = Arrays.asList(idD1, idD2);
        List<Long> listeDesFreeze = Arrays.asList(idD1, idFreeze);

        GroupeDes groupe = GroupeDes.builder().id(idGroupe).listeDes(listeDes).build();
        GroupeDes groupeFreeze = GroupeDes.builder().id(idGroupe).listeDes(listeDesFreeze).build();

        when(groupeDesRepo.findById(idGroupe)).thenReturn(Optional.of(groupe));
        when(groupeDesRepo.save(groupe)).thenReturn(groupeFreeze);

        Optional<GroupeDes> groupeObtenu = groupeDesService.freezeDeGroupe(idGroupe, idD2);
        Assertions.assertTrue(groupeObtenu.isPresent());
        Assertions.assertEquals(listeDes.size(), groupeObtenu.get().getListeDes().size());
        Assertions.assertEquals(idFreeze, groupeObtenu.get().getListeDes().get(1));

        Optional<GroupeDes> groupeDesOptional1 = groupeDesService.freezeDeGroupe(idFaux, idD2);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional1);

        Optional<GroupeDes> groupeDesOptional2 = groupeDesService.freezeDeGroupe(idGroupe, idDFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional2);
    }


    @Test
    public void unFreezeDeGroupe_Test(){

        Long idDFaux = 6L;

        List<Long> listeDes = Arrays.asList(idD1, idD2);
        GroupeDes groupeUnFreeze = GroupeDes.builder().id(idGroupe).listeDes(listeDes).build();

        when(groupeDesRepo.findById(idGroupe)).thenReturn(Optional.of(groupeUnFreeze));
        when(groupeDesRepo.save(groupeUnFreeze)).thenReturn(groupeUnFreeze);

        Optional<GroupeDes> groupeDesUnFreeze = groupeDesService.unFreezeDeGroupe(idGroupe, idD2);
        Assertions.assertTrue(groupeDesUnFreeze.isPresent());
        Assertions.assertEquals(listeDes.size(), groupeDesUnFreeze.get().getListeDes().size());
        Assertions.assertEquals(idD2, groupeDesUnFreeze.get().getListeDes().get(1));

        Optional<GroupeDes> groupeDesOptional1 = groupeDesService.unFreezeDeGroupe(idFaux, idD2);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional1);

        Optional<GroupeDes> groupeDesOptional2 = groupeDesService.unFreezeDeGroupe(idGroupe, idDFaux);
        Assertions.assertEquals(Optional.empty(), groupeDesOptional2);
    }


    @Test
    public void convertToDto_Test(){

        List<Long> listeIdDes = Arrays.asList(idD1, idD2);
        GroupeDes groupeDes = GroupeDes.builder()
                .id(idGroupe)
                .listeDes(listeIdDes).build();

        when(deService.getDe(idD1)).thenReturn(Optional.of(d1));
        when(deService.getDe(idD2)).thenReturn(Optional.of(d2));

        GroupeDesDto groupeDesDto = groupeDesService.convertToDto(groupeDes);
        Assertions.assertNotNull(groupeDesDto);
        Assertions.assertEquals(GroupeDesDto.class, groupeDesDto.getClass());
        Assertions.assertEquals(groupeDes.getListeDes().size(), groupeDesDto.getListeDes().size());
    }
}
