package nl.pinkroccade.intake.stuivenberg.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinimalPersonDTOTest {


    @Test
    void givenMinor_shouldReturnTrue() {
        var birthDate = LocalDate.of(2010, 3, 20);
        var person = new MinimalPersonDTO(UUID.randomUUID(), "Peter Minor", birthDate);

        var result = person.isMinor();

        // Dan zou de persoon minderjarig moeten zijn, dus resultaat moet true zijn
        assertTrue(result, "De persoon zou minderjarig moeten zijn.");
    }

    @Test
    void givenAdult_shouldReturnFalse() {
        var birthDate = LocalDate.of(1992, 3, 20);
        var person = new MinimalPersonDTO(UUID.randomUUID(), "Peter Minor", birthDate);

        var result = person.isMinor();

        // Dan zou de persoon minderjarig moeten zijn, dus resultaat moet true zijn
        assertFalse(result, "De persoon zou volwassen moeten zijn.");
    }
}
