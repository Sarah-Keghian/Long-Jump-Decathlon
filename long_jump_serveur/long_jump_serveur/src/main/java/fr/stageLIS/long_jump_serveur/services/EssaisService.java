package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.repositories.EssaisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EssaisService {

    private final EssaisRepo essaisRepo;

    @Autowired
    public EssaisService(EssaisRepo essaisRepo) {
        this.essaisRepo = essaisRepo;
    }

    public Essais createEssais(Long idPartie) {

        Essais essais = new Essais();
        essais.setIdPartie(idPartie);
        return essaisRepo.save(essais);
    }


    public Optional<Essais> getEssais(Long id){

        return essaisRepo.findById(id);

    }

    public Optional<Essais> addEssai(Long id, int score){

        Optional<Essais> essaisOptional = this.getEssais(id);

        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();

            if (essais.getScore1() == null) {
                essais.setScore1(score);
                essaisRepo.save(essais);
                return Optional.of(essais);
            }
            else if (essais.getScore2() == null) {
                essais.setScore2(score);
                essaisRepo.save(essais);
                return Optional.of(essais);
            }
            else if (essais.getScore3() == null) {
                essais.setScore3(score);
                essaisRepo.save(essais);
                return Optional.of(essais);
            }
            else {
                return Optional.of(essais);
            }
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<Essais> deleteEssais(Long id){

        Optional<Essais> essaisOptional = this.getEssais(id);

        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();
            essaisRepo.deleteById(id);
            return Optional.of(essais);
        }
        else {
            return Optional.empty();
        }
    }
}
