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

    private final DeRepo deRepo;

    @Autowired
    public DeService(DeRepo deRepo) {
        this.deRepo = deRepo;
    }

    public De createDe(Long idGroupe) {
        De de = new De();
        de.setIdGroupe(idGroupe);
        de.setFrozen(false);
        return deRepo.save(de);
    }

    public List<De> getAllDe() {

        return deRepo.findAll();
    }

    public Optional<De> getDe(Long id) {

        return deRepo.findById(id);
    }

    public Optional<De> updateDe(Long id, De newDe) {

        Optional<De> de = deRepo.findById(id);
        if (de.isPresent()) {
            De oldDe = de.get();
            oldDe.setPosition(newDe.getPosition());
            return Optional.of(deRepo.save(oldDe));
        } else {
            return Optional.empty();
        }
    }

    public Optional<De> deleteDe(Long id) {

        Optional<De> de = deRepo.findById(id);
        if (de.isPresent()) {
            deRepo.deleteById(id);
            return de;
        } else {
            return Optional.empty();
        }
    }

    public void deleteAllDes() {

        deRepo.deleteAll();
    }

    public Optional<De> throwDe(Long id) {

        Random random = new Random();
        Optional<De> deOptional = deRepo.findById(id);

        if (deOptional.isPresent()) {
            De de = deOptional.get();

            if (!de.isFrozen()) {
                int nbAleatoire = random.nextInt(1, 7);
                de.setPosition(nbAleatoire);
                return Optional.of(deRepo.save(de));
            } else {
                return Optional.of(de);
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<De> freezeDe(Long id) {

        Optional<De> deOptional1 = deRepo.findById(id);
        if (deOptional1.isPresent()) {

            De de = deOptional1.get();
            if (!de.isFrozen()) {
                de.setFrozen(true);
                return Optional.of(deRepo.save(de));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<De> unFreezeDe(Long id){

        Optional<De> deOptional = deRepo.findById(id);
        if (deOptional.isPresent()) {
            De de = deOptional.get();
            if (de.isFrozen()){
                de.setFrozen(false);
                return Optional.of(deRepo.save(de));
            }
            else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public DeDto convertToDTO(De de){

        return DeDto.builder()
                .id(de.getId())
                .idGroupe(de.getIdGroupe())
                .position(de.getPosition())
                .frozen(de.isFrozen()).build();
    }

    public Optional<De> convertToEntity(DeDto deDto){

        List<De> des = deRepo.findAll();
        for (De de : des){
            if (de.getId().equals(deDto.getId())) {
                return Optional.of(de);
            }
        }
        return Optional.empty();
    }
}
