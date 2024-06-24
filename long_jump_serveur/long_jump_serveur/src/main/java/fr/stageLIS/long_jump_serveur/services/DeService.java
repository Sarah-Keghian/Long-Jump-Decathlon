package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.repositories.DeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DeService {

    @Autowired
    private DeRepo deRepo;

    public De createDe(Long idGroupe){
        De de = new De();
        de.setIdGroupe(idGroupe);
        de.setFrozen(false);
        return deRepo.save(de);
    }

    public List<De> getAllDe(){

        return deRepo.findAll();
    }

    public De getDe(Long id){

        Optional<De> de = deRepo.findById(id);
        if (de.isPresent()){
            return de.get();
        }
        else {
            throw new IllegalArgumentException("Aucun Dé n'a l'id : " +id);
        }
    }

    public De updateDe(Long id, De newDe) {
        Optional<De> de = deRepo.findById(id);
        if (de.isPresent()) {
            De oldDe = de.get();
            oldDe.setPosition(newDe.getPosition());
            return deRepo.save(oldDe);
        } else {
            throw new IllegalArgumentException("Aucun Dé n'a l'id : " +id);
        }
    }

    public void deleteDe(Long id){

        if (deRepo.existsById(id)){
            deRepo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Aucun Dé n'a l'id : " +id);
        }
    }

    public De throwDe(Long id){

        Random random = new Random();
        Optional<De> deOptional = deRepo.findById(id);
        if (deOptional.isPresent()){
            De de = deOptional.get();
            if (!de.isFrozen()){
                int nbAleatoire = random.nextInt(1,7);
                de.setPosition(nbAleatoire);
                return deRepo.save(de);
            }
            else {
                return de;
            }
        }
        else {
            throw new IllegalArgumentException("Aucun Dé n'a l'id : " +id);
        }
    }

    public De freezeDe(Long id){
        Optional<De> deOptional = deRepo.findById(id);
        if (deOptional.isPresent()){
            De de = deOptional.get();
            if (!de.isFrozen()){
                de.setFrozen(true);
                return deRepo.save(de);
            }
            else {
                throw new IllegalStateException("Le Dé est déjà gelé");
            }
        }
        else {
            throw new IllegalStateException("Le Dé est gelé : il ne peut plus être lancé");
        }
    }

}
