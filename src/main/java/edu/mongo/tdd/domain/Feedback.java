package edu.mongo.tdd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "feedback")
public class Feedback {

    private String id;

    private Integer productId;

    private Integer userId;

    private String status;

    private Integer version = 1;

    private String message;

    public Feedback(String id, Integer productId, Integer userId, String status, String message) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.status = status;
        this.message = message;
    }
}
