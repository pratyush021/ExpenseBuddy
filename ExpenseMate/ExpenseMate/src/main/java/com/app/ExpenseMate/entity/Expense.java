package com.app.ExpenseMate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "Expense")
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String expenseId;
    private String roomId;
    private String paidBy;
    private double amount; // amount of each purchase e.g. one dinner with 4 people, then those 4 people needs to be added to the share list
    private String description;
    private String createdAt;
    private List<Share> shares;
}
