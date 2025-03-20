package nl.pinkroccade.intake.stuivenberg.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PersonCsvDTO(
        UUID id,
        String name,
        LocalDate birthDate,
        UUID parent1Id,
        UUID parent2Id,
        UUID partnerId
) {}
