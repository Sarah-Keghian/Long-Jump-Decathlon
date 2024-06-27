package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.EssaisDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import fr.stageLIS.long_jump_serveur.wrappers.UpdateWrapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/Essais")
public class EssaisController {

    EssaisService essaisService;
    @Autowired
    public EssaisController(EssaisService essaisService) {this.essaisService = essaisService;}

    @PutMapping("/create")
    public ResponseEntity<EssaisDto> createEssais(@RequestBody Long idPartie) {

        Essais essaisCree = essaisService.createEssais(idPartie);
        return ResponseEntity.ok(essaisService.convertEssaisToDto(essaisCree));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getEssais(@RequestBody Long id) {

        Optional<Essais> essaisOptional = essaisService.getEssais(id);
        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            return new ResponseEntity<String>("Aucun Essais n'a l id" + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addEssai")
    public ResponseEntity<?> addEssai(@RequestBody UpdateWrapper<Integer> updateWrapper) {

        Optional<Essais> essaisOptional = essaisService.addEssai(updateWrapper.getId(), updateWrapper.getObjet());

        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            return new ResponseEntity<String>("Aucune Essais n'a l id" + updateWrapper.getId(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEssais(@RequestBody Long id) {

        Optional<Essais> essaisOptional = essaisService.getEssais(id);
        if (essaisOptional.isPresent()) {
            Essais essais = essaisService.deleteEssais(id).get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            return new ResponseEntity<String>("Aucun Essais n'a l id" + id, HttpStatus.NOT_FOUND);
        }
    }
}
