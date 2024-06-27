package fr.stageLIS.long_jump_serveur.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Builder
public class LeaderBoardDto {
    private Long id;

    private Long idJoueur;
    private Integer bestScore;
    private Integer place;
}
