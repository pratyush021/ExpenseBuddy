package com.app.ExpenseMate.dao;

import com.app.ExpenseMate.entity.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ExpenseDao {

    public String addExpense(Expense expense, String roomId);

    public Expense getRoomExpense(String expenseId);

    public String settleDebt(String expenseId);

    public List<Expense> getAllExpenseofRoom(String roomId);
}
