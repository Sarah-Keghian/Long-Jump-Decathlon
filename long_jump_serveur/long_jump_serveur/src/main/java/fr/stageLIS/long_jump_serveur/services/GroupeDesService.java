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


    public Optional<GroupeDes> createGroupe(int nbDes){


        GroupeDes groupeDes = groupeDesRepo.save(new GroupeDes());
        List<Long> listeIds= new ArrayList<>();

        if (nbDes > 0) {
            for (int i = 0; i < nbDes; i++) {
                De deTemp = deService.createDe(groupeDes.getId());
                listeIds.add(deTemp.getId());
            }
            groupeDes.setListeDes(listeIds);
            return Optional.of(groupeDesRepo.save(groupeDes));
        }
        else {
            return Optional.empty();
        }

    }

    public Optional<GroupeDes> getGroupe(Long id){

        return groupeDesRepo.findById(id);
    }

    public Optional<GroupeDes> updateGroupe(Long id, GroupeDes newGroupe){

        Optional<GroupeDes> groupeDesOptional = groupeDesRepo.findById(id);
        if (groupeDesOptional.isPresent()) {
            GroupeDes groupeDes = groupeDesOptional.get();
            groupeDes.setListeDes(newGroupe.getListeDes());
            return Optional.of(groupeDesRepo.save(groupeDes));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<GroupeDes> deleteGroupe(Long id){

        Optional<GroupeDes> groupeDesOptional = groupeDesRepo.findById(id);
        if (groupeDesOptional.isPresent()) {
            GroupeDes groupeDes = groupeDesOptional.get();
            groupeDesRepo.delete(groupeDes);
            return Optional.of(groupeDes);
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<GroupeDes> throwGroupe(Long id){

        Optional<GroupeDes> groupeDesOptional = groupeDesRepo.findById(id);
        if (groupeDesOptional.isPresent()) {
            GroupeDes groupeDes = groupeDesOptional.get();
            for (Long idDe : groupeDes.getListeDes()){
                deService.throwDe(idDe);
            }
            return Optional.of(groupeDesRepo.save(groupeDes));
        }
        else {
            return Optional.empty();
        }

    }


    public Optional<GroupeDes> freezeDeGroupe(Long id, Long idDeChoisi) {

        Optional<GroupeDes> groupeDesOptional = groupeDesRepo.findById(id);

        if (groupeDesOptional.isPresent()) {
            GroupeDes groupeDes = groupeDesOptional.get();

            for (Long idDe : groupeDes.getListeDes()) {
                if (idDe.equals(idDeChoisi)) {
                    deService.freezeDe(idDe);
                    return Optional.of(groupeDesRepo.save(groupeDes));
                }
            }
            return Optional.empty();
        }
        else {
            return Optional.empty();
        }
    }


    public Optional<GroupeDes> unFreezeDeGroupe(Long id, Long idDeChoisi) {

        Optional<GroupeDes> groupeDesOptional = groupeDesRepo.findById(id);

        if (groupeDesOptional.isPresent()) {

            GroupeDes groupeDes = groupeDesOptional.get();
            for (Long idDe : groupeDes.getListeDes()) {
                if (idDe.equals(idDeChoisi)) {
                    deService.unFreezeDe(idDe);
                    return Optional.of(groupeDesRepo.save(groupeDes));
                }
            }
            return Optional.empty();
        }
        else {
            return Optional.empty();
        }
    }

    public GroupeDesDto convertToDto(GroupeDes groupeDes){
        GroupeDesDto groupeDesDto = new GroupeDesDto();
        groupeDesDto.setId(groupeDes.getId());
        List<De> listeDes = new ArrayList<>();
        List<DeDto> listeDesDto = new ArrayList<>();

        for (Long id : groupeDes.getListeDes()){
            listeDes.add(deService.getDe(id).get());
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