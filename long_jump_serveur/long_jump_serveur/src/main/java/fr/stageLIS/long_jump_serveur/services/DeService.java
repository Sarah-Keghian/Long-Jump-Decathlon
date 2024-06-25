package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.DeDto;
import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.repositories.DeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DeService {

    private DeRepo deRepo;
    @Autowired
    public DeService(DeRepo deRepo) {this.deRepo = deRepo;}

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
        De de = this.getDe(id);
        if (!de.isFrozen()){
            int nbAleatoire = random.nextInt(1,7);
            de.setPosition(nbAleatoire);
            return deRepo.save(de);
        }
        else {
            return de;
        }
    }

    public De freezeDe(Long id){
        De de = this.getDe(id);
        if (!de.isFrozen()){
            de.setFrozen(true);
            return deRepo.save(de);
        }
        else {
            throw new IllegalStateException("Le Dé est déjà gelé");
        }
    }

    public DeDto convertToDTO(De de){
//        DeDto deDto = new DeDto();
//        deDto.setFrozen(de.isFrozen());
//        deDto.setIdGroupe(de.getIdGroupe());
//        deDto.setPosition(de.getPosition());
//        deDto.setId(de.getId());
//        return deDto;
        return DeDto.builder()
                .id(de.getId())
                .idGroupe(de.getIdGroupe())
                .position(de.getPosition())
                .frozen(de.isFrozen()).build();
    }

    public De convertToEntity(DeDto deDto){

        List<De> des = deRepo.findAll();
        for (De de : des){
            if (de.getId().equals(deDto.getId())) {
                return de;
            }
        }
        throw new IllegalArgumentException();
    }

}
