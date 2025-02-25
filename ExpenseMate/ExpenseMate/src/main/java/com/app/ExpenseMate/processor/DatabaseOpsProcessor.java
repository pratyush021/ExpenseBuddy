package com.app.ExpenseMate.processor;

import ch.qos.logback.core.util.StringUtil;
import com.app.ExpenseMate.dao.ExpenseDao;
import com.app.ExpenseMate.dao.RoomDao;
import com.app.ExpenseMate.dao.UserDao;
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

    @Autowired
    private UserDao userDao;

    /**
     * Adds an expense to a specified room.
     *
     * @param expense the expense to be added
     * @param roomId  the ID of the room to which the expense will be added
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
        if (StringUtil.isNullOrEmpty(room.getRoomName())) {
            throw new InvalidInputException("Room name can not be NULL or EMPTY!");
        }
        String randomUUID = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        String uniqueId = randomUUID.substring(0, 4);
        room.setRoomId("r-" + uniqueId);
        return roomDao.create(room);
    }
    /**
     * Adds users to a specified room.
     *
     * @param users the list of users to be added to the room
     * @param roomId the ID of the room to which the users will be added
     * @return the updated room with the added users
     * @throws InvalidInputException if the roomId is null or empty
     */
    public Room addUserToRoom(List<User> users, String roomId) {
        log.info("Starting process to add users to room with ID: {}", roomId);
        if (StringUtil.isNullOrEmpty(roomId)) {
            throw new InvalidInputException("RoomId can not be NULL or EMPTY!");
        }
        return roomDao.addUser(roomId, users);
    }
    /**
     * Retrieves a room by its ID.
     *
     * @param roomId the ID of the room to be retrieved
     * @return the room with the specified ID
     * @throws InvalidInputException if the roomId is null or empty
     */
    public Room getRoomById(String roomId) {
        log.info("Finding room by ID: {}", roomId);
        if (StringUtil.isNullOrEmpty(roomId)) {
            log.error("Invalid roomId: {}", roomId);
            throw new InvalidInputException("RoomId can not be NULL or EMPTY!");
        }
        return roomDao.getRoomById(roomId);
    }

    /**
     * Settles the expenses for a specified room.
     *
     * @param roomId the ID of the room for which expenses are to be settled
     * @return a ResponseEntity containing a confirmation message
     * @throws NotFoundException if the room is not found
     */
    public ResponseEntity<String> settle(String roomId) {
        // log the expenses and remove them from the room
        log.info("Settling expenses for -> {}", roomId);
        Room room = roomDao.getRoomById(roomId);
        if (room == null) {
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
    /**
     * Retrieves the list of expenses for a specified room.
     *
     * @param roomId the ID of the room for which expenses are to be retrieved
     * @return a ResponseEntity containing a list of expenses for the specified room
     * @throws NotFoundException if the room is not found or no expenses are found for the room
     */
    public ResponseEntity<List<Expense>> getExpense(String roomId) {
        log.info("[GET] expense for roomId -> {}", roomId);
        Room room = roomDao.getRoomById(roomId);
        if (room == null) {
            throw new NotFoundException("Invalid roomId!");
        }
        List<Expense> expenseList = expenseDao.getAllExpenseofRoom(roomId);
        if (expenseList.isEmpty()) {
            throw new NotFoundException("No expenses found!");
        }
        return ResponseEntity.ok(expenseList);
    }


  /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing a list of all users
     * @throws NotFoundException if no users are found
     */
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userDao.getAllUsers();
        if(userList.isEmpty()) throw new NotFoundException("No users found!");
        return ResponseEntity.ok(userList);
    }
    /**
     * Finds a user by email or phone number.
     *
     * @param email the email of the user to be found
     * @param phoneNumber the phone number of the user to be found
     * @return a ResponseEntity containing the found user
     * @throws NotFoundException if no user is found with the provided email or phone number
     */
    public ResponseEntity<User> findUserByPhoneOrEmail(String email, String phoneNumber) {
        User user = null;
        if (email != null && !email.isEmpty()) {
            user = userDao.getUserByEmail(email);
        }
        if (user == null && phoneNumber != null && !phoneNumber.isEmpty()) {
            user = userDao.getUserByPhoneNumber(phoneNumber);
        }
        if (user == null) {
            throw new NotFoundException("User not found with provided email or phone number");
        }
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> roomList = roomDao.getAllRoom();
        if(roomList.isEmpty()) throw new NotFoundException("No rooms found!");
        return ResponseEntity.ok(roomList);
    }
}
