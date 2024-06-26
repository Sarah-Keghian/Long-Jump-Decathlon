package fr.stageLIS.long_jump_serveur.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class EssaisDto {

    private Long id;

    private Long idPartie;
    private Integer score1;
    private Integer score2;
    private Integer score3;
}
