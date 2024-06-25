package fr.stageLIS.long_jump_serveur.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder

public class DeDto {

    private Long id;

    private Long idGroupe;
    private Integer position;
    private boolean frozen;
}
