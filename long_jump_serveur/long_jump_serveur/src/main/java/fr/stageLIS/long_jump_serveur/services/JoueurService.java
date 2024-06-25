package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.repositories.JoueurRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JoueurService {


    private JoueurRepo joueurRepo;
    @Autowired
    public JoueurService(JoueurRepo joueurRepo) {this.joueurRepo = joueurRepo;}

    public Joueur createJoueur(String nom){

        Joueur joueur = new Joueur();
        joueur.setNom(nom);
        return joueurRepo.save(joueur);
    }

    public Joueur getJoueurById(Long id){

        Optional<Joueur> joueur = joueurRepo.findById(id);
        if (joueur.isPresent()){
            return joueur.get();
        }
        else {
            throw new IllegalArgumentException("Aucun Joueur n'a l'id : " + id);
        }
    }

    public List<Joueur> getAllJoueurs(){

        return joueurRepo.findAll();
    }

    public Joueur getJoueurByNom(String nom){

        List<Joueur> listeJoueurs = getAllJoueurs();

        for (Joueur joueur : listeJoueurs){
            if (joueur.getNom().equals(nom)){
                return joueur;
            }
        }
        throw new IllegalArgumentException("Le joueur " + nom + " n'existe pas");
    }

//    public void updateJoueur(){}

    public void deleteJoueurById(Long id){

        if (joueurRepo.existsById(id)){
            joueurRepo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Aucun Joueur n'a l'id : " + id);
        }
    }

    public void deleteJoueurByNom(String nom){

        try {
            Joueur joueur = getJoueurByNom(nom);
            deleteJoueurById(joueur.getId());
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Ce joueur n'existe pas");
        }
    }
}
