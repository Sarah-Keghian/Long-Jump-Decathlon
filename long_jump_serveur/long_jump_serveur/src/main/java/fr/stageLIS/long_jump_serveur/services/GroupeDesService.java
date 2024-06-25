package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.DTO.DeDto;
import fr.stageLIS.long_jump_serveur.DTO.GroupeDesDto;
import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.models.GroupeDes;
import fr.stageLIS.long_jump_serveur.repositories.GroupeDesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupeDesService {

    @Autowired
    private GroupeDesRepo groupeDesRepo;

    @Autowired
    private DeService deService;

    public GroupeDes createGroupe(Long id){

        List<De> listeDes = deService.getAllDe();
        List<Long> listeIds= new ArrayList<>();

        for (De de : listeDes){
            if (de.getIdGroupe().equals(id)) {
                listeIds.add(de.getId());
            }
        }

        GroupeDes groupeDes = new GroupeDes();
        groupeDes.setId(id);
        groupeDes.setListeDes(listeIds);

        return groupeDesRepo.save(groupeDes);
    }

    public GroupeDes getGroupe(Long id){

        Optional<GroupeDes> groupeDes = groupeDesRepo.findById(id);
        if (groupeDes.isPresent()){
            return groupeDes.get();
        }
        else {
            throw new IllegalArgumentException("Aucun Groupe de Dés n'a l'id : " + id);
        }
    }

    public GroupeDes updateGroupe(Long id, GroupeDes newGroupe){

        GroupeDes groupeDes = getGroupe(id);
        groupeDes.setListeDes(newGroupe.getListeDes());
        return groupeDesRepo.save(groupeDes);
    }

    public void deleteGroupe(Long id){

        if (groupeDesRepo.existsById(id)){
            groupeDesRepo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Aucun Groupe de Dés n'a l'id : " + id);
        }
    }

    public GroupeDes throwGroupe(Long id){

        GroupeDes groupeDes = this.getGroupe(id);
        for (Long idDe : groupeDes.getListeDes()){
            deService.throwDe(idDe);
        }
        return groupeDesRepo.save(groupeDes);
    }


    public GroupeDes freezeDeGroupe(Long id, Long idDeChoisi) {

        GroupeDes groupeDes = this.getGroupe(id);

        for (Long idDe : groupeDes.getListeDes()) {
            if (idDe.equals(idDeChoisi)) {
                try {
                    deService.freezeDe(idDe);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            }
        }
        return groupeDesRepo.save(groupeDes);
    }

    public GroupeDesDto convertToDto(GroupeDes groupeDes){
        GroupeDesDto groupeDesDto = new GroupeDesDto();
        groupeDesDto.setId(groupeDes.getId());
        List<De> listeDes = new ArrayList<>();
        List<DeDto> listeDesDto = new ArrayList<>();

        for (Long id : groupeDes.getListeDes()){
            listeDes.add(deService.getDe(id));
        }
        for (De de : listeDes) {
            listeDesDto.add(deService.convertToDTO(de));
        }
//        List<DeDto> listeDesDto = groupeDes.getListeDes().stream()
//                .map(idDe -> deService.convertToDTO(deService.getDe(idDe)))
//                .collect(Collectors.toList());

        groupeDesDto.setListeDes(listeDesDto);
        return groupeDesDto;
    }



    public GroupeDes convertToEntity(GroupeDesDto groupeDesDto){
        GroupeDes groupeDes = new GroupeDes();
        groupeDes.setId(groupeDesDto.getId());

        List<Long> listeIdDes = groupeDesDto.getListeDes().stream().map(DeDto::getId).toList();
        groupeDes.setListeDes(listeIdDes);
        return groupeDes;
    }
}
