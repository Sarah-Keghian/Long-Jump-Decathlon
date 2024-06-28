package fr.stageLIS.long_jump_serveur.wrappers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AddScoreWrapper {

    private Long id;

    private int score;
}
