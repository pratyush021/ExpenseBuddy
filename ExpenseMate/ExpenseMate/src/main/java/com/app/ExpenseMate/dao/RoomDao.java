package com.app.ExpenseMate.dao;

import com.app.ExpenseMate.entity.Room;
import com.app.ExpenseMate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoomDao {

    public Room create(Room room);
    public Room addUser(String roomId, List<User> userList);
    public Room getRoomById(String roomId);
    public List<Room> getAllRoom(); 

}
