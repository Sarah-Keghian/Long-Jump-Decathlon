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


    public GroupeDes createGroupe() {


        GroupeDes groupeDes = groupeDesRepo.save(new GroupeDes());
        List<Long> listeIds = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            De deTemp = deService.createDe(groupeDes.getId());
            listeIds.add(deTemp.getId());
        }
        groupeDes.setListeDes(listeIds);
        return groupeDesRepo.save(groupeDes);
    }

    public Optional<GroupeDes> getGroupe(Long id) {

        return groupeDesRepo.findById(id);
    }


    public Optional<GroupeDes> deleteGroupe(Long id) {

        Optional<GroupeDes> groupeDesOptional = groupeDesRepo.findById(id);
        if (groupeDesOptional.isPresent()) {
            GroupeDes groupeDes = groupeDesOptional.get();
            for (Long idDe : groupeDes.getListeDes()) {
                deService.deleteDe(idDe);
            }
            groupeDesRepo.deleteById(id);
            return Optional.of(groupeDes);
        } else {
            return Optional.empty();
        }
    }


    public Optional<GroupeDes> throwGroupe(Long id) {

        Optional<GroupeDes> groupeDesOptional = groupeDesRepo.findById(id);
        if (groupeDesOptional.isPresent()) {
            GroupeDes groupeDes = groupeDesOptional.get();
            for (Long idDe : groupeDes.getListeDes()) {
                deService.throwDe(idDe);
            }
            return Optional.of(groupeDesRepo.save(groupeDes));
        } else {
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
        }
        return Optional.empty();
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
        }
        return Optional.empty();
    }

    public GroupeDesDto convertToDto(GroupeDes groupeDes) {

        GroupeDesDto groupeDesDto = new GroupeDesDto();
        groupeDesDto.setId(groupeDes.getId());
        List<De> listeDes = new ArrayList<>();
        List<DeDto> listeDesDto = new ArrayList<>();

        for (Long id : groupeDes.getListeDes()) {
            if (deService.getDe(id).isPresent()) {
                listeDesDto.add(deService.convertToDTO(deService.getDe(id).get()));
            }
        }

        groupeDesDto.setListeDes(listeDesDto);
        return groupeDesDto;
    }
}