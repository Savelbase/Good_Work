package com.example.feedbackqueryapplication.repository;

import com.example.feedbackqueryapplication.model.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    @Query("Select f from Feedback f where f.userId=:userId")
    List<Feedback> getAllFeedbacksByUserIdOrderByDateTimeDesc(@Param("userId") String userId, Pageable pageable);
}