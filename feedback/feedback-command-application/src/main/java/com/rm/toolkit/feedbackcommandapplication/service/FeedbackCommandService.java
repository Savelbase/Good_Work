package com.rm.toolkit.feedbackcommandapplication.service;

import com.rm.toolkit.feedbackcommandapplication.dto.command.CreateFeedbackCommand;
import com.rm.toolkit.feedbackcommandapplication.dto.command.EditFeedbackCommand;
import com.rm.toolkit.feedbackcommandapplication.event.feedback.FeedbackCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.feedback.FeedbackEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.FeedbackNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.message.EventPublisher;
import com.rm.toolkit.feedbackcommandapplication.message.projector.FeedbackProjector;
import com.rm.toolkit.feedbackcommandapplication.model.Feedback;
import com.rm.toolkit.feedbackcommandapplication.model.OneToOne;
import com.rm.toolkit.feedbackcommandapplication.repository.FeedbackRepository;
import com.rm.toolkit.feedbackcommandapplication.repository.OneToOneRepository;
import com.rm.toolkit.feedbackcommandapplication.util.EventUtil;
import com.rm.toolkit.feedbackcommandapplication.util.OneToOneUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackCommandService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackProjector feedbackProjector;
    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;
    private final OneToOneUtil oneToOneUtil;

    @Transactional
    public void addNewFeedback(CreateFeedbackCommand command, String authorId) {
        oneToOneUtil.getCompletedOneToOne(command.getOneToOneId());

        FeedbackCreatedEvent.Payload payload = FeedbackCreatedEvent.Payload.builder()
                .dateTime(ZonedDateTime.now())
                .userId(command.getUserId())
                .resourceManagerId(command.getResourceManagerId())
                .overAllAssessment(command.getOverAllAssessment())
                .professionalSkills(command.getProfessionalSkills())
                .workQuality(command.getWorkQuality())
                .criticalThinking(command.getCriticalThinking())
                .reliability(command.getReliability())
                .communicationSkills(command.getCommunicationSkills())
                .development(command.getDevelopment())
                .project(command.getProject())
                .goals(command.getGoals())
                .department(command.getDepartment())
                .activities(command.getActivities())
                .additionally(command.getAdditionally())
                .oneToOneId(command.getOneToOneId())
                .build();

        FeedbackCreatedEvent event = new FeedbackCreatedEvent();
        event.setPayload(payload);
        eventUtil.populateEventFields(event, UUID.randomUUID().toString(), 1, authorId, payload, true);

        Feedback feedback = feedbackProjector.project(event);
        feedbackRepository.save(feedback);

        eventPublisher.publishNoReupload(event);
    }

    @Transactional
    public void editFeedback(EditFeedbackCommand command, String feedbackId) {
        Feedback feedback = getFeedbackById(feedbackId);

        String feedbackEditedEventId = UUID.randomUUID().toString();
        FeedbackEditedEvent.Payload payload = FeedbackEditedEvent.Payload.builder()
                .dateTime(ZonedDateTime.now())
                .overAllAssessment(command.getOverAllAssessment())
                .professionalSkills(command.getProfessionalSkills())
                .workQuality(command.getWorkQuality())
                .criticalThinking(command.getCriticalThinking())
                .reliability(command.getReliability())
                .communicationSkills(command.getCommunicationSkills())
                .development(command.getDevelopment())
                .project(command.getProject())
                .goals(command.getGoals())
                .department(command.getDepartment())
                .activities(command.getActivities())
                .additionally(command.getAdditionally())
                .build();

        FeedbackEditedEvent event = new FeedbackEditedEvent();
        event.setId(feedbackEditedEventId);
        eventUtil.populateEventFields(event, command.getId(), feedback.getVersion() + 1, feedbackId, payload);

        feedbackProjector.project(event, feedback);
        feedbackRepository.save(feedback);

        eventPublisher.publishWithReupload(event);
    }

    @Transactional
    public Feedback getFeedbackById(String feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> {
                    log.error("фидбек с id = {} не найден", feedbackId);
                    throw new FeedbackNotFoundException(feedbackId);
                });
    }
}
