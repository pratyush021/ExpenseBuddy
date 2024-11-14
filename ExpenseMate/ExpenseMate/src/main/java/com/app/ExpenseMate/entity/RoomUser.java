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
@Document(collection = "RoomUser")
public class RoomUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String roomId;
    private String userId;
}
