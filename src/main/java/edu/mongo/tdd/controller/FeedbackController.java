package edu.mongo.tdd.controller;

import edu.mongo.tdd.domain.Feedback;
import edu.mongo.tdd.service.FeedbackService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class FeedbackController {

    private FeedbackService feedbackService;

    @GetMapping("/feedback/{id}")
    private ResponseEntity<?> getFeedBack(@PathVariable String id) {

        log.debug("Searching for feedback with id : {}", id);

        return feedbackService.findById(id)
                .map( feedback -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(feedback.getVersion()))
                                .location(new URI("/feedback/"+id))
                                .body(feedback);
                    } catch (URISyntaxException e){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/feedback")
    private Iterable<Feedback> getAllFeedBackForProduct(@RequestParam(value = "productId",required = false) Optional<String> productId) {

        return productId.map( id -> feedbackService.findByProductId(Integer.valueOf(id))
                                        .map(Arrays::asList)
                                        .orElseGet(ArrayList::new))
                        .orElse(feedbackService.findAll());
    }

    @PostMapping("/feedback")
    private ResponseEntity<?> createFeedBack(@RequestBody Feedback feedback) {

        log.debug("Adding new feedback for product with id : {}", feedback.getProductId());

        feedback = feedbackService.save(feedback);

        try {
            return ResponseEntity
                    .created(new URI("/feedback/"+feedback.getId()))
                    .eTag(Integer.toString(feedback.getVersion()))
                    .body(feedback);
        } catch (URISyntaxException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
