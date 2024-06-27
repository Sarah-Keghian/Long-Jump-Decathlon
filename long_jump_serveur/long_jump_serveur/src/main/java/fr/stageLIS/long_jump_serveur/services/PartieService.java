package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.LeaderDto;
import fr.stageLIS.long_jump_serveur.DTO.PartieDto;
import fr.stageLIS.long_jump_serveur.models.Essais;
import fr.stageLIS.long_jump_serveur.models.Joueur;
import fr.stageLIS.long_jump_serveur.models.Partie;
import fr.stageLIS.long_jump_serveur.repositories.PartieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PartieService {

    private final PartieRepo partieRepo;
    private final EssaisService essaisService;
    private final JoueurService joueurService;

    @Autowired
    public PartieService(PartieRepo partieRepo, EssaisService essaisService, JoueurService joueurService) {
        this.partieRepo = partieRepo;
        this.essaisService = essaisService;
        this.joueurService = joueurService;
    }

    public Partie createPartie(Long idJoueur) {

        Partie partie = new Partie();
        partie.setIdJoueur(idJoueur);

        return partieRepo.save(partie);
    }

    public Optional<Partie> getPartie(Long id) {

        return partieRepo.findById(id);
    }

    public List<Partie> getAllParties(){
        return partieRepo.findAll();
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

    public List<Partie> classerParties() {

        List<Partie> partiesListe = partieRepo.findAll();
        Collections.sort(partiesListe);
        return partiesListe;
    }

    // RENVOIE LA LISTE DES 15 MEILLEURS JOUEURS + SCORE
    public List<LeaderDto> getLeadersParties() {

        List<Partie> partiesLeadersList = this.classerParties()
                .subList(0, Math.min(this.classerParties().size(), 15));
        return this.convertToLeaderDtoList(partiesLeadersList);
    }

    public LeaderDto convertToLeaderDto(Partie partie) {

        LeaderDto leaderDto = new LeaderDto();
        leaderDto.setPlace(this.convertPartieToDto(partie).getPlace());

        List<Joueur> listeJoueurs = joueurService.getAllJoueurs();
        for (Joueur joueur : listeJoueurs) {
            if (joueur.getId().equals(partie.getIdJoueur())) {
                leaderDto.setNomJoueur(joueur.getNom());
            }
        }
        return leaderDto;
    }

    public List<LeaderDto> convertToLeaderDtoList(List<Partie> parties) {
        List<LeaderDto> leaderDtoList = new ArrayList<>();
        for (Partie partie : parties) {
            leaderDtoList.add(this.convertToLeaderDto(partie));
        }
        return leaderDtoList;
    }

    public PartieDto convertPartieToDto (Partie partie){

        PartieDto partieDto = new PartieDto();
        partieDto.setId(partie.getId());
        partieDto.setIdJoueur(partie.getIdJoueur());
        partieDto.setScoreFinal(partie.getScoreFinal());

        List<Partie> listePartieTrie = this.classerParties();
        for (int i=0 ; i<listePartieTrie.size() ; i++) {
            if (listePartieTrie.get(i).getId().equals(partie.getId())) {
                partieDto.setPlace(i+1);
            }
        }
        return partieDto;
    }
}
