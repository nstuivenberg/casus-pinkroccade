package nl.pinkroccade.intake.stuivenberg.controller;

import nl.pinkroccade.intake.stuivenberg.dto.PersonDTO;
import nl.pinkroccade.intake.stuivenberg.service.CsvService;
import nl.pinkroccade.intake.stuivenberg.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/person")
public class PersonController {
    private PersonService personService;
    private CsvService csvService;

    @GetMapping("/persons")
    public List<PersonDTO> getPersons(
            @RequestParam(required = false) Boolean hasMinor,
            @RequestParam(required = false) Boolean hasPartner,
            @RequestParam(required = false) Integer amountOfChildren,
            @RequestParam(required = false) Integer minimumAmountOfMinors) {
        return personService.getFilteredPersons(hasMinor, hasPartner, amountOfChildren, minimumAmountOfMinors);
    }

    @GetMapping("/persons/csv")
    public String getPersonsAsCsv(
            @RequestParam(required = false) Boolean hasMinor,
            @RequestParam(required = false) Boolean hasPartner,
            @RequestParam(required = false) Integer amountOfChildren,
            @RequestParam(required = false) Integer minimumAmountOfMinors) {
        return csvService.filterPersonsAndReturnAsCsv(hasMinor, hasPartner, amountOfChildren, minimumAmountOfMinors);
    }

    @GetMapping("/persons/encrypted/csv")
    public String getPersonsAsEncryptedCsv(
            @RequestParam(required = false) Boolean hasMinor,
            @RequestParam(required = false) Boolean hasPartner,
            @RequestParam(required = false) Integer amountOfChildren,
            @RequestParam(required = false) Integer minimumAmountOfMinors) {
        return csvService.filterPersonsAndReturnAsEncryptedCsv(hasMinor, hasPartner, amountOfChildren, minimumAmountOfMinors);
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setCsvService(CsvService csvService) {
        this.csvService = csvService;
    }
}
