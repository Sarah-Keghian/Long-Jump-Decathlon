package fr.stageLIS.long_jump_serveur.DTO;

import fr.stageLIS.long_jump_serveur.models.De;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Builder
public class GroupeDesDto {

    private Long id;

    private List<DeDto> listeDes;
}
