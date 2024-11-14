package com.app.ExpenseMate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Room")
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String roomId;
    private String roomName;
    private String createdBy;
    private String createdAt;
    private List<User> users;
}
