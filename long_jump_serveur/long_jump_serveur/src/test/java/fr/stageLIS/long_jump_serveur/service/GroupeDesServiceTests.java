package fr.stageLIS.long_jump_serveur.service;

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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GroupeDesServiceTests {

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

        GroupeDes groupeDes = GroupeDes.builder().id(id).build();

        when(groupeDesRepo.existsById(id)).thenReturn(true);
        when(groupeDesRepo.existsById(idFaux)).thenReturn(false);
        doNothing().when(groupeDesRepo).deleteById(id);

        Assertions
                .assertDoesNotThrow(()->groupeDesService.deleteGroupe(id));
        Assertions
                .assertThrows(IllegalArgumentException.class, ()->groupeDesService.deleteGroupe(idFaux));
    }
}
