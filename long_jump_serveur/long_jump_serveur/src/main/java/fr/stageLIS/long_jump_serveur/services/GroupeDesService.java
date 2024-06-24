package fr.stageLIS.long_jump_serveur.services;

import fr.stageLIS.long_jump_serveur.models.De;
import fr.stageLIS.long_jump_serveur.models.GroupeDes;
import fr.stageLIS.long_jump_serveur.repositories.GroupeDesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Optional<GroupeDes> groupeDes1 = groupeDesRepo.findById(id);
        if (groupeDes1.isPresent()){
            GroupeDes groupeDes2 = groupeDes1.get();
            groupeDes2.setListeDes(newGroupe.getListeDes());
            return groupeDesRepo.save(groupeDes2);
        }
        else {
            throw new IllegalArgumentException("Aucun Groupe de Dés n'a l'id : " + id);
        }
    }

    public void deleteGroupe(Long id){

        if (groupeDesRepo.existsById(id)){
            groupeDesRepo.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Aucun Groupe de Dés n'a l'id : " + id);
        }
    }
}
