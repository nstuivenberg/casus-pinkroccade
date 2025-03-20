package nl.pinkroccade.intake.stuivenberg.service;

import nl.pinkroccade.intake.stuivenberg.dto.MinimalPersonDTO;
import nl.pinkroccade.intake.stuivenberg.dto.PersonCsvDTO;
import nl.pinkroccade.intake.stuivenberg.dto.PersonDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvServiceTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private CsvService csvService;

    @Test
    void givenResultFromFilter_shouldReturnCcvWithCorrectHeaderAndFilteredPerson() {
        List<PersonDTO> mockPersons = List.of(
                new PersonDTO(UUID.randomUUID(), "Alice", LocalDate.of(1990, 5, 10), null, null, null, null)
        );

        when(personService.getFilteredPersons(false, false, 0, 0)).thenReturn(mockPersons);

        String csv = csvService.filterPersonsAndReturnAsCsv(false, false, 0, 0);

        String expectedHeader = "ID,Name,BirthDate,Parent1Id,Parent2Id,PartnerId";
        assertTrue(csv.startsWith(expectedHeader));
        assertTrue(csv.contains("Alice"));
    }

    @Test
    void givenChildAndParent_shouldIncludeBothInCsv() {
        UUID childId = UUID.randomUUID();
        UUID parentId = UUID.randomUUID();
        MinimalPersonDTO child = new MinimalPersonDTO(childId, "Bob", LocalDate.of(2015, 8, 15));

        PersonDTO person = new PersonDTO(parentId, "Charlie", LocalDate.of(1985, 3, 22), null, null, List.of(child), null);

        String csv = csvService.generateCSV(List.of(person));

        assertTrue(csv.contains("Charlie"));
        assertTrue(csv.contains("Bob"));
        assertTrue(csv.contains(parentId.toString()));
    }

    @Test
    void givenListWithoutGivenChildPresent_shouldAddChild() {
        Set<PersonCsvDTO> csvRows = new HashSet<>();
        UUID parentId = UUID.randomUUID();
        UUID childId = UUID.randomUUID();
        MinimalPersonDTO child = new MinimalPersonDTO(childId, "Daisy", LocalDate.of(2018, 4, 1));

        csvService.addOrUpdateChild(csvRows, child, parentId);

        assertEquals(1, csvRows.size());
        assertTrue(csvRows.stream().anyMatch(row -> row.id().equals(childId) && row.parent1Id().equals(parentId)));
    }

    @Test
    void addOrUpdateChild_ShouldUpdateParent2IfAlreadyExists() {
        Set<PersonCsvDTO> csvRows = new HashSet<>();
        UUID childId = UUID.randomUUID();
        UUID firstParentId = UUID.randomUUID();
        UUID secondParentId = UUID.randomUUID();

        PersonCsvDTO existingChild = new PersonCsvDTO(childId, "Eve", LocalDate.of(2016, 7, 19), firstParentId, null, null);
        csvRows.add(existingChild);

        MinimalPersonDTO child = new MinimalPersonDTO(childId, "Eve", LocalDate.of(2016, 7, 19));

        // Act
        csvService.addOrUpdateChild(csvRows, child, secondParentId);

        // Assert
        assertEquals(1, csvRows.size());
        assertTrue(csvRows.stream().anyMatch(row -> row.id().equals(childId) && row.parent2Id().equals(secondParentId)));
    }
}
