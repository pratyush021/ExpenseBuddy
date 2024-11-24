package com.app.ExpenseMate.dao.impl;

import com.app.ExpenseMate.dao.ExpenseDao;
import com.app.ExpenseMate.entity.Expense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Slf4j
public class ExpenseDaoImpl implements ExpenseDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String addExpense(Expense expense, String roomId) {
        expense.setRoomId(roomId);
        Expense addedObj = mongoTemplate.save(expense, "Expense");
        return "Added expense " + roomId;
    }

    @Override
    public Expense getRoomExpense(String expenseId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("expenseId").is(expenseId));
        return mongoTemplate.findOne(query, Expense.class);
    }

    @Override
    public String settleDebt(String expenseId) {
        //delete
        Query query = new Query();
        query.addCriteria(Criteria.where("expenseId").is(expenseId));
        return mongoTemplate.remove(query, Expense.class).toString();
    }

    @Override
    public List<Expense> getAllExpenseofRoom(String roomId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));
        return mongoTemplate.find(query, Expense.class);
    }

}
