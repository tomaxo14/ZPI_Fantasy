package com.example.ZPI.Repository;

import com.example.ZPI.entities.Team;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class TeamRepositoryCustomImpl implements TeamRepositoryCustom{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean update(Team team) {
        Query query = new Query(Criteria.where("_id").is(team.getTeamId()));
        Update update = new Update();
        update.set("athletes",team.getAthletes());
        update.set("points", team.getPoints());
        update.set("budget", team.getBudget());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Team.class);
        return result.getModifiedCount()>0;
    }
}
