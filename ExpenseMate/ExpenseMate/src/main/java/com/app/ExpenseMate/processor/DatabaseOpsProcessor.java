package com.app.ExpenseMate.processor;

import ch.qos.logback.core.util.StringUtil;
import com.app.ExpenseMate.entity.Room;
import com.app.ExpenseMate.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DatabaseOpsProcessor {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Room createNewRoom(Room room) {
        log.info("[POST] adding room " + room.getRoomId() + " by " + room.getCreatedBy());
        String randomUUID = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        String uniqueId = randomUUID.substring(0, 4);
        room.setRoomId("r-"+uniqueId);
        return mongoTemplate.save(room, "Room");
    }
    /**
     * Adds users to a room by updating the list of users in the room.
     *
     * @param users  List of users to be added to the room.
     * @param roomId The ID of the room where users will be added.
     * @return The updated Room object after adding the users.
     * @throws IllegalArgumentException if the roomId is invalid or the room is not found.
     */
    public Room addUserToRoom(List<User> users, String roomId) {
        log.info("Starting process to add users to room with ID: {}", roomId);
        if(StringUtil.isNullOrEmpty(roomId)) {
            throw new IllegalArgumentException("Invalid roomId");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));
        Room room = mongoTemplate.findOne(query, Room.class);
        if(room == null) {
            log.error("Room with ID: {} not found", roomId);
            throw new IllegalArgumentException("Room not found for ID: "+roomId);
        }

        room.setUsers(users);
        Update update = new Update();
        update.addToSet("users", room.getUsers());
        return mongoTemplate.findAndModify(query, update, Room.class);
    }

    public Room getRoomById(String roomId) {
        log.info("Finding room by ID: {}", roomId);
        if(StringUtil.isNullOrEmpty(roomId)) {
            log.error("Invalid roomId: {}", roomId);
            throw new IllegalArgumentException("Invalid roomId");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));
        return mongoTemplate.findOne(query, Room.class, "Room");
    }
    public List<Room> getAll() {
        return mongoTemplate.findAll(Room.class, "Room");
    }

}
