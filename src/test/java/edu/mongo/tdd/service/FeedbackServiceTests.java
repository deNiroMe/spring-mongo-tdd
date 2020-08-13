package edu.mongo.tdd.service;


import edu.mongo.tdd.domain.Feedback;
import edu.mongo.tdd.repository.FeedbackRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ExtendWith({SpringExtension.class})
public class FeedbackServiceTests {

    @Autowired
    private FeedbackService feedbackService;

    @MockBean
    private FeedbackRepository feedbackRepository;

    @Test
    @DisplayName("Test find feedback by id successfully")
    public void testFindFeedbackById() throws Exception {

        // Prepare mock feedback
        Feedback mockFeedback = new Feedback("1", 1, 1, "POSTED", "This is a feedback message!");

        // prepare mocked repository method
        doReturn(Optional.of(mockFeedback)).when(feedbackRepository).findById(mockFeedback.getId());

        //when
        Optional<Feedback> foundFeedback = feedbackService.findById("1");

        //then
        Assertions.assertTrue(foundFeedback.isPresent());
        Assertions.assertNotEquals(Optional.empty(),foundFeedback);
        foundFeedback.ifPresent(feedback -> {
            Assertions.assertEquals(1,feedback.getProductId().intValue());
            Assertions.assertEquals("POSTED",feedback.getStatus());
        });
    }

    @Test
    @DisplayName("Test find feedback by product id successfully")
    public void testFindFeedbackByProductId() throws Exception {

        // Prepare mock feedback
        Feedback mockFeedback = new Feedback("1", 1, 1, "POSTED", "This is a feedback message!");

        // prepare mocked repository method
        doReturn(Optional.of(mockFeedback)).when(feedbackRepository).findByProductId(1);

        //when
        Optional<Feedback> foundFeedback = feedbackService.findByProductId(1);

        //then
        Assertions.assertTrue(foundFeedback.isPresent());
        Assertions.assertNotEquals(Optional.empty(),foundFeedback);
        foundFeedback.ifPresent(feedback -> {
            Assertions.assertEquals("1",feedback.getId());
            Assertions.assertEquals("POSTED",feedback.getStatus());
            Assertions.assertEquals(1,feedback.getUserId().intValue());
        });
    }

    @Test
    @DisplayName("Test find feedback by id failure")
    public void testFindFeedbackByIdFailure() throws Exception {

        // prepare mocked repository method
        doReturn(Optional.empty()).when(feedbackRepository).findByProductId(1);

        //when
        Optional<Feedback> foundFeedback = feedbackService.findByProductId(1);

        //then
        Assertions.assertFalse(foundFeedback.isPresent());
        Assertions.assertEquals(Optional.empty(),foundFeedback);
    }

    @Test
    @DisplayName("Test find List of feedback - GET /feedback")
    public void testFindAllFeedback() throws Exception {

        // Prepare mock feedback
        Feedback mockFeedback1 = new Feedback("1", 1, 1, "POSTED", "This is a feedback message!");
        Feedback mockFeedback2 = new Feedback("1", 1, 1, "POSTED", "This is a feedback message!");

        // prepare mocked service methods
        doReturn(Arrays.asList(mockFeedback1,mockFeedback2)).when(feedbackRepository).findAll();

        // when
        List<Feedback> feedbackList = feedbackService.findAll();

        //then
        Assertions.assertNotNull(feedbackList,"list should not be null");
        Assertions.assertEquals(2,feedbackList.size());
    }

    @Test
    @DisplayName("Test save new - Post /feedback")
    public void testSaveNewFeedback() throws Exception {

        // Prepare mock feedback
        Feedback feedbackToSave = new Feedback("1", 1, 1, "POSTED", "This is a feedback message!");
        Feedback feedbackToReturn = new Feedback("1", 1, 1, "POSTED", "This is a feedback message!");

        // prepare mocked service method
        doReturn(feedbackToReturn).when(feedbackRepository).save(feedbackToSave);

        // when
        Feedback savedFeedback = feedbackService.save(feedbackToSave);

        //then
        Assertions.assertNotNull(savedFeedback,"feedback should not be null");
        Assertions.assertEquals(1,savedFeedback.getVersion().intValue());
    }
    
}
