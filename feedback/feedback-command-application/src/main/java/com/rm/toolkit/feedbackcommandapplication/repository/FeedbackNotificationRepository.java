package com.rm.toolkit.feedbackcommandapplication.repository;

import com.rm.toolkit.feedbackcommandapplication.model.FeedbackNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackNotificationRepository extends JpaRepository<FeedbackNotification, String> {

    Optional<FeedbackNotification> findByOneToOneId(String oneToOneId);
}
