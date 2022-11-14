package com.rm.toolkit.feedbackcommandapplication.service;

import com.rm.toolkit.feedbackcommandapplication.model.FeedbackInterval;
import com.rm.toolkit.feedbackcommandapplication.repository.FeedbackIntervalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackIntervalCommandService {

    private static final Integer VERSION_VALUE = 1;

    private final FeedbackIntervalRepository intervalRepository;

    @Transactional
    public void setIntervalForFeedback(Integer interval) {
        intervalRepository.deleteAll();

        FeedbackInterval feedbackInterval = FeedbackInterval.builder()
                .id(UUID.randomUUID().toString())
                .interval(interval)
                .version(VERSION_VALUE)
                .build();

        intervalRepository.save(feedbackInterval);
    }
}
