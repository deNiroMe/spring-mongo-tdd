package edu.mongo.tdd.service;

import edu.mongo.tdd.domain.Feedback;
import edu.mongo.tdd.repository.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackService {

    private FeedbackRepository feedbackRepository;

    public Optional<Feedback> findById(String id) { return feedbackRepository.findById(id); }

    public Optional<Feedback> findByProductId(Integer id) { return feedbackRepository.findByProductId(id); }

    public List<Feedback> findAll() { return feedbackRepository.findAll(); }

    public void delete(String id) { feedbackRepository.deleteById(id); }

    public Feedback save(Feedback feedback) {
        feedback.setVersion(1);
        return feedbackRepository.save(feedback);
    }
}
