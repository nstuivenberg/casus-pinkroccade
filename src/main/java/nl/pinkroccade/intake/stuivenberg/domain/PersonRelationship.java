package nl.pinkroccade.intake.stuivenberg.domain;

import jakarta.persistence.*;

@Entity
public class PersonRelationship {

    @EmbeddedId
    private PersonRelationshipId id;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipType relationshipType;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Person authority;

    public PersonRelationshipId getId() {
        return id;
    }

    public void setId(PersonRelationshipId id) {
        this.id = id;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Person getAuthority() {
        return authority;
    }

    public void setAuthority(Person authority) {
        this.authority = authority;
    }
}

