package edu.mongo.tdd.repository;

import edu.mongo.tdd.domain.Feedback;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.List;
import java.util.Optional;

@DataMongoTest
@ExtendWith({MongoSpringExtension.class})
public class FeedbackRepositoryTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FeedbackRepository feedbackRepository;

    // used by the MongoTestDataFile annotation
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @AfterEach
    public void cleanup() {
        // cleanup database after each test
        mongoTemplate.dropCollection("feedback");
    }

    @Test
    @DisplayName("Test find feedback by id")
    @MongoTestDataFile(value = "data.json",classType = Feedback.class,collectionName = "Feedback")
    public void testFindFeedbackById() {

        // when
        Optional<Feedback> feedback = feedbackRepository.findById("1");

        // then
        Assertions.assertTrue(feedback.isPresent(),"a feedback with id 1 should exist");
        feedback.ifPresent( f -> {
            Assertions.assertEquals("1",f.getId());
            Assertions.assertEquals(1,f.getUserId().intValue());
            Assertions.assertEquals(1,f.getProductId().intValue());
            Assertions.assertEquals("POSTED",f.getStatus());
        });
    }

    @Test
    @DisplayName("Test find all feedback")
    @MongoTestDataFile(value = "data.json",classType = Feedback.class,collectionName = "Feedback")
    public void testFindAllFeedback() {
        // when
        List<Feedback> feedbackList = feedbackRepository.findAll();

        // then
        Assertions.assertEquals(3,feedbackList.size(),"a feedback list of size 3 should be returned");
    }

    @Test
    @DisplayName("Test find feedback by product id")
    @MongoTestDataFile(value = "data.json",classType = Feedback.class,collectionName = "Feedback")
    public void testFindFeedbackByProductId() {
        // when
        Optional<Feedback> feedback = feedbackRepository.findByProductId(1);

        // then
        Assertions.assertTrue(feedback.isPresent(),"feedback for product 2 should exist");
    }

    @Test
    @DisplayName("Test find feedback by product id Failure")
    @MongoTestDataFile(value = "data.json",classType = Feedback.class,collectionName = "Feedback")
    public void testFindFeedbackByProductIdFailure() {
        // when
        Optional<Feedback> feedback = feedbackRepository.findByProductId(8);

        // then
        Assertions.assertFalse(feedback.isPresent(),"feedback for product 8 should not exist");
    }

    @Test
    @DisplayName("Test save new feedback")
    @MongoTestDataFile(value = "data.json",classType = Feedback.class,collectionName = "Feedback")
    public void testSavingNewFeedback() {

        // Prepare mock feedback
        Feedback newFeedback = new Feedback("4", 1, 1, "POSTED", "This is a feedback message!");

        // when
        Feedback savedFeedback = feedbackRepository.save(newFeedback);

        // then
        Assertions.assertEquals(1,savedFeedback.getVersion().intValue(),"a new feedback with version 1 should be created");
    }

    @Test
    @DisplayName("Test delete a feedback")
    @MongoTestDataFile(value = "data.json",classType = Feedback.class,collectionName = "Feedback")
    public void testDeletingFeedback() {

        // Prepare mock feedback
        Feedback existingFeedback = new Feedback("1", 1, 1, "POSTED",1, "This is a feedback message!");

        // when
        feedbackRepository.delete(existingFeedback);

        // then
        Assertions.assertEquals(2,feedbackRepository.count());
    }

}
