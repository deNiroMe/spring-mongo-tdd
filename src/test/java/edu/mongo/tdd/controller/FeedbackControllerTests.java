package edu.mongo.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mongo.tdd.domain.Feedback;
import edu.mongo.tdd.service.FeedbackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
public class FeedbackControllerTests {

    @MockBean
    private FeedbackService feedbackService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test get feedback by id - GET /feedback/1")
    public void testGetFeedbackById() throws Exception {

        // Prepare mock feedback
        Feedback mockfeedback = new Feedback("1",1,1,"POSTED","This is a feedback message!");

        // prepare mocked service method
        doReturn(Optional.of(mockfeedback)).when(feedbackService).findById(mockfeedback.getId());

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.get("/feedback/{id}",mockfeedback.getId()))
                // validate 200 OK and JSON response type is received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                //validate response headers
                .andExpect(header().string(HttpHeaders.ETAG,"\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION,"/feedback/1"))

                // validate response body
                .andExpect(jsonPath("$.id",is("1")))
                .andExpect(jsonPath("$.productId",is(1)))
                .andExpect(jsonPath("$.userId",is(1)))
                .andExpect(jsonPath("$.status",is("POSTED")));
    }

    @Test
    @DisplayName("Test feedback not found for a given id - GET /feedback/1")
    public void testFeedbackNotFoundById() throws Exception {
        // prepare mocked service method
        doReturn(null).when(feedbackService).findById(null);

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.get("/feedback/{id}",1))
                // validate 404 NOT_FOUND is received
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test get all feedback - GET /feedback")
    public void testGetAllFeedback() throws Exception {

        // Prepare mock feedback
        Feedback firstFeedback = new Feedback("1",1,1,"POSTED","This is a feedback message!");
        Feedback secondFeedback = new Feedback("2",1,2,"PUBLISHED","This is a feedback message!");

        // prepare mocked service methods
        doReturn(Arrays.asList(firstFeedback,secondFeedback)).when(feedbackService).findAll();

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.get("/feedback"))
                // validate 200 OK and JSON response type is received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                // validate response body
                .andExpect(jsonPath("$[0].id",is("1")))
                .andExpect(jsonPath("$[1].id",is("2")))
                .andExpect(jsonPath("$[0].status",is("POSTED")))
                .andExpect(jsonPath("$[1].status",is("PUBLISHED")));
    }

    @Test
    @DisplayName("Test save a new feedback - POST /feedback")
    public void testSaveANewFeedback() throws Exception {

        // Prepare mock feedback
        Feedback newFeedback = new Feedback("1",1,1,"POSTED","This is a feedback message!");
        Feedback mockedFeedback = new Feedback("1",1,1,"POSTED",1,"This is a feedback message!");

        // prepare mocked service methods
        doReturn(mockedFeedback).when(feedbackService).save(ArgumentMatchers.any());

        // perform GET Request
        mockMvc.perform(MockMvcRequestBuilders.post("/feedback")
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(new ObjectMapper().writeValueAsString(newFeedback))
                )
                // validate 201 CREATED and JSON response type is received
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                //validate response headers
                .andExpect(header().string(HttpHeaders.ETAG,"\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION,"/feedback/1"))

                // validate response body
                .andExpect(jsonPath("$.id",is("1")))
                .andExpect(jsonPath("$.productId",is(1)))
                .andExpect(jsonPath("$.version",is(1)))
                .andExpect(jsonPath("$.userId",is(1)))
                .andExpect(jsonPath("$.status",is("POSTED")));
    }


}
