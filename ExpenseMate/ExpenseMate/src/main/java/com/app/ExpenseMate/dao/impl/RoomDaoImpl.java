package com.app.ExpenseMate.dao.impl;

import com.app.ExpenseMate.dao.RoomDao;
import com.app.ExpenseMate.entity.Room;
import com.app.ExpenseMate.entity.User;
import com.app.ExpenseMate.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
public class RoomDaoImpl implements RoomDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Room create(Room room) {
        return mongoTemplate.save(room, "Room");
    }

    @Override
    public Room addUser(String roomId, List<User> userList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));
        Room room = mongoTemplate.findOne(query, Room.class);
        if(room == null) {
            log.error("Room with ID: {} not found", roomId);
            throw new NotFoundException("Room not found!");
        }
        room.setUsers(userList);
        Update update = new Update();
        update.addToSet("users", room.getUsers());
        return mongoTemplate.findAndModify(query, update, Room.class);
    }

    @Override
    public Room getRoomById(String roomId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));
        return mongoTemplate.findOne(query, Room.class, "Room");
    }
}
