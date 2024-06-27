package fr.stageLIS.long_jump_serveur.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Builder
public class JoueurDto {

    private Long id;
    private String nom;
}
