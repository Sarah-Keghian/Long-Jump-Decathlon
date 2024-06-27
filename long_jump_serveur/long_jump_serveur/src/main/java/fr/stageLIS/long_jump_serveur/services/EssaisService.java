package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.EssaisDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.repositories.EssaisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        essais.setScore1(-1);
        essais.setScore2(-1);
        essais.setScore3(-1);
        return essaisRepo.save(essais);
    }


    public Optional<Essais> getEssais(Long id){

        return essaisRepo.findById(id);

    }

    public List<Essais> getAllEssais(){

        return essaisRepo.findAll();
    }

    public Optional<Essais> addEssai(Long id, Integer score){

        Optional<Essais> essaisOptional = this.getEssais(id);

        if (essaisOptional.isPresent()) {
            Essais essais = essaisOptional.get();

            if (essais.getScore1() == -1) {
                essais.setScore1(score);
                essaisRepo.save(essais);
                return Optional.of(essais);
            }
            else if (essais.getScore2() == -1) {
                essais.setScore2(score);
                essaisRepo.save(essais);
                return Optional.of(essais);
            }
            else if (essais.getScore3() == -1) {
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

    public EssaisDto convertEssaisToDto(Essais essais){

        EssaisDto essaisDto = new EssaisDto();
        essaisDto.setId(essais.getId());
        essaisDto.setIdPartie(essais.getIdPartie());
        essaisDto.setScore1(essais.getScore1());
        essaisDto.setScore2(essais.getScore2());
        essaisDto.setScore3(essais.getScore3());
        return essaisDto;
    }

    public Optional<Essais> convertDtoToEssais(EssaisDto essaisDto){

        List<Essais> listeEssais = essaisRepo.findAll();

        for (Essais essais : listeEssais) {
            if (essais.getId().equals(essaisDto.getId())) {
                return Optional.of(essais);
            }
        }
        return Optional.empty();
    }
}
