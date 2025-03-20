package nl.pinkroccade.intake.stuivenberg.service;

import nl.pinkroccade.intake.stuivenberg.dto.MinimalPersonDTO;
import nl.pinkroccade.intake.stuivenberg.dto.PersonCsvDTO;
import nl.pinkroccade.intake.stuivenberg.dto.PersonDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CsvService {
    private final PersonService personService;

    public CsvService(PersonService personService) {
        this.personService = personService;
    }
    public String filterPersonsAndReturnAsEncryptedCsv(Boolean hasMinor, Boolean hasPartner, Integer amountOfChildren, Integer minimumAmountOfMinors) {
        return Base64.getEncoder().encodeToString(this.filterPersonsAndReturnAsCsv(hasMinor, hasPartner, amountOfChildren, minimumAmountOfMinors).getBytes());
    }

    public String filterPersonsAndReturnAsCsv(Boolean hasMinor, Boolean hasPartner, Integer amountOfChildren, Integer minimumAmountOfMinors) {
        var filteredPersons = personService.getFilteredPersons(hasMinor, hasPartner, amountOfChildren, minimumAmountOfMinors);

        return this.generateCSV(filteredPersons);
    }

    String generateCSV(List<PersonDTO> personDTOList) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Name,BirthDate,Parent1Id,Parent2Id,PartnerId").append(System.lineSeparator());

        Set<PersonCsvDTO> csvRows = new HashSet<>();

        for (PersonDTO personDTO : personDTOList) {
            csvRows.add(personDtoToCsvRow(personDTO));

            if (personDTO.getParent1() != null) {
                csvRows.add(minimalPersonDtoToCsvRowParent(personDTO.getParent1()));
            }
            if (personDTO.getParent2() != null) {
                csvRows.add(minimalPersonDtoToCsvRowParent(personDTO.getParent2()));
            }
            for (MinimalPersonDTO child : personDTO.getChildren()) {
                addOrUpdateChild(csvRows, child, personDTO.getId());
            }
        }

        csvRows.forEach(row -> addRowToCsv(csvBuilder, row));

        return csvBuilder.toString();
    }

    private PersonCsvDTO personDtoToCsvRow(PersonDTO personDTO) {
        return new PersonCsvDTO(
                personDTO.getId(), personDTO.getName(), personDTO.getBirthDate(),
                personDTO.getParent1() != null ? personDTO.getParent1().getId() : null,
                personDTO.getParent2() != null ? personDTO.getParent2().getId() : null,
                personDTO.getPartner() != null ? personDTO.getPartner().getId() : null
        );
    }


    private PersonCsvDTO minimalPersonDtoToCsvRowParent(MinimalPersonDTO personDTO) {
        return new PersonCsvDTO(
                personDTO.getId(), personDTO.getName(), personDTO.getBirthDate(),
                null, null, null
        );
    }

    private void addRowToCsv(StringBuilder csvBuilder, PersonCsvDTO row) {
        csvBuilder
                .append(row.id())
                .append(",")
                .append(row.name())
                .append(",")
                .append(row.birthDate().toString())
                .append(",")
                .append(row.parent1Id())
                .append(",")
                .append(row.parent2Id())
                .append(",")
                .append(row.partnerId())
                .append(System.lineSeparator());
    }

    void addOrUpdateChild(Set<PersonCsvDTO> csvRows, MinimalPersonDTO child, UUID parentId) {
        for (PersonCsvDTO row : csvRows) {
            if (row.id().equals(child.getId())) {
                csvRows.remove(row);
                csvRows.add(new PersonCsvDTO(
                        row.id(), row.name(), row.birthDate(),
                        row.parent1Id() != null ? row.parent1Id() : parentId,
                        row.parent1Id() != null ? parentId : row.parent2Id(),
                        row.partnerId()
                ));
                return;
            }
        }
        csvRows.add(new PersonCsvDTO(child.getId(), child.getName(), child.getBirthDate(), parentId, null, null));
    }

}
