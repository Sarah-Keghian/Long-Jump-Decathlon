package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.JoueurDto;
import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.repositories.JoueurRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JoueurService {

    private final JoueurRepo joueurRepo;
    @Autowired
    public JoueurService(JoueurRepo joueurRepo) {this.joueurRepo = joueurRepo;}


    public Joueur createJoueur(String nom){

        Joueur joueur = new Joueur();
        joueur.setNom(nom);
        return joueurRepo.save(joueur);
    }


    public Optional<Joueur> existsByNom(String nom){

        for (Joueur joueur : joueurRepo.findAll()){
            if (joueur.getNom().equals(nom)){
                return Optional.of(joueur);
            }
        }
        return Optional.empty();
    }

    public List<Joueur> getAllJoueurs(){

        return joueurRepo.findAll();
    }

    public Optional<Joueur> getJoueurByNom(String nom){

        List<Joueur> listeJoueurs = getAllJoueurs();

        for (Joueur joueur : listeJoueurs){
            if (joueur.getNom().equals(nom)){
                return Optional.of(joueur);
            }
        }
        return Optional.empty();
    }


    public JoueurDto convertJoueurToDto(Joueur joueur){

        JoueurDto joueurDto = new JoueurDto();
        joueurDto.setId(joueur.getId());
        joueurDto.setNom(joueur.getNom());
        return joueurDto;
    }
}
