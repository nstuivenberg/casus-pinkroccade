package nl.pinkroccade.intake.stuivenberg.repository;

import nl.pinkroccade.intake.stuivenberg.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {
    @Query("SELECT p FROM Person p " +
            "LEFT JOIN PersonRelationship pr ON pr.id.person1 = p OR pr.id.person2 = p " +
            "WHERE pr.id.person1 IS NULL AND pr.id.person2 IS NULL")
    List<Person> findAllPersonsWithoutRelationships();
}
