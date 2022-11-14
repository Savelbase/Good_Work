package com.rm.toolkit.feedbackcommandapplication.service;

import com.rm.toolkit.feedbackcommandapplication.command.CommandType;
import com.rm.toolkit.feedbackcommandapplication.command.EmailCommand;
import com.rm.toolkit.feedbackcommandapplication.command.EmailCommandPayload;
import com.rm.toolkit.feedbackcommandapplication.message.CommandPublisher;
import com.rm.toolkit.feedbackcommandapplication.model.*;
import com.rm.toolkit.feedbackcommandapplication.repository.*;
import com.rm.toolkit.feedbackcommandapplication.util.CommandUtil;
import com.rm.toolkit.feedbackcommandapplication.util.DepartmentUtil;
import com.rm.toolkit.feedbackcommandapplication.util.FeedbackNotificationUtil;
import com.rm.toolkit.feedbackcommandapplication.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackNotificationCommandService {

    private static final Integer SINGLE_RECORD = 0;
    private static final Integer DAYS_FOR_REPEAT_NOTIFICATION_TO_PM = 2;
    private static final Integer WEEKS_FOR_REPEAT_NOTIFICATION_TO_RD = 1;
    private static final Integer MAX_NOTIFICATION_COUNTER = 3;

    private final OneToOneRepository oneToOneRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackIntervalRepository intervalRepository;
    private final FeedbackNotificationRepository notificationRepository;
    private final FeedbackNotificationUtil notificationUtil;
    private final CommandUtil commandUtil;
    private final CommandPublisher commandPublisher;
    private final UserUtil userUtil;
    private final DepartmentUtil departmentUtil;

    /**
     * раз в день проверяем не забыл ли PM оставить фидбек, если 121 встреча завершена
     */
    @Scheduled(fixedDelay = 86_400_000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void notifyIfFeedbackIsMissedForCompletedOneToOne() {
        List<OneToOne> allCompletedOneToOne = oneToOneRepository.findAllCompletedOneToOne();

        for (OneToOne completedOneToOne : allCompletedOneToOne) {
            String completedOneToOneId = completedOneToOne.getId();
            boolean feedbackExist = feedbackRepository.existsByOneToOneId(completedOneToOneId);
            if (!feedbackExist) {
                FeedbackInterval feedbackInterval = intervalRepository.findAll().get(SINGLE_RECORD);
                Integer interval = feedbackInterval.getInterval();
                boolean timeIsExpired = completedOneToOne.getDateTime().plusDays(interval).isBefore(ZonedDateTime.now());
                if (timeIsExpired) {
                    Optional<FeedbackNotification> feedbackNotification = notificationRepository
                            .findByOneToOneId(completedOneToOneId);

                    if(feedbackNotification.isEmpty()) {
                        firstNotificationToPM(completedOneToOneId, completedOneToOne);
                    } else {
                        FeedbackNotification notification = feedbackNotification.get();

                        repeatNotificationToPM(notification, completedOneToOne);

                        repeatNotificationToRD(notification, completedOneToOne);
                    }
                }
            }
        }
    }

    private void firstNotificationToPM(String completedOneToOneId, OneToOne oneToOne) {
        FeedbackNotification newNotification = notificationUtil.createNotificationWithoutDateTime(completedOneToOneId);
        Integer notificationCounter = newNotification.getNotificationCounter();

        if (notificationCounter < MAX_NOTIFICATION_COUNTER) {
            sendFirstNotification(oneToOne);

            newNotification.setNotificationCounter(notificationCounter + 1);
            newNotification.setFirstNotificationDateTime(ZonedDateTime.now());
            newNotification.setRepeatedNotificationDateTime(ZonedDateTime.now());
            notificationRepository.save(newNotification);
        }
    }

    private void repeatNotificationToPM(FeedbackNotification notification, OneToOne oneToOne) {
        boolean timeToRepeatNotification = notification.getRepeatedNotificationDateTime()
                .plusDays(DAYS_FOR_REPEAT_NOTIFICATION_TO_PM).isBefore(ZonedDateTime.now());

        if (timeToRepeatNotification) {
            Integer notificationCounter = notification.getNotificationCounter();

            if (notificationCounter < MAX_NOTIFICATION_COUNTER) {
                sendRepeatedNotification(oneToOne);

                notification.setNotificationCounter(notificationCounter + 1);
                notification.setRepeatedNotificationDateTime(ZonedDateTime.now());
                notificationRepository.save(notification);
            }
        }
    }

    private void repeatNotificationToRD(FeedbackNotification notification, OneToOne oneToOne) {
        boolean timeToNotifyRD = notification.getFirstNotificationDateTime()
                .plusWeeks(WEEKS_FOR_REPEAT_NOTIFICATION_TO_RD).isBefore(ZonedDateTime.now());

        if (timeToNotifyRD) {
            boolean rdIsNotified = notification.isRdIsNotified();
            if (!rdIsNotified) {
                sendNotificationToRD(oneToOne);

                notification.setRdIsNotified(true);
                notificationRepository.save(notification);
            }
        }
    }

    private void sendFirstNotification(OneToOne oneToOne) {
        User subordinate = userUtil.findUserIfExists(oneToOne.getUserId());
        User resourceManger = userUtil.findUserIfExists(oneToOne.getResourceManagerId());

        EmailCommandPayload emailCommandPayload = commandUtil.createEmailCommandPayload(subordinate, resourceManger,
                oneToOne, CommandType.NO_FEEDBACK_PROVIDED);

        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setType(CommandType.NO_FEEDBACK_PROVIDED);
        commandUtil.populateCommandFields(emailCommand, emailCommandPayload);
        commandPublisher.publish(emailCommand);
    }

    private void sendRepeatedNotification(OneToOne oneToOne) {
        User subordinate = userUtil.findUserIfExists(oneToOne.getUserId());
        User resourceManger = userUtil.findUserIfExists(oneToOne.getResourceManagerId());

        EmailCommandPayload emailCommandPayload = commandUtil.createEmailCommandPayload(subordinate, resourceManger,
                oneToOne, CommandType.REPEATED_NO_FEEDBACK_PROVIDED);

        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setType(CommandType.REPEATED_NO_FEEDBACK_PROVIDED);
        commandUtil.populateCommandFields(emailCommand, emailCommandPayload);
        commandPublisher.publish(emailCommand);
    }

    private void sendNotificationToRD(OneToOne oneToOne) {
        User resourceManager = userUtil.findUserIfExists(oneToOne.getResourceManagerId());

        Department department = departmentUtil.findDepartmentIfExists(resourceManager.getDepartmentId());

        User resourceDirector = userUtil.findUserIfExists(department.getHeadId());

        EmailCommandPayload emailCommandPayload = commandUtil.createEmailCommandPayload(resourceManager, resourceDirector,
                oneToOne, CommandType.REPEATED_NO_FEEDBACK_PROVIDED);

        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setType(CommandType.REPEATED_NO_FEEDBACK_PROVIDED);
        commandUtil.populateCommandFields(emailCommand, emailCommandPayload);
        commandPublisher.publish(emailCommand);
    }
}
