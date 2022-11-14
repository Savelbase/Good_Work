package com.rm.toolkit.feedbackcommandapplication.repository;

import com.rm.toolkit.feedbackcommandapplication.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    boolean existsByOneToOneId(String oneToOneId);
}
