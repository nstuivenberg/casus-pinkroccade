package nl.pinkroccade.intake.stuivenberg.service;

import nl.pinkroccade.intake.stuivenberg.domain.Person;
import nl.pinkroccade.intake.stuivenberg.domain.PersonRelationship;
import nl.pinkroccade.intake.stuivenberg.domain.RelationshipType;
import nl.pinkroccade.intake.stuivenberg.dto.MinimalPersonDTO;
import nl.pinkroccade.intake.stuivenberg.dto.PersonDTO;
import nl.pinkroccade.intake.stuivenberg.repository.PersonRelationshipRepository;
import nl.pinkroccade.intake.stuivenberg.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonRelationshipRepository personRelationshipRepository;

    public PersonService(PersonRepository personRepository, PersonRelationshipRepository personRelationshipRepository) {
        this.personRepository = personRepository;
        this.personRelationshipRepository = personRelationshipRepository;
    }

    public List<PersonDTO> getFilteredPersons(Boolean hasMinor, Boolean hasPartner, Integer amountOfChildren, Integer minimumAmountOfMinors) {
        return this.findAllWithRelationshipAndConvertToDto().stream()
                .filter(person -> {
                    boolean minorCondition = hasMinor == null || person.hasMinors() == hasMinor;
                    boolean partnerCondition = hasPartner == null || person.hasPartner() == hasPartner;
                    boolean childrenCondition = amountOfChildren == null || person.getChildren().size() == amountOfChildren;
                    boolean minorsCondition = minimumAmountOfMinors == null || person.getAmountOfMinors() >= minimumAmountOfMinors;
                    return minorCondition && partnerCondition && childrenCondition && minorsCondition;
                })
                .toList();
    }

    public List<PersonDTO> findAllWithRelationshipAndConvertToDto() {
        var relationships = personRelationshipRepository.findAllWithRelationships();
        var personsWithoutRelationship = personRepository.findAllPersonsWithoutRelationships();

        Map<UUID, PersonDTO> personDTOMap = new HashMap<>();

        processRelationships(relationships, personDTOMap);

        addPersonsWithoutRelationships(personsWithoutRelationship, personDTOMap);

        return List.copyOf(personDTOMap.values());
    }

    private void processRelationships(List<PersonRelationship> relationships, Map<UUID, PersonDTO> personDTOMap) {
        for (PersonRelationship pr : relationships) {
            var person1DTO = getOrCreatePersonDTO(pr.getId().getPerson1(), personDTOMap);
            var person2DTO = getOrCreatePersonDTO(pr.getId().getPerson2(), personDTOMap);

            if (pr.getRelationshipType() == RelationshipType.PARENT_CHILD) {
                linkParentChildRelationship(pr, person1DTO, person2DTO);
            } else if (pr.getRelationshipType() == RelationshipType.PARTNER) {
                linkPartnerRelationship(person1DTO, person2DTO);
            }

            personDTOMap.put(person1DTO.getId(), person1DTO);
            personDTOMap.put(person2DTO.getId(), person2DTO);
        }
    }

    private void linkParentChildRelationship(PersonRelationship pr, PersonDTO person1DTO, PersonDTO person2DTO) {
        if (pr.getAuthority().getId().equals(person1DTO.getId())) {
            person1DTO.getChildren().add(convertPersonDTOToMinimalPersonDTO(person2DTO));
            setParent(person2DTO, person1DTO);
        }
        if (pr.getAuthority().getId().equals(person2DTO.getId())) {
            person2DTO.getChildren().add(convertPersonDTOToMinimalPersonDTO(person1DTO));
            setParent(person1DTO, person2DTO);
        }
    }

    private void setParent(PersonDTO childDTO, PersonDTO parentDTO) {
        if (childDTO.getParent1() == null) {
            childDTO.setParent1(convertPersonDTOToMinimalPersonDTO(parentDTO));
        } else {
            childDTO.setParent2(convertPersonDTOToMinimalPersonDTO(parentDTO));
        }
    }

    private void linkPartnerRelationship(PersonDTO person1DTO, PersonDTO person2DTO) {
        person1DTO.setPartner(convertPersonDTOToMinimalPersonDTO(person2DTO));
        person2DTO.setPartner(convertPersonDTOToMinimalPersonDTO(person1DTO));
    }

    private PersonDTO getOrCreatePersonDTO(Person person, Map<UUID, PersonDTO> personDTOMap) {
        return personDTOMap.computeIfAbsent(person.getId(), id -> convertToPersonDTO(person));
    }

    private void addPersonsWithoutRelationships(List<Person> personsWithoutRelationship, Map<UUID, PersonDTO> personDTOMap) {
        for (Person person : personsWithoutRelationship) {
            personDTOMap.putIfAbsent(person.getId(), convertToPersonDTO(person));
        }
    }

    PersonDTO convertToPersonDTO(Person person) {
        if (person == null) {
            return null;
        }

        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setBirthDate(person.getBirthDate());
        dto.setChildren(new ArrayList<>());
        return dto;
    }

    private MinimalPersonDTO convertPersonDTOToMinimalPersonDTO(PersonDTO person) {
        if (person == null) {
            return null;
        }
        MinimalPersonDTO dto = new MinimalPersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setBirthDate(person.getBirthDate());
        return dto;
    }
}

