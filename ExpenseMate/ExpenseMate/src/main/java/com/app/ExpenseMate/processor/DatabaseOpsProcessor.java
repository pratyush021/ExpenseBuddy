package com.app.ExpenseMate.processor;

import ch.qos.logback.core.util.StringUtil;
import com.app.ExpenseMate.dao.ExpenseDao;
import com.app.ExpenseMate.dao.RoomDao;
import com.app.ExpenseMate.entity.Expense;
import com.app.ExpenseMate.entity.Room;
import com.app.ExpenseMate.entity.User;
import com.app.ExpenseMate.exception.InvalidInputException;
import com.app.ExpenseMate.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DatabaseOpsProcessor {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private ExpenseDao expenseDao;

    /**
     * Adds an expense to a specified room.
     *
     * @param expense the expense to be added
     * @param roomId the ID of the room to which the expense will be added
     * @return the ID of the added expense
     */
    public String addExpense(Expense expense, String roomId) {
        log.info("[POST] adding expense to room {}", roomId);
        expenseDao.addExpense(expense, roomId);
        return expense.getExpenseId();
    }
    /**
     * Creates a new room.
     *
     * @param room the room to be created
     * @return the created room
     * @throws InvalidInputException if the room name is null or empty
     */
    public Room createNewRoom(Room room) {
        log.info("[POST] adding room " + room.getRoomId() + " by " + room.getCreatedBy());
        if(StringUtil.isNullOrEmpty(room.getRoomName())) {
            throw new InvalidInputException("Room name can not be NULL or EMPTY!");
        }
        String randomUUID = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        String uniqueId = randomUUID.substring(0, 4);
        room.setRoomId("r-"+uniqueId);
        return roomDao.create(room);
    }

    public Room addUserToRoom(List<User> users, String roomId) {
        log.info("Starting process to add users to room with ID: {}", roomId);
        if(StringUtil.isNullOrEmpty(roomId)) {
            throw new InvalidInputException("RoomId can not be NULL or EMPTY!");
        }
        return roomDao.addUser(roomId, users);
    }

    public Room getRoomById(String roomId) {
        log.info("Finding room by ID: {}", roomId);
        if(StringUtil.isNullOrEmpty(roomId)) {
            log.error("Invalid roomId: {}", roomId);
            throw new InvalidInputException("RoomId can not be NULL or EMPTY!");
        }
       return roomDao.getRoomById(roomId);
    }


    public ResponseEntity<String> settle(String roomId) {
        // log the expenses and remove them from the room
        log.info("Settling expenses for -> {}", roomId);
        Room room = roomDao.getRoomById(roomId);
        if(room == null) {
            throw new NotFoundException("Room not found!");
        }
        List<Expense> expenseList = expenseDao.getAllExpenseofRoom(roomId);
        log.info("List of expense -> {}", expenseList);
        for (Expense expense : expenseList) {
            String deleted_result = expenseDao.settleDebt(expense.getExpenseId());
            log.info("DELETED RESULT -> {}", deleted_result);
        }
        return ResponseEntity.ok("Settled!");
    }

    public ResponseEntity<List<Expense>> getExpense(String roomId) {
        log.info("[GET] expense for roomId -> {}", roomId);
        Room room = roomDao.getRoomById(roomId);
        if(room == null) {
            throw new NotFoundException("Invalid roomId!");
        }
        List<Expense> expenseList = expenseDao.getAllExpenseofRoom(roomId);
        if(expenseList.isEmpty()) {
            throw new NotFoundException("No expenses found!");
        }
        return ResponseEntity.ok(expenseList);
    }
}
