package com.example.feedbackqueryapplication.message;

import com.example.feedbackqueryapplication.event.Event;
import com.example.feedbackqueryapplication.event.EventPayload;
import com.example.feedbackqueryapplication.event.feedback.FeedbackCreatedEvent;
import com.example.feedbackqueryapplication.event.feedback.FeedbackEditedEvent;
import com.example.feedbackqueryapplication.event.user.UserCreatedEvent;
import com.example.feedbackqueryapplication.event.user.UserEditedEvent;
import com.example.feedbackqueryapplication.exception.notfound.FeedbackNotFoundException;
import com.example.feedbackqueryapplication.exception.notfound.UserNotFoundException;
import com.example.feedbackqueryapplication.message.projector.FeedbackProjector;
import com.example.feedbackqueryapplication.message.projector.UserProjector;
import com.example.feedbackqueryapplication.model.Feedback;
import com.example.feedbackqueryapplication.model.User;
import com.example.feedbackqueryapplication.repository.FeedbackRepository;
import com.example.feedbackqueryapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackProjector feedbackProjector;
    private final UserProjector userProjector;
    private final UserRepository userRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(FeedbackCreatedEvent event) {
        Feedback feedback = feedbackProjector.project(event);
        feedbackRepository.save(feedback);

        log.info("Фидбек с id {} создан после получения события", feedback.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(FeedbackEditedEvent event) {
        Feedback feedback = getFeedbackFromEvent(event);

        feedbackProjector.project(event, feedback);
        feedbackRepository.save(feedback);

        log.info("Фидбек с id {} изменен после получения события", feedback.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserCreatedEvent event) {
        User user = userProjector.project(event);
        userRepository.save(user);

        log.info("Пользователь с id {} создан после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserEditedEvent event) {
        User user = getUserFromEvent(event);

        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} отредактирован после получения события", user.getId());
    }

    private Feedback getFeedbackFromEvent(Event<? extends EventPayload> event) {
        String feedbackId = event.getEntityId();
        return feedbackRepository.findById(feedbackId).orElseThrow(() -> {
            throw new FeedbackNotFoundException(feedbackId);
        });
    }

    private User getUserFromEvent(Event<? extends EventPayload> event) {
        String userId = event.getEntityId();
        return userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь с id {} не существует", userId);
            throw new UserNotFoundException(userId);
        });
    }
}
