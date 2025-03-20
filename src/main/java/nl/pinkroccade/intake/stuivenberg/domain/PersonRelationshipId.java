package nl.pinkroccade.intake.stuivenberg.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class PersonRelationshipId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "person1_id", nullable = false)
    private Person person1;

    @ManyToOne
    @JoinColumn(name = "person2_id", nullable = false)
    private Person person2;

    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }
}

