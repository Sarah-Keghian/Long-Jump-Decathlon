# Diagrammes UML des classes serveur.

## Classes @Data (données)

| Classe @Data |
|--------|
| De |
|--------|
| + id : Long |
| + idGroupe : Long |
| + position : int |
| + frozen : boolean |
|--------|
| + De() : De |
| + De(id, idGroupe, position, frozen) : De |
| + getId() : Long |
| + getIdGroupe() : Long |
| + getPosition() : int |
| + isFrozen() : boolean |
| + setId(Long) : void |
| + setIdGroupe(Long) : void |
| + setPosition(int) : void |
| + setFrozen(boolean) : void |


| Classe @Data |
|--------|
| GroupeDes |
|--------|
| + id : Long |
| + listeDes : List<Long> |
|--------|
| + GroupeDes() : GroupeDes |
| + GroupeDes(id, listeDes) : GroupeDes |
| + getId() : Long |
| + getListeDes() : List<Long> |
| + setId(Long) : void |
| + setListeDes(List<Long>) : void |

| Classe @Data |
|--------|
| Score |
|--------|
| + id : Long |
| + idJoueur : Long |
| + scoreFinal : int |
|--------|
| Score() : Score |
| Score(id, idJoueur, scoreFinal) : Score |
| + getId() : Long |
| + getIdJoueur() : Long |
| + getScoreFinal() : int |
| + setId(Long) : void |
| + setIdJoueur(Long) : void |
| + setScoreFinal(int) : Long |

| Classe @Data |
|--------|
| Joueur |
|--------|
| + id : Long |
| + nom : String |
|--------|
| + Joueur() : Joueur |
| + Joueur(id, nom) : Joueur |
| + getId() : Long |
| + getNom() : String |
| + setId(Long) : void |
| + setNiom(String) : void |

## Classes DTO (Data Transfer Object)

| Classe |
|--------|
| DeDto |
|--------|
| + id : Long |
| + idGroupe : Long |
| + position : int |
| + frozen : boolean |
|--------|
| + DeDto() : DeDto |
| + DeDto(id, idGroupe, position, frozen) : DeDto |
| + getId() : Long |
| + getIdGroupe() : Long |
| + getPosition() : int |
| + isFrozen() : boolean |
| + setId(Long) : void |
| + setIdGroupe(Long) : void |
| + setPosition(int) : void |
| + setFrozen(boolean) : void |

| Classe |
|--------|
| GroupeDesDto |
|--------|
| + id : Long |
| + listeDes : List<Long> |
|--------|
| + GroupeDesDto() : GroupeDesDto |
| + GroupeDesDto(id, listeDes) : GroupeDesDto |
| + getId() : Long |
| + getListeDes() : List<Long> |
| + setId(Long) : void |
| + setListeDes(List<Long>) : void |

## Classes Service

| Classe |
|--------|
| DeService |
|--------|
| + deRepo : DeRepo |
|--------|
| + CRUD |
| + throwDe(Long) : De |
| + freezeDe(Long) : De |

| Classe |
|--------|
| GroupeDesService |
|--------|
| + groupeDesRepo : GroupeDesRepo |
|+ deService : DeService |
|--------|
| + CRUD |
| + throwGroupe(Long) : GroupeDes |
| + freezeGroupe(Long, Long) : GroupeDes |

| Classe |
|--------|
| ScoreService |
|--------|
| + scoreRepo : ScoreRepo |
|--------|
| + CRUD |

| Classe |
|--------|
| JoueurService |
|--------|
| + joueurRepo : JoueurRepo |
|--------|
| + CRUD |

## Classes Controller : 

| Classe |
|--------|
| GroupeDesController : Mapping : /api/GroupeDes |
|--------|
|+ groupeDesService : GroupeDesService |
|--------|
| + createGroupe(int nbDes) : ResponseEntity<GroupeDesDto>  ;  Mapping : /create |
| + getGroupe(Long id) : ResponseEntity<GroupeDesDto>  ;  Mapping : /get |
| + updateGroupe(Long id, GroupeDesDto newGroupeDto : ResponseEntity<GroupeDesDto>  ;  Mapping : /update |
| + deleteGroupe(Long id) : ResponseEntity<GroupeDesDto> ;  Mapping : /delete |
| + throwGroupe(Long) : ResponseEntity<GroupeDesDto> ;  Mapping : /throw |
| + freezeGroupe(Long, Long) : ResponseEntity<GroupeDesDto> ;  Mapping : /freeze |




