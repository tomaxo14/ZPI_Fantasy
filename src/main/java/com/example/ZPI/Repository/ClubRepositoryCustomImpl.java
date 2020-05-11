package com.example.ZPI.Repository;

import com.example.ZPI.entities.Club;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ClubRepositoryCustomImpl implements ClubRepositoryCustom{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean update(Club club) {
        Query query = new Query(Criteria.where("_id").is(club.getClubId()));
        Update update = new Update();
        update.set("matches", club.getMatches());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Club.class);
        return result.getModifiedCount()>0;
    }
}
