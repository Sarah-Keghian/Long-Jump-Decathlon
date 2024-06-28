package fr.stageLIS.long_jump_serveur.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Builder
public class LeaderDto {

    private String nomJoueur;
    private Integer place;
    private Integer score;
}