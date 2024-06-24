package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.models.Score;
import fr.stageLIS.long_jump_serveur.repositories.ScoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepo scoreRepo;
    @Autowired
    private JoueurService joueurService;



    public Score createScore(Long idJoueur){
        Score score = new Score();
        score.setIdJoueur(idJoueur);
        return scoreRepo.save(score);
    }

    public Score getScoreById(Long id){
        Optional<Score> score = scoreRepo.findById(id);
        if (score.isPresent()) {
            return score.get();
        }
        else {
            throw new IllegalArgumentException("Aucun Score n'a l'id : " + id);
        }
    }

    public List<Score> getAllScores(){
        return scoreRepo.findAll();
    }


//    public Score getScoreByNom(String nom){
//
//        List<Score> scores = getAllScores();
//        List<Long> joueursIds = joueurService.getAllJoueurs().stream().map(Joueur::getId).toList();
//        for (Score score : scores) {
//            if (joueursIds.contains(score.getIdJoueur())) {
//                return score;
//            }
//        }
//        throw new IllegalArgumentException("Aucun Score associé au Joueur " + nom);
//    }

    public Score updateScoreById(Long id, Score newScore){

        Optional<Score> scoreOuNon = scoreRepo.findById(id);
        if (scoreOuNon.isPresent()) {
            Score score = scoreOuNon.get();
            score.setScoreFinal(newScore.getScoreFinal());
            return scoreRepo.save(score);
        }
        else {
            throw new IllegalArgumentException("Aucun Score n'a l'id : " + id);
        }
    }

    public void deleteScoreById(Long id){

        if (scoreRepo.existsById(id)) {
            scoreRepo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Aucun Score n'a l'id : " + id);
        }
    }
}
