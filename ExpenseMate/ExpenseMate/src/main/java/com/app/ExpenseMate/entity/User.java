package com.app.ExpenseMate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "User")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String userId;
    private String name;
    private String email;
    private String phoneNumber;
}
