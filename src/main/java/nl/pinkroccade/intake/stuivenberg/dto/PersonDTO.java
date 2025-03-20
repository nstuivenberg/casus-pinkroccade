package nl.pinkroccade.intake.stuivenberg.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.*;

public class PersonDTO {
    private UUID id;
    private String name;
    private LocalDate birthDate;
    private MinimalPersonDTO parent1;
    private MinimalPersonDTO parent2;
    private List<MinimalPersonDTO> children;
    private MinimalPersonDTO partner;

    public PersonDTO() {
        this.children = new ArrayList<>();
    }

    public PersonDTO(UUID id, String name, LocalDate birthDate, MinimalPersonDTO parent1, MinimalPersonDTO parent2, List<MinimalPersonDTO> children, MinimalPersonDTO partner) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.children = children;
        this.partner = partner;
        if(this.children == null) {
            this.children = new ArrayList<>();
        }
    }

    public boolean hasPartner() {
        return partner != null;
    }

    public boolean hasMinors() {
        return children.stream().anyMatch(MinimalPersonDTO::isMinor);
    }

    @JsonIgnore
    public Integer getAmountOfMinors() {
        return (int) children.stream()
                .filter(MinimalPersonDTO::isMinor)
                .count();
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

    public MinimalPersonDTO getParent1() {
        return parent1;
    }

    public void setParent1(MinimalPersonDTO parent1) {
        this.parent1 = parent1;
    }

    public MinimalPersonDTO getParent2() {
        return parent2;
    }

    public void setParent2(MinimalPersonDTO parent2) {
        this.parent2 = parent2;
    }

    public List<MinimalPersonDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MinimalPersonDTO> children) {
        this.children = children;
    }

    public MinimalPersonDTO getPartner() {
        return partner;
    }

    public void setPartner(MinimalPersonDTO partner) {
        this.partner = partner;
    }
}

