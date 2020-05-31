package com.example.ZPI.Repository;

import com.example.ZPI.entities.Athlete;
import com.example.ZPI.entities.Team;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class AthleteRepositoryCustomImpl implements AthleteRepositoryCustom{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean update(Athlete athlete) {
        Query query = new Query(Criteria.where("_id").is(athlete.getAthleteId()));
        Update update = new Update();
        update.set("performances",athlete.getPerformances());
        update.set("points", athlete.getPoints());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Athlete.class);
        return result.getModifiedCount()>0;
    }
}
