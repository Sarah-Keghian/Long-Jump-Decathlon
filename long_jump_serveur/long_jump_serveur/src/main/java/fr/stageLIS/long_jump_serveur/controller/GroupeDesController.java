package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.GroupeDesDto;
import fr.stageLIS.long_jump_serveur.models.GroupeDes;
import fr.stageLIS.long_jump_serveur.services.GroupeDesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/GroupeDes")
public class GroupeDesController {


    private final GroupeDesService groupeDesService;
    @Autowired
    public GroupeDesController(GroupeDesService groupeDesService) {this.groupeDesService = groupeDesService;}


    @PutMapping("/create")
    public ResponseEntity<GroupeDesDto> createGroupe(@RequestParam("nbDes") int nbDes) {
        GroupeDesDto groupeDesDto = groupeDesService.convertToDto(groupeDesService.createGroupe(nbDes));
        return ResponseEntity.ok(groupeDesDto);
    }

    @GetMapping("/get")
    public ResponseEntity<GroupeDesDto> getGroupe(@RequestParam("id") Long id) {
    try {
        GroupeDes groupeDes = groupeDesService.getGroupe(id);
        GroupeDesDto grpDesDto = groupeDesService.convertToDto(groupeDes);
        return ResponseEntity.ok(grpDesDto);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/update")
    public ResponseEntity<GroupeDesDto> updateGroupe(@RequestParam("id") Long id, @RequestBody GroupeDesDto newGroupeDto) {
    try {
        GroupeDes groupeDes = groupeDesService.updateGroupe(id, groupeDesService.convertToEntity(newGroupeDto));
        return ResponseEntity.ok(groupeDesService.convertToDto(groupeDes));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GroupeDesDto> deleteGroupe(@RequestParam("id") Long id) {
    try {
        groupeDesService.deleteGroupe(id);
        return ResponseEntity.ok().build();
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/throw")
    public ResponseEntity<GroupeDesDto> throwGroupe(@RequestParam("id") Long id){

    try {
        GroupeDes groupeLance = groupeDesService.throwGroupe(id);
        return ResponseEntity.ok(groupeDesService.convertToDto(groupeLance));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/freeze")
    public ResponseEntity<GroupeDesDto> freezeGroupe(@RequestParam("id") Long id, @RequestParam("idDe") Long idDe) {

    try {
        GroupeDes groupeDes = groupeDesService.freezeDeGroupe(id, idDe);
        return ResponseEntity.ok(groupeDesService.convertToDto(groupeDes));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
        }
    }
}