package fr.stageLIS.long_jump_serveur.repositories;

import fr.stageLIS.long_jump_serveur.models.De;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeRepo extends JpaRepository<De, Long> {
}
