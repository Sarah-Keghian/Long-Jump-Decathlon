package fr.stageLIS.long_jump_serveur.repositories;

import fr.stageLIS.long_jump_serveur.models.Partie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartieRepo extends JpaRepository<Partie, Long> {
}
