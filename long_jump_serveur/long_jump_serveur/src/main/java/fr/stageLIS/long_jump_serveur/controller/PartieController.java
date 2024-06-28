package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.DTO.LeaderDto;
import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.services.PartieService;
import fr.stageLIS.long_jump_serveur.wrappers.AddScoreWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // A PRESENT IL A UN ATTRIBUT "place" QUI T'INDIQUE à QUELLE PLACE TU TE SITUES PAR RAPPORT A TOUTES LES AUTRES PARTIES
    @GetMapping("/get")
    public ResponseEntity<?> getPartie(@RequestBody Long id) {

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
    public ResponseEntity<?> addScoreFinal2(@RequestBody AddScoreWrapper addScoreWrapper) {

        Optional<Partie> partieOptional = partieService.addScoreFinalPartie2(addScoreWrapper.getId(), addScoreWrapper.getScore());
        if (partieOptional.isPresent()) {
            Partie partie = partieOptional.get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addScoreFinalOld")
    public ResponseEntity<?> addScoreFinal(@RequestBody Long id) {

        Optional<Partie> partieOptional = partieService.addScoreFinalPartie(id);
        if (partieOptional.isPresent()) {
            Partie partie = partieOptional.get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePartie(@RequestBody Long id) {

        Optional<Partie> partieOptional = partieService.getPartie(id);

        if (partieOptional.isPresent()) {
            Partie partie = partieService.deletePartie(id).get();
            return ResponseEntity.ok(partieService.convertPartieToDto(partie));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // RENVOIE UNE LISTE DES 15 MEILLEURS JOUEURS AVEC LE SCORE ASSOCIE
    @GetMapping("/getLeaders")
    public ResponseEntity<List<LeaderDto>> getLeadersParties() {

        return ResponseEntity.ok(partieService.getLeadersParties());
    }
}
