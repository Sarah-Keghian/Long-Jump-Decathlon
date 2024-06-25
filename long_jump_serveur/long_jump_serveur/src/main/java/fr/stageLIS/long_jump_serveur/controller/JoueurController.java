package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.services.JoueurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Joueur")
public class JoueurController {

    private final JoueurService joueurService;
    public JoueurController(JoueurService joueurService) {this.joueurService = joueurService;}

    @PutMapping("/create")
    public ResponseEntity<Joueur> createJoueur(@RequestParam("nom") String nom) {
        return ResponseEntity.ok(joueurService.createJoueur(nom));
    }
}
