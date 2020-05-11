package com.example.ZPI.Repository;

import com.example.ZPI.entities.Athlete;
import com.example.ZPI.entities.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AthleteRepository extends MongoRepository<Athlete, Integer>, AthleteRepositoryCustom {

    List<Athlete> findAll();

    @Override
    Optional<Athlete> findById(Integer id);

    Optional<Athlete> findByFirstNameContainsAndSurnameContains(String firstName, String lastName);

    List<Athlete> findAllByFirstNameContainsAndSurnameContainsAndNationalityContainsAndAndCategory(String firstName, String surname, String nationality, Athlete.Category category);

    List<Athlete> findAllByClub(Integer clubId);

    default List<Athlete> findAllFilter(String firstName, String surname, String nationality, Athlete.Category category) {
        return findAllByFirstNameContainsAndSurnameContainsAndNationalityContainsAndAndCategory(firstName, surname, nationality, category);
    }

}
