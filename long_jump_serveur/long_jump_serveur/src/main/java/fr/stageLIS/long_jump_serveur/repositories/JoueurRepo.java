package fr.stageLIS.long_jump_serveur.repositories;

import fr.stageLIS.long_jump_serveur.models.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoueurRepo extends JpaRepository<Joueur, Long> {
}
