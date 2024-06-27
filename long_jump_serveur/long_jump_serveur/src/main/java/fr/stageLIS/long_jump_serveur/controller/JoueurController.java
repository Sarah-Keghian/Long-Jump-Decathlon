package fr.stageLIS.long_jump_serveur.controller;

import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.services.EssaisService;
import fr.stageLIS.long_jump_serveur.services.JoueurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/Joueur")
public class JoueurController {

    private final JoueurService joueurService;
    private final EssaisService essaisService;

    public JoueurController(JoueurService joueurService, EssaisService essaisService) {this.joueurService = joueurService;
        this.essaisService = essaisService;
    }

    @PutMapping("/create")
    public ResponseEntity<Long> createJoueur(@RequestBody String nom) {

        Optional<Joueur> joueurOptional = joueurService.existsByNom(nom);
        if (joueurOptional.isPresent()) {
            Joueur joueur = joueurOptional.get();
            return ResponseEntity.ok(joueurService.convertJoueurToDto(joueur).getId());
        }
        else {
            return ResponseEntity.ok(joueurService.createJoueur(nom).getId());
        }
    }



}
