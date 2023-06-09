package com.Clinic.WebApp.service;

import com.Clinic.WebApp.model.PersonsModel;
import com.Clinic.WebApp.model.PetOwnerDTO;
import com.Clinic.WebApp.model.PetsModel;
import com.Clinic.WebApp.repository.PersonRepository;
import com.Clinic.WebApp.repository.PetsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PetsRepository petsRepository;

    public List<PersonsModel> getPersons(){
        return personRepository.getPersons();
    }

    public List<PetsModel> getMyPets(int idOwner){
        List<Integer> ownPetsIDs = petsRepository.getPetsByOwnerID(idOwner);
        List<PetsModel> pets = new LinkedList<>();

        for(int tmp : ownPetsIDs){
            pets.add(petsRepository.getById(tmp));
        }
        return pets;
    }

    public PersonsModel getById(int id){
        return personRepository.getById(id);
    }

    public List<PersonsModel> getByName(String name){
        if(Character.isLowerCase(name.charAt(0)))
            name = name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
        return personRepository.getByName(name);
    }

    public int save(List<PersonsModel> personsModels){
        return personRepository.save(personsModels);
    }

    public int addOwner(PetOwnerDTO newOwner) { return personRepository.addOwner(newOwner); }

    public int patch(int id, PersonsModel person){
        return updateOrPatch(id, person, true);
    }

    public int update(int id, PersonsModel person){
        return updateOrPatch(id, person, false);
    }
    public int delete(int id){
        return personRepository.delete(id);
    }

    private int updateOrPatch(int id, PersonsModel updatedPerson, boolean isPatch){
        PersonsModel oldPer = personRepository.getById(id);

        if (isPatch) {
            if (updatedPerson.getFirst_name() != null)
                oldPer.setFirst_name(updatedPerson.getFirst_name());
            if (updatedPerson.getLast_name() != null)
                oldPer.setLast_name(updatedPerson.getLast_name());
            if (updatedPerson.getTelephone() != null)
                oldPer.setTelephone(updatedPerson.getTelephone());
            if (updatedPerson.getCity() != null)
                oldPer.setCity(updatedPerson.getCity());
            if (updatedPerson.getEmail() != null)
                oldPer.setEmail(updatedPerson.getEmail());
            if (updatedPerson.getAddress() != null)
                oldPer.setAddress(updatedPerson.getAddress());
        }
        else
            oldPer = updatedPerson;

        return personRepository.update(id, oldPer);
    }
}
