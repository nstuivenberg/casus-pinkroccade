package nl.pinkroccade.intake.stuivenberg.repository;

import nl.pinkroccade.intake.stuivenberg.domain.PersonRelationship;
import nl.pinkroccade.intake.stuivenberg.domain.PersonRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRelationshipRepository extends JpaRepository<PersonRelationship, PersonRelationshipId> {
    @Query("SELECT pr FROM PersonRelationship pr " +
            "LEFT JOIN FETCH pr.id.person1 " +
            "LEFT JOIN FETCH pr.id.person2 " +
            "LEFT JOIN FETCH pr.authority")
    List<PersonRelationship> findAllWithRelationships();
}

