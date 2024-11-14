package com.app.ExpenseMate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "Share")
public class Share implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String shareId;
    private String expenseId;
    private String owedBy;
    private String owedTo;
    private double amount;
}
