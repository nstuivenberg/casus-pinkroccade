package nl.pinkroccade.intake.stuivenberg.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nl.pinkroccade.intake.stuivenberg.domain.Person;
import nl.pinkroccade.intake.stuivenberg.domain.PersonRelationship;
import nl.pinkroccade.intake.stuivenberg.domain.PersonRelationshipId;
import nl.pinkroccade.intake.stuivenberg.domain.RelationshipType;
import nl.pinkroccade.intake.stuivenberg.dto.PersonDTO;
import nl.pinkroccade.intake.stuivenberg.repository.PersonRelationshipRepository;
import nl.pinkroccade.intake.stuivenberg.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;


@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonRelationshipRepository personRelationshipRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personService = new PersonService(personRepository, personRelationshipRepository);
    }

    @Test
    void testGetFilteredPersons_withHasMinorFilter() {
        Person person1 = new Person();
        person1.setId(UUID.randomUUID());
        person1.setName("John Doe");

        Person person2 = new Person();
        person2.setId(UUID.randomUUID());
        person2.setName("Jane Doe");
        person2.setBirthDate(LocalDate.now());

        PersonRelationshipId personRelationshipId = new PersonRelationshipId();
        personRelationshipId.setPerson1(person1);
        personRelationshipId.setPerson2(person2);
        PersonRelationship personRelationship = new PersonRelationship();
        personRelationship.setId(personRelationshipId);
        personRelationship.setRelationshipType(RelationshipType.PARENT_CHILD);
        personRelationship.setAuthority(person1);

        when(personRelationshipRepository.findAllWithRelationships()).thenReturn(List.of(personRelationship));

        List<PersonDTO> filteredPersons = personService.getFilteredPersons(true, null, null, null);

        assertEquals(1, filteredPersons.size());
        assertEquals("John Doe", filteredPersons.get(0).getName());
    }

    @Test
    void testGetFilteredPersons_withExactAmountOfChildrenFilter() {
        Person person1 = new Person();
        person1.setId(UUID.randomUUID());
        person1.setName("Papa Doe");

        Person person2 = new Person();
        person2.setId(UUID.randomUUID());
        person2.setName("Kiddy Doe");
        person2.setBirthDate(LocalDate.now());

        PersonRelationshipId personRelationshipId = new PersonRelationshipId();
        personRelationshipId.setPerson1(person1);
        personRelationshipId.setPerson2(person2);
        PersonRelationship personRelationship = new PersonRelationship();
        personRelationship.setId(personRelationshipId);
        personRelationship.setRelationshipType(RelationshipType.PARENT_CHILD);
        personRelationship.setAuthority(person1);

        when(personRelationshipRepository.findAllWithRelationships()).thenReturn(List.of(personRelationship));


        List<PersonDTO> filteredPersons = personService.getFilteredPersons(null, null, 1, null);

        assertEquals(1, filteredPersons.size());
        assertEquals("Papa Doe", filteredPersons.get(0).getName());
    }

    @Test
    void testGetFilteredPersons_withMinimumAmountOfMinors() {
        Person person1 = new Person();
        person1.setId(UUID.randomUUID());
        person1.setName("Papa Doe");

        Person person2 = new Person();
        person2.setId(UUID.randomUUID());
        person2.setName("Kiddy Doe");
        person2.setBirthDate(LocalDate.now());

        Person person3 = new Person();
        person3.setId(UUID.randomUUID());
        person3.setName("Kiddy Jane");
        person3.setBirthDate(LocalDate.now());

        PersonRelationshipId personRelationshipId = new PersonRelationshipId();
        personRelationshipId.setPerson1(person1);
        personRelationshipId.setPerson2(person2);
        PersonRelationship personRelationship = new PersonRelationship();
        personRelationship.setId(personRelationshipId);
        personRelationship.setRelationshipType(RelationshipType.PARENT_CHILD);
        personRelationship.setAuthority(person1);

        PersonRelationshipId personRelationshipId1 = new PersonRelationshipId();
        personRelationshipId1.setPerson1(person1);
        personRelationshipId1.setPerson2(person3);
        PersonRelationship personRelationship1 = new PersonRelationship();
        personRelationship1.setId(personRelationshipId1);
        personRelationship1.setRelationshipType(RelationshipType.PARENT_CHILD);
        personRelationship1.setAuthority(person1);

        when(personRelationshipRepository.findAllWithRelationships()).thenReturn(Arrays.asList(personRelationship, personRelationship1));
        List<PersonDTO> filteredPersons = personService.getFilteredPersons(null, null, null, 2);

        assertEquals(1, filteredPersons.size());
        assertEquals("Papa Doe", filteredPersons.get(0).getName());
    }

    @Test
    void testFindAllWithRelationshipAndConvertToDto() {
        Person person1 = new Person();
        person1.setId(UUID.randomUUID());
        person1.setName("John Doe");

        Person person2 = new Person();
        person2.setId(UUID.randomUUID());
        person2.setName("Jane Doe");

        PersonRelationshipId personRelationshipId = new PersonRelationshipId();
        personRelationshipId.setPerson1(person1);
        personRelationshipId.setPerson2(person2);
        PersonRelationship personRelationship = new PersonRelationship();
        personRelationship.setId(personRelationshipId);
        personRelationship.setRelationshipType(RelationshipType.PARTNER);

        List<PersonRelationship> relationships = List.of(personRelationship);

        when(personRelationshipRepository.findAllWithRelationships()).thenReturn(relationships);
        when(personRepository.findAllPersonsWithoutRelationships()).thenReturn(Collections.emptyList());

        List<PersonDTO> dtoList = personService.findAllWithRelationshipAndConvertToDto();

        assertEquals(2, dtoList.size());
        assertTrue(dtoList.stream().anyMatch(dto -> dto.getName().equals("John Doe")));
        assertTrue(dtoList.stream().anyMatch(dto -> dto.getName().equals("Jane Doe")));
    }

    @Test
    void testConvertToPersonDTO() {
        // Gegeven
        Person person = new Person();
        person.setId(UUID.randomUUID());
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        // Wanneer
        PersonDTO dto = personService.convertToPersonDTO(person);

        // Dan
        assertNotNull(dto);
        assertEquals("John Doe", dto.getName());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getBirthDate());
    }
}

