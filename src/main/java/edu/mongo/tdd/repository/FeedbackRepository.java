package edu.mongo.tdd.repository;

import edu.mongo.tdd.domain.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FeedbackRepository extends MongoRepository<Feedback,String> {

    Optional<Feedback> findByProductId(Integer id);

}
