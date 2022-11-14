package com.rm.toolkit.oneToOne.command.service;

import com.rm.toolkit.oneToOne.command.dto.request.CreateOneToOneRequest;
import com.rm.toolkit.oneToOne.command.dto.request.UpdateOneToOneRequest;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.oneToOne.command.message.CommandPublisher;
import com.rm.toolkit.oneToOne.command.message.EventPublisher;
import com.rm.toolkit.oneToOne.command.message.projector.OneToOneProjector;
import com.rm.toolkit.oneToOne.command.model.OneToOne;
import com.rm.toolkit.oneToOne.command.model.User;
import com.rm.toolkit.oneToOne.command.repository.OneToOneRepository;
import com.rm.toolkit.oneToOne.command.repository.UserRepository;
import com.rm.toolkit.oneToOne.command.util.CommandUtil;
import com.rm.toolkit.oneToOne.command.util.EventUtil;
import com.rm.toolkit.oneToOne.command.util.OneToOneUtil;
import com.rm.toolkit.oneToOne.command.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MockBeans({@MockBean(OneToOneUtil.class),
            @MockBean(EventUtil.class),
            @MockBean(CommandUtil.class),
            @MockBean(UserUtil.class),
            @MockBean(EventPublisher.class),
            @MockBean(CommandPublisher.class),
            @MockBean(OneToOneProjector.class)
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DataJpaTest
@ContextConfiguration(classes = OneToOneCommandServiceTestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OneToOneCommandServiceTest {
    private final OneToOneRepository oneToOneRepository;
    private final UserRepository userRepository;
    private final OneToOneUtil oneToOneUtil;
    private final EventUtil eventUtil;
    private final UserUtil userUtil;
    private final CommandUtil commandUtil;
    private final EventPublisher eventPublisher;
    private final CommandPublisher commandPublisher;
    private final OneToOneProjector oneToOneProjector;

    private final OneToOneCommandService oneToOneCommandService;

    private final UpdateOneToOneRequest updateOneToOneRequest = new UpdateOneToOneRequest();

    private final static Long DAYS_TO_121 = 7L;
    private final String authorId = "authorIdForTest";
    private final String userId = "userIdForTest";
    private final String entityId = "oneToOneIdForTest";
    private final String comment = "Comment for one to one";
    private final ZonedDateTime dateTimeToOneToOne = ZonedDateTime.now().plusDays(DAYS_TO_121);

    private final Supplier<CreateOneToOneRequest> oneToOneRequestSupplier = () -> {
        CreateOneToOneRequest oneToOneRequest = new CreateOneToOneRequest();
        oneToOneRequest.setUserId(userId);
        oneToOneRequest.setComment(comment);
        oneToOneRequest.setDateTime(dateTimeToOneToOne);
        return oneToOneRequest;
    };

    private final Supplier<OneToOne> oneToOneSupplier = () -> {
        OneToOne oneToOne = new OneToOne();
        oneToOne.setId(entityId);
        oneToOne.setUserId(userId);
        oneToOne.setResourceManagerId(authorId);
        oneToOne.setDateTime(dateTimeToOneToOne);
        oneToOne.setComment(comment);
        oneToOne.setOver(false);
        oneToOne.setDeleted(false);
        oneToOne.setVersion(1);
        return oneToOne;
    };

    private final Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setId(userId);
        user.setResourceManagerId(authorId);
        user.setFirstName("userFirstName");
        user.setLastName("userLastName");
        user.setEmail("userEmail");
        return user;
    };
    private final Supplier<User> resourceManagerSupplier = () -> {
        User resourceManager = new User();
        resourceManager.setId(authorId);
        resourceManager.setResourceManagerId("rMForRMId");
        resourceManager.setFirstName("resourceManagerFirstName");
        resourceManager.setLastName("resourceManagerLastName");
        resourceManager.setEmail("resourceManagerEmail");
        return resourceManager;
    };

    @BeforeEach
    void mockOfOneToOneMethodsAndPreparationOfUserRepository() {
        when(oneToOneUtil.findByIdOrThrowException(oneToOneSupplier.get().getId())).thenReturn(oneToOneSupplier.get());
        when(oneToOneProjector.project(any())).thenReturn(oneToOneSupplier.get());
        userRepository.save(userSupplier.get());
        userRepository.save(resourceManagerSupplier.get());
    }

    @Test
    void createOneToOneShouldUseUtils() {
        oneToOneCommandService.createOneToOne(oneToOneRequestSupplier.get(), authorId);
        verify(oneToOneUtil).checkIfCommentWithinLimit(oneToOneRequestSupplier.get().getComment());
        verify(oneToOneUtil).checkTimeNotPassed(oneToOneRequestSupplier.get().getDateTime());
        verify(userUtil).checkIfUserExistInPool(oneToOneRequestSupplier.get().getUserId(), authorId);
    }

    @Test
    void createOneToOneShouldCreateAndPublishEvent() {
        oneToOneCommandService.createOneToOne(oneToOneRequestSupplier.get(), authorId);
        verify(eventUtil).populateEventFields(any(), anyString(), anyInt(), anyString(), any(), anyBoolean());
        verify(eventPublisher).publishNoReupload(any());
    }

    @Test
    void createOneToOneShouldCreateAndPublishCommand() {
        oneToOneCommandService.createOneToOne(oneToOneRequestSupplier.get(), authorId);
        verify(commandUtil).populateCommandFields(any(), anyString(), any());
        verify(commandPublisher).publish(any());
    }

    @Test
    void createOneToOneShouldCreateOneToOne() {
        oneToOneCommandService.createOneToOne(oneToOneRequestSupplier.get(), authorId);
        verify(oneToOneProjector).project(any());
    }

    @Test
    void createOneToOneShouldSaveOneToOne() {
        oneToOneCommandService.createOneToOne(oneToOneRequestSupplier.get(), authorId);
        assertTrue(oneToOneRepository.findById(entityId).isPresent());
        assertEquals(oneToOneSupplier.get(), oneToOneRepository.findById(entityId).get());
    }

    @Test
    void completeOneToOneShouldUseUtils() {
        oneToOneCommandService.completeOneToOne(oneToOneSupplier.get().getId(), authorId);
        verify(oneToOneUtil).findByIdOrThrowException(oneToOneSupplier.get().getId());
        verify(oneToOneUtil).checkBeforeAppointedTime(any(), anyString());
        verify(eventUtil).populateEventFields(any(), anyString(), anyInt(), anyString(), any(), anyBoolean(), anyString());
    }

    @Test
    void completeOneToOneShouldCreateAndPublishEvent() {
        oneToOneCommandService.completeOneToOne(oneToOneSupplier.get().getId(), authorId);
        verify(eventUtil).populateEventFields(any(),anyString(), anyInt(), anyString(), any(), anyBoolean(), anyString());
        verify(eventPublisher).publish(any());
    }

    @Test
    void completeOneToOneShouldModifyOneToOne() {
        oneToOneCommandService.completeOneToOne(oneToOneSupplier.get().getId(), authorId);
        verify(oneToOneProjector).project((OneToOneCompletedEvent) any(), any());
    }

    @Test
    void completeOneToOneShouldSaveOneToOne() {
        oneToOneCommandService.completeOneToOne(oneToOneSupplier.get().getId(), authorId);
        assertTrue(oneToOneRepository.findById(entityId).isPresent());
        assertEquals(oneToOneSupplier.get(), oneToOneRepository.findById(entityId).get());
    }

    @Test
    void updateOneToOneShouldUseUtils() {
        oneToOneCommandService.updateOneToOne(oneToOneSupplier.get().getId(), updateOneToOneRequest, authorId);
        verify(oneToOneUtil, times(2)).checkTimeNotPassed(any());
        verify(oneToOneUtil).checkIfCommentWithinLimit(anyString());
        verify(oneToOneUtil).findByIdOrThrowException(anyString());
    }

    @Test
    void updateOneToOneShouldCreateAndPublishEvent() {
        oneToOneCommandService.updateOneToOne(oneToOneSupplier.get().getId(), updateOneToOneRequest, authorId);
        verify(eventUtil).populateEventFields(any(), anyString(), anyInt(), anyString(), any(), anyBoolean());
        verify(eventPublisher).publish(any());
    }

    @Test
    void updateOneToOneShouldCreateAndPublishCommand() {
        oneToOneCommandService.updateOneToOne(oneToOneSupplier.get().getId(), updateOneToOneRequest, authorId);
        verify(commandUtil).populateCommandFields(any(), anyString(), any());
        verify(commandPublisher).publish(any());
    }

    @Test
    void updateOneToOneShouldModifyOneToOne() {
        oneToOneCommandService.updateOneToOne(oneToOneSupplier.get().getId(), updateOneToOneRequest, authorId);
        verify(oneToOneProjector).project((OneToOneUpdatedEvent) any(), any());
    }

    @Test
    void updateOneToOneShouldUseRepositorySave() {
        oneToOneCommandService.updateOneToOne(oneToOneSupplier.get().getId(), updateOneToOneRequest, authorId);
        assertTrue(oneToOneRepository.findById(entityId).isPresent());
        assertEquals(oneToOneSupplier.get(), oneToOneRepository.findById(entityId).get());
    }

    @Test
    void deleteOneToOneShouldUseUtil() {
        oneToOneCommandService.deleteOneToOne(oneToOneSupplier.get().getId(), authorId);
        verify(oneToOneUtil).findByIdOrThrowException(anyString());
    }

    @Test
    void deleteOneToOneShouldCreateAndPublishEvent() {
        oneToOneCommandService.deleteOneToOne(oneToOneSupplier.get().getId(), authorId);
        verify(eventUtil).populateEventFields(any(), anyString(), anyInt(), anyString(), any(), anyBoolean());
        verify(eventPublisher).publish(any());
    }

    @Test
    void deleteOneToOneShouldCreateAndPublishCommand() {
        oneToOneCommandService.deleteOneToOne(oneToOneSupplier.get().getId(), authorId);
        verify(commandUtil).populateCommandFields(any(), anyString(), any());
        verify(commandPublisher).publish(any());
    }

    @Test
    void deleteOneToOneShouldModifyOneToOne() {
        oneToOneCommandService.deleteOneToOne(oneToOneSupplier.get().getId(), authorId);
        verify(oneToOneProjector).project((OneToOneDeletedEvent) any(), any());
    }

    @Test
    void deleteOneToOneShouldUseRepositorySave() {
        oneToOneCommandService.deleteOneToOne(oneToOneSupplier.get().getId(), authorId);
        assertTrue(oneToOneRepository.findById(entityId).isPresent());
        assertEquals(oneToOneSupplier.get(), oneToOneRepository.findById(entityId).get());
    }
}
