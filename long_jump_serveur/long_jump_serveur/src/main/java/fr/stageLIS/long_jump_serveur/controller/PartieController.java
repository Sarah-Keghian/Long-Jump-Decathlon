package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import fr.stageLIS.long_jump_serveur.services.PartieService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/Partie")
public class PartieController {

    private final PartieService partieService;
    @Autowired
    public PartieController(PartieService partieService) {this.partieService = partieService;}


    @PutMapping("/create")
    public ResponseEntity<PartieDto> createPartie(@RequestBody Long idJoueur) {

        Partie partie = partieService.createPartie(idJoueur);
        return ResponseEntity.ok(partieService.convertPartieToDto(partie));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPartie(@RequestParam Long id) {

        Optional<Partie> partieOptional = partieService.getPartie(id);

        if (partieOptional.isPresent()) {
            Partie partie = partieOptional.get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return new ResponseEntity<String>("Aucune Partie n'a l'id" + id, HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping("/addScoreFinal")
    public ResponseEntity<?> addScoreFinal(@RequestBody Long id) {

        Optional<Partie> partieOptional = partieService.getPartie(id);
        if (partieOptional.isPresent()) {
            Partie partie = partieOptional.get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return new ResponseEntity<String>("Aucune Partie n'a l'id" + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePartie(@RequestParam Long id) {

        Optional<Partie> partieOptional = partieService.getPartie(id);

        if (partieOptional.isPresent()) {
            Partie partie = partieService.deletePartie(id).get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return new ResponseEntity<String>("Aucune Partie n'a l'id" + id, HttpStatus.NOT_FOUND);
        }
    }
}
