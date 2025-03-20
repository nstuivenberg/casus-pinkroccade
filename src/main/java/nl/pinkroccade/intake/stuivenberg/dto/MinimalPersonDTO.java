package nl.pinkroccade.intake.stuivenberg.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class MinimalPersonDTO {
    private UUID id;
    private String name;
    private LocalDate birthDate;

    public MinimalPersonDTO() {
    }

    public MinimalPersonDTO(UUID id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    @JsonIgnore
    public Boolean isMinor() {
        var today = LocalDate.now();

        return Period.between(birthDate, today).getYears() < 18;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
