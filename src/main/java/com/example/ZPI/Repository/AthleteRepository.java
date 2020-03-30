package com.example.ZPI.Repository;

import com.example.ZPI.entities.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Integer> {

    List<Athlete> findAll();

    @Override
    Optional<Athlete> findById(Integer athleteId);

    List<Athlete> findAllByFirstNameContainsAndSurnameContainsAndNationalityContainsAndAndCategory(String firstName, String surname, String nationality, Athlete.Category category);

    List<Athlete> findAllByClub(Integer clubId);

    default List<Athlete> findAllFilter(String firstName, String surname, String nationality, Athlete.Category category) {
        return findAllByFirstNameContainsAndSurnameContainsAndNationalityContainsAndAndCategory(firstName, surname, nationality, category);
    }
}
