package com.app.ExpenseMate.controller;

import com.app.ExpenseMate.entity.Expense;
import com.app.ExpenseMate.entity.Room;
import com.app.ExpenseMate.entity.User;
import com.app.ExpenseMate.processor.DatabaseOpsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class controller {

    @Autowired
    private DatabaseOpsProcessor databaseOpsProcessor;

    @PostMapping("/room/create")
    public ResponseEntity<Room> createRoom(
            @RequestBody Room room
    ) {
        Room addedRoom = databaseOpsProcessor.createNewRoom(room);
        return ResponseEntity.ok(addedRoom);
    }

    @PutMapping("/room")
    public ResponseEntity<String> addUsersToRoom(
            @RequestParam(value = "roomId", required = false) String roomId,
            @RequestBody List<User> users
            ) {
        databaseOpsProcessor.addUserToRoom(users, roomId);
        return ResponseEntity.ok("Users " + users + " added to " + roomId + ".");
    }

    @GetMapping("/room")
    public ResponseEntity<Room> getRoomDetails(
            @RequestParam(value = "roomId", required = false) String roomId
    ) {
        Room room = databaseOpsProcessor.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/expense")
    public ResponseEntity<String> addExpense(
            @RequestBody Expense expense,
            @RequestParam(value = "roomId") String roomId
    ) {
        String response = databaseOpsProcessor.addExpense(expense, roomId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/room/settle")
    public ResponseEntity<String> settleExpenses(
            @RequestParam(value = "roomId") String roomId
    ) {
        return databaseOpsProcessor.settle(roomId);
    }
    @GetMapping("/expense")
    public ResponseEntity<List<Expense>> getExpense(
        @RequestParam(value = "roomId") String roomId
    ) {
        return databaseOpsProcessor.getExpense(roomId);
    }

}
