package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.EssaisDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import fr.stageLIS.long_jump_serveur.wrappers.UpdateWrapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<EssaisDto> getEssais(@RequestBody Long id) {

        Optional<Essais> essaisOptional = essaisService.getEssais(id);
        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            String message = "Aucune partie n'a l'id" + id;
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addEssai")
    public ResponseEntity<EssaisDto> addEssai(@RequestBody UpdateWrapper<Integer> updateWrapper) {

        Optional<Essais> essaisOptional = essaisService.addEssai(updateWrapper.getId(), updateWrapper.getObjet());

        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            String message = "Aucun Essais n'a l'id" + updateWrapper.getId();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<EssaisDto> deleteEssais(@RequestBody Long id) {

        Optional<Essais> essaisOptional = essaisService.getEssais(id);
        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            String message = "Aucun Essai n'a l'id" + id;
            return ResponseEntity.notFound().build();
        }
    }
}
