package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import fr.stageLIS.long_jump_serveur.services.PartieService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/Partie")
public class PartieController {

    private final PartieService partieService;
    private final EssaisService essaisService;

    public PartieController(PartieService partieService, EssaisService essaisService) {this.partieService = partieService;
        this.essaisService = essaisService;
    }


    @PutMapping("/create")
    public ResponseEntity<PartieDto> createPartie(@RequestBody Long idJoueur) {

        Partie partie = partieService.createPartie(idJoueur);
        return ResponseEntity.ok(partieService.convertPartieToDto(partie));
    }

    @GetMapping("/get")
    public ResponseEntity<PartieDto> getPartie(@RequestParam Long id) {

        Optional<Partie> partieOptional = partieService.getPartie(id);

        if (partieOptional.isPresent()) {
            Partie partie = partieOptional.get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addScoreFinal")
    public ResponseEntity<PartieDto> addScoreFinal(@RequestBody Long id) {

        Optional<Partie> partieOptional = partieService.getPartie(id);
        if (partieOptional.isPresent()) {
            Partie partie = partieOptional.get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<PartieDto> deletePartie(@RequestParam Long id) {

        Optional<Partie> partieOptional = partieService.getPartie(id);

        if (partieOptional.isPresent()) {
            Partie partie = partieService.deletePartie(id).get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
