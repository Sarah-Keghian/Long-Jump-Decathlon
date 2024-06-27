package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.repositories.PartieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartieService {

    private final PartieRepo partieRepo;
    private final EssaisService essaisService;

    @Autowired
    public PartieService(PartieRepo partieRepo, EssaisService essaisService) {
        this.partieRepo = partieRepo;
        this.essaisService = essaisService;
    }

    public Partie createPartie(Long idJoueur) {

        Partie partie = new Partie();
        partie.setIdJoueur(idJoueur);

        return partieRepo.save(partie);
    }

    public Optional<Partie> getPartie(Long id) {

        return partieRepo.findById(id);
    }

    public Optional<Partie> addScoreFinalPartie(Long id) {

        Optional<Partie> partieOptional = getPartie(id);


        if (partieOptional.isPresent()) {

            Partie partie = partieOptional.get();

            List<Essais> listeEssais = essaisService.getAllEssais();
            for (Essais essais : listeEssais) {

                if (essais.getIdPartie().equals(id)) {
                    int scoreFinal = Math.max
                            (Math.max(essais.getScore1(), essais.getScore2()), essais.getScore3());
                    partie.setScoreFinal(scoreFinal);
                    partieRepo.save(partie);
                    return Optional.of(partie);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Partie> deletePartie(Long id) {

        Optional<Partie> partieOptional = getPartie(id);
        if (partieOptional.isPresent()) {
            partieRepo.deleteById(id);
            return partieOptional;
        } else {
            return Optional.empty();
        }
    }

    public PartieDto convertPartieToDto (Partie partie){

        PartieDto partieDto = new PartieDto();
        partieDto.setId(partie.getId());
        partieDto.setIdJoueur(partie.getIdJoueur());
        partieDto.setScoreFinal(partie.getScoreFinal());
        return partieDto;
    }

    public Optional<Partie> convertDtoToPartie (Partie partie){

        List<Partie> partieListe = partieRepo.findAll();

        for (Partie partieIter : partieListe) {
            if (partieIter.getId().equals(partie.getId())) {
                return Optional.of(partieIter);
            }
        }
        return Optional.empty();
    }

}
