package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.repositories.DeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeService {

    @Autowired
    private DeRepo deRepo;

    public De createDe(Long idGroupe){
        De de = new De();
        de.setIdGroupe(idGroupe);
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

}
