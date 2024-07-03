package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.EssaisDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import fr.stageLIS.long_jump_serveur.wrappers.AddScoreWrapper;
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
    public ResponseEntity<?> getEssais(@RequestBody Long id) {

        Optional<Essais> essaisOptional = essaisService.getEssais(id);
        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addEssai")
    public ResponseEntity<?> addEssai(@RequestBody AddScoreWrapper addScoreWrapper) {

        Optional<Essais> essaisOptional = essaisService.addEssai(addScoreWrapper.getId(), addScoreWrapper.getScore());

        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            return ResponseEntity.ok(essaisService.convertEssaisToDto(essais));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEssais(@RequestBody Long id) {

        Optional<Essais> essaisOptional = essaisService.getEssais(id);
        if (essaisOptional.isPresent()) {
            essaisService.deleteEssais(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
