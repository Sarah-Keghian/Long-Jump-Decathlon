package fr.stageLIS.long_jump_serveur.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder
public class Partie implements Comparable<Partie>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Long idJoueur;
    private Integer scoreFinal;


    public int compareTo(Partie other) {

        if (this.scoreFinal < other.scoreFinal){
            return -1;
        }
        else if (this.scoreFinal > other.scoreFinal){
            return 1;
        }
        else {
            return 0;
        }
    }
}

