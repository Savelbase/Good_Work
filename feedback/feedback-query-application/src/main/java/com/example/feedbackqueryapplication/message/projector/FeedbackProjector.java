package com.example.feedbackqueryapplication.message.projector;

import com.example.feedbackqueryapplication.event.feedback.FeedbackCreatedEvent;
import com.example.feedbackqueryapplication.event.feedback.FeedbackEditedEvent;
import com.example.feedbackqueryapplication.model.Feedback;
import com.example.feedbackqueryapplication.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class FeedbackProjector {

    private static final Integer VERSION_VALUE = 1;

    private final ProjectionUtil projectionUtil;

    public Feedback project(FeedbackCreatedEvent event){
        var payload = event.getPayload();

        return Feedback.builder()
                .id(event.getEntityId())
                .dateTime(ZonedDateTime.now())
                .userId(payload.getUserId())
                .resourceManagerId(payload.getResourceManagerId())
                .overAllAssessment(payload.getOverAllAssessment())
                .professionalSkills(payload.getProfessionalSkills())
                .workQuality(payload.getWorkQuality())
                .criticalThinking(payload.getCriticalThinking())
                .reliability(payload.getReliability())
                .communicationSkills(payload.getCommunicationSkills())
                .development(payload.getDevelopment())
                .project(payload.getProject())
                .goals(payload.getGoals())
                .department(payload.getDepartment())
                .activities(payload.getActivities())
                .additionally(payload.getAdditionally())
                .version(VERSION_VALUE)
                .build();
    }

    public void project(FeedbackEditedEvent event, Feedback feedback) {
        projectionUtil.validateEvent(event, feedback.getId(), feedback.getVersion());

        var payload = event.getPayload();

        feedback.setOverAllAssessment(payload.getOverAllAssessment());
        feedback.setProfessionalSkills(payload.getProfessionalSkills());
        feedback.setWorkQuality(payload.getWorkQuality());
        feedback.setCriticalThinking(payload.getCriticalThinking());
        feedback.setReliability(payload.getReliability());
        feedback.setCommunicationSkills(payload.getCommunicationSkills());
        feedback.setDevelopment(payload.getDevelopment());
        feedback.setDepartment(payload.getDepartment());
        feedback.setGoals(payload.getGoals());
        feedback.setProject(payload.getProject());
        feedback.setActivities(payload.getActivities());
        feedback.setAdditionally(payload.getAdditionally());
    }
}
