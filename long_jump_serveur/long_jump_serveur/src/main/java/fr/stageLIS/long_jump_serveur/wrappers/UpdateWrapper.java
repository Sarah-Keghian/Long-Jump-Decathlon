package fr.stageLIS.long_jump_serveur.wrappers;

import fr.stageLIS.long_jump_serveur.DTO.GroupeDesDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UpdateWrapper {

    private Long id;
    private GroupeDesDto newGroupeDto;
}
