package com.rm.toolkit.feedbackcommandapplication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rm.toolkit.feedbackcommandapplication.model.type.Versionable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "feedback_notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackNotification implements Versionable {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(name = "one_to_one_id", nullable = false)
    private String oneToOneId;

    @Column(name = "first_notification_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime firstNotificationDateTime;

    @Column(name = "repeated_notification_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime repeatedNotificationDateTime;

    @Column(name = "notification_counter")
    private Integer notificationCounter;

    @Column(name = "rd_is_notified")
    private boolean rdIsNotified;

    private Integer version;
}
