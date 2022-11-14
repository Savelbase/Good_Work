package com.rm.toolkit.oneToOne.command.service;

import com.rm.toolkit.oneToOne.command.command.CommandType;
import com.rm.toolkit.oneToOne.command.command.EmailCommand;
import com.rm.toolkit.oneToOne.command.command.EmailCommandPayload;
import com.rm.toolkit.oneToOne.command.dto.request.CreateOneToOneRequest;
import com.rm.toolkit.oneToOne.command.dto.request.UpdateOneToOneRequest;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.oneToOne.command.exception.OneToOneNotFoundException;
import com.rm.toolkit.oneToOne.command.exception.UserNotYourDisposalException;
import com.rm.toolkit.oneToOne.command.message.CommandPublisher;
import com.rm.toolkit.oneToOne.command.message.EventPublisher;
import com.rm.toolkit.oneToOne.command.message.projector.OneToOneProjector;
import com.rm.toolkit.oneToOne.command.model.OneToOne;
import com.rm.toolkit.oneToOne.command.model.User;
import com.rm.toolkit.oneToOne.command.repository.OneToOneRepository;
import com.rm.toolkit.oneToOne.command.util.CommandUtil;
import com.rm.toolkit.oneToOne.command.util.EventUtil;
import com.rm.toolkit.oneToOne.command.util.OneToOneUtil;
import com.rm.toolkit.oneToOne.command.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OneToOneCommandService {

    private final OneToOneRepository oneToOneRepository;
    private final OneToOneUtil oneToOneUtil;
    private final EventUtil eventUtil;
    private final UserUtil userUtil;
    private final CommandUtil commandUtil;
    private final EventPublisher eventPublisher;
    private final CommandPublisher commandPublisher;
    private final OneToOneProjector oneToOneProjector;

    @Transactional
    public String createOneToOne(CreateOneToOneRequest request, String authorId) {

        String comment = request.getComment() == null ? "" : request.getComment().trim();

        oneToOneUtil.checkIfCommentWithinLimit(comment);
        oneToOneUtil.checkTimeNotPassed(request.getDateTime());
        userUtil.checkIfUserExistInPool(request.getUserId(), authorId);

        OneToOneCreatedEvent.Payload payload = OneToOneCreatedEvent.Payload.builder()
                .userId(request.getUserId())
                .resourceManagerId(authorId)
                .dateTime(request.getDateTime())
                .comment(comment)
                .isOver(false)
                .isDeleted(false)
                .build();

        OneToOneCreatedEvent oneToOneCreatedEvent = new OneToOneCreatedEvent();
        oneToOneCreatedEvent.setPayload(payload);
        eventUtil.populateEventFields(oneToOneCreatedEvent, UUID.randomUUID().toString(), 1, authorId, payload, true);

        eventPublisher.publishNoReupload(oneToOneCreatedEvent);

        OneToOne oneToOne = oneToOneProjector.project(oneToOneCreatedEvent);
        oneToOneRepository.save(oneToOne);

        User subordinate = userUtil.findUserIfExists(oneToOne.getUserId());
        User resourceManger = userUtil.findUserIfExists(oneToOne.getResourceManagerId());

        EmailCommandPayload emailCommandPayload = commandUtil.createEmailCommandPayload(subordinate,
                resourceManger, oneToOne, CommandType.CREATED_ONE_TO_ONE);

        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setType(CommandType.CREATED_ONE_TO_ONE);
        commandUtil.populateCommandFields(emailCommand, authorId, emailCommandPayload);
        commandPublisher.publish(emailCommand);

        return oneToOne.getId();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void completeOneToOne(String oneToOneId, String authorId) {

        OneToOne oneToOne = oneToOneRepository.findById(oneToOneId).orElseThrow(() -> new OneToOneNotFoundException(oneToOneId));

        if (!oneToOne.getResourceManagerId().equals(authorId)) {
            throw new UserNotYourDisposalException(oneToOneId);
        }


        oneToOneUtil.checkBeforeAppointedTime(oneToOne.getDateTime(), oneToOneId);


        String oneToOneCompletedEventId = UUID.randomUUID().toString();

        OneToOneCompletedEvent oneToOneCompletedEvent = new OneToOneCompletedEvent();
        OneToOneCompletedEvent.Payload payload = OneToOneCompletedEvent.Payload.builder().isOver(true).build();
        oneToOneCompletedEvent.setId(oneToOneCompletedEventId);
        eventUtil.populateEventFields(oneToOneCompletedEvent, oneToOne.getId(), oneToOne.getVersion() + 1,
                authorId, payload, false, oneToOneCompletedEventId);

        oneToOneProjector.project(oneToOneCompletedEvent, oneToOne);
        oneToOneRepository.save(oneToOne);

        eventPublisher.publish(oneToOneCompletedEvent);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateOneToOne(String oneToOneId, UpdateOneToOneRequest request, String authorId) {
        oneToOneUtil.checkTimeNotPassed(request.getDateTime());
        String comment = request.getComment() == null ? "" : request.getComment().trim();
        oneToOneUtil.checkIfCommentWithinLimit(comment);
        OneToOne oneToOne = oneToOneUtil.findByIdOrThrowException(oneToOneId);
        oneToOneUtil.checkTimeNotPassed(oneToOne.getDateTime());

        String oneToOneUpdatedEventId = UUID.randomUUID().toString();

        OneToOneUpdatedEvent.Payload payload = OneToOneUpdatedEvent.Payload.builder()
                .userId(oneToOne.getUserId())
                .resourceManagerId(oneToOne.getResourceManagerId())
                .dateTime(request.getDateTime())
                .comment(comment)
                .isOver(false)
                .build();

        OneToOneUpdatedEvent oneToOneUpdatedEvent = new OneToOneUpdatedEvent();
        oneToOneUpdatedEvent.setId(oneToOneUpdatedEventId);
        eventUtil.populateEventFields(oneToOneUpdatedEvent, oneToOne.getId(), oneToOne.getVersion() + 1, authorId, payload, false);

        oneToOneProjector.project(oneToOneUpdatedEvent, oneToOne);
        oneToOneRepository.save(oneToOne);

        eventPublisher.publish(oneToOneUpdatedEvent);

        User subordinate = userUtil.findUserIfExists(oneToOne.getUserId());
        User resourceManger = userUtil.findUserIfExists(oneToOne.getResourceManagerId());

        EmailCommandPayload emailCommandPayload = commandUtil.createEmailCommandPayload(subordinate,
                resourceManger, oneToOne, CommandType.UPDATED_ONE_TO_ONE);

        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setType(CommandType.UPDATED_ONE_TO_ONE);
        commandUtil.populateCommandFields(emailCommand, authorId, emailCommandPayload);
        commandPublisher.publish(emailCommand);

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteOneToOne(String oneToOneId, String authorId) {
        OneToOne oneToOne = oneToOneUtil.findByIdOrThrowException(oneToOneId);

        String oneToOneDeletedEventId = UUID.randomUUID().toString();

        OneToOneDeletedEvent oneToOneDeletedEvent = new OneToOneDeletedEvent();
        new OneToOneDeletedEvent.Payload();
        OneToOneDeletedEvent.Payload payload = OneToOneDeletedEvent.Payload.builder().isDeleted(true).build();
        oneToOneDeletedEvent.setId(oneToOneDeletedEventId);
        eventUtil.populateEventFields(oneToOneDeletedEvent, oneToOneId, oneToOne.getVersion() + 1, authorId, payload,
                false);

        oneToOneProjector.project(oneToOneDeletedEvent, oneToOne);
        oneToOneRepository.save(oneToOne);

        eventPublisher.publish(oneToOneDeletedEvent);

        User subordinate = userUtil.findUserIfExists(oneToOne.getUserId());
        User resourceManger = userUtil.findUserIfExists(oneToOne.getResourceManagerId());

        EmailCommandPayload emailCommandPayload = commandUtil.createEmailCommandPayload(subordinate,
                resourceManger, oneToOne, CommandType.DELETED_ONE_TO_ONE);

        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setType(CommandType.DELETED_ONE_TO_ONE);
        commandUtil.populateCommandFields(emailCommand, authorId, emailCommandPayload);
        commandPublisher.publish(emailCommand);
    }
}

