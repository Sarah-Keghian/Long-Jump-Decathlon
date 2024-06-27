package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.GroupeDesDto;
import fr.stageLIS.long_jump_serveur.models.GroupeDes;
import fr.stageLIS.long_jump_serveur.services.GroupeDesService;
import fr.stageLIS.long_jump_serveur.wrappers.FreezeWrapper;
import fr.stageLIS.long_jump_serveur.wrappers.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/GroupeDes")
public class GroupeDesController {


    private final GroupeDesService groupeDesService;
    @Autowired
    public GroupeDesController(GroupeDesService groupeDesService) {this.groupeDesService = groupeDesService;}


    @GetMapping("/create")
    public ResponseEntity<GroupeDesDto> createGroupe() {

        GroupeDesDto groupeDesDto = groupeDesService.convertToDto(groupeDesService.createGroupe());
        return ResponseEntity.ok(groupeDesDto);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getGroupe(@RequestBody Long id) {

        Optional<GroupeDes> groupeDesOptional = groupeDesService.getGroupe(id);
        if (groupeDesOptional.isPresent()) {
            GroupeDesDto grpDesDto = groupeDesService.convertToDto(groupeDesOptional.get());
            return ResponseEntity.ok(grpDesDto);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping("/update")
//    public ResponseEntity<GroupeDesDto> updateGroupe(@RequestBody UpdateWrapper<GroupeDesDto> updateWrapper) {
//
//        Optional<GroupeDes> newGroupe = groupeDesService.convertToEntity(updateWrapper.getObjet());
//        if (newGroupe.isPresent()) {
//
//        }
//        Optional<GroupeDes> groupeDes = groupeDesService.updateGroupe(updateWrapper.getId(), groupeDesService.convertToEntity(updateWrapper.getObjet()));
//        return ResponseEntity.ok(groupeDesService.convertToDto(groupeDes));
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGroupe(@RequestBody Long id) {

        Optional<GroupeDes> groupedDeletedOptional = groupeDesService.deleteGroupe(id);

        if (groupedDeletedOptional.isPresent()) {
            GroupeDes groupeDeleted = groupedDeletedOptional.get();
            return ResponseEntity.ok(groupeDesService.convertToDto(groupeDeleted));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/throw")
    public ResponseEntity<?> throwGroupe(@RequestBody Long id) {

        Optional<GroupeDes> groupeLanceOptional = groupeDesService.throwGroupe(id);

        if (groupeLanceOptional.isPresent()) {
            GroupeDes groupeLance = groupeLanceOptional.get();
            return ResponseEntity.ok(groupeDesService.convertToDto(groupeLance));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/freeze")
    public ResponseEntity<?> freezeGroupe(@RequestBody FreezeWrapper freezeWrapper) {

        Optional<GroupeDes> groupeDesOptional = groupeDesService.freezeDeGroupe(freezeWrapper.getId(), freezeWrapper.getIdDe());
        if (groupeDesOptional.isPresent()) {
            GroupeDes groupeDes = groupeDesOptional.get();
            return ResponseEntity.ok(groupeDesService.convertToDto(groupeDes));
        }
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/unFreeze")
    public ResponseEntity<?> unfreezeGroupe(@RequestBody FreezeWrapper freezeWrapper) {

            Optional<GroupeDes> groupeDesOptional = groupeDesService.unFreezeDeGroupe(freezeWrapper.getId(), freezeWrapper.getIdDe());
            if (groupeDesOptional.isPresent()) {
                GroupeDes groupeDes = groupeDesOptional.get();
                return ResponseEntity.ok(groupeDesService.convertToDto(groupeDes));
            }
            else {
                return ResponseEntity.notFound().build();
            }
    }
}
