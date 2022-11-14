package com.rm.toolkit.feedbackcommandapplication.event.feedback;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("FEEDBACK_CREATED")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FeedbackCreatedEvent extends Event<FeedbackCreatedEvent.Payload> {

    public FeedbackCreatedEvent() {
        this.type = EventType.FEEDBACK_CREATED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
    public static class Payload extends EventPayload {

        private ZonedDateTime dateTime;

        private String userId;

        private String resourceManagerId;

        private int overAllAssessment;

        private int professionalSkills;

        private int workQuality;

        private int criticalThinking;

        private int reliability;

        private int communicationSkills;

        private String development;

        private String project;

        private String goals;

        private String department;

        private String activities;

        private String additionally;

        private String oneToOneId;
    }
}
