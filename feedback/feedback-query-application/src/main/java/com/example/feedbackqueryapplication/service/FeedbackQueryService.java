package com.example.feedbackqueryapplication.service;

import com.example.feedbackqueryapplication.exception.notfound.FeedbackNotFoundException;
import com.example.feedbackqueryapplication.model.Feedback;
import com.example.feedbackqueryapplication.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackQueryService {

    private static final String DATE_TIME_COMMENT_PARAMETER = "dateTime";

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public List<Feedback> getFeedbacksByUserId(String userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, DATE_TIME_COMMENT_PARAMETER));

        return feedbackRepository.getAllFeedbacksByUserIdOrderByDateTimeDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Feedback getFeedbackById(String feedbackId) {
        return feedbackRepository.findById(feedbackId).orElseThrow(() -> {
            log.error("Фидбек с id {} не найден", feedbackId);
            throw new FeedbackNotFoundException(feedbackId);
        });
    }
}