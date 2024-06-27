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

    private final GroupeDesRepo groupeDesRepo;
    private final DeService deService;
    @Autowired
    public GroupeDesService(GroupeDesRepo groupeDesRepo, DeService deService) {
        this.groupeDesRepo = groupeDesRepo;
        this.deService = deService;
    }


    public GroupeDes createGroupe(int nbDes){


        GroupeDes groupeDes = groupeDesRepo.save(new GroupeDes());
        List<Long> listeIds= new ArrayList<>();

        if (nbDes > 0) {
            for (int i = 0; i < nbDes; i++) {
                De deTemp = deService.createDe(groupeDes.getId());
                listeIds.add(deTemp.getId());
            }
            groupeDes.setListeDes(listeIds);
            return groupeDesRepo.save(groupeDes);
        }
        else {
            throw new IllegalArgumentException("Le nombre de Dés doit être positif");
        }

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
                    deService.freezeDe(idDe);
            }
        }
        return groupeDesRepo.save(groupeDes);
    }


    public GroupeDes unFreezeDeGroupe(Long id, Long idDeChoisi) {

        GroupeDes groupeDes = this.getGroupe(id);

        for (Long idDe : groupeDes.getListeDes()) {
            if (idDe.equals(idDeChoisi)) {
                deService.unFreezeDe(idDe);
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

        groupeDesDto.setListeDes(listeDesDto);
        return groupeDesDto;
    }


    public GroupeDes convertToEntity(GroupeDesDto groupeDesDto){

        List<GroupeDes> listeGroupeDes = groupeDesRepo.findAll();

        for (GroupeDes groupeDes : listeGroupeDes) {
            if (groupeDes.getId().equals(groupeDesDto.getId())) {
                return groupeDes;
            }
        }
        throw new IllegalArgumentException();
    }
}