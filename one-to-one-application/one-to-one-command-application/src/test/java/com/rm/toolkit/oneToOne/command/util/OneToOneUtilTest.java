package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.exception.InappropriateTimeException;
import com.rm.toolkit.oneToOne.command.exception.OneToOneNotFoundException;
import com.rm.toolkit.oneToOne.command.exception.badrequest.FieldTooLongException;
import com.rm.toolkit.oneToOne.command.model.OneToOne;
import com.rm.toolkit.oneToOne.command.repository.OneToOneRepository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OneToOneUtil.class})
@ExtendWith(SpringExtension.class)
class OneToOneUtilTest {
    @MockBean
    private OneToOneRepository oneToOneRepository;

    @Autowired
    private OneToOneUtil oneToOneUtil;

    private static final int DAYS_TO_121 = 7;
    OneToOne oneToOne = new OneToOne();

    @BeforeEach
    void preparationOfData() {
        String sd = Stream.iterate("a", s -> s)
                .limit(255)
                .reduce((s1, s2) -> s1 + s2).get();
        oneToOne.setComment(sd);
        oneToOne.setDateTime(ZonedDateTime.now().plusDays(DAYS_TO_121));
        oneToOne.setDeleted(false);
        oneToOne.setId("oneToOneIdForTest");
        oneToOne.setOver(false);
        oneToOne.setResourceManagerId("resourceManagerIdForTest");
        oneToOne.setUserId("userIdForTest");
        oneToOne.setVersion(1);
    }

    @Test
    void FindByIdOrThrowExceptionShouldUseRepository() {
        Mockito.when(oneToOneRepository.findById(oneToOne.getId())).thenReturn(Optional.of(oneToOne));
        oneToOneUtil.findByIdOrThrowException(oneToOne.getId());
        Mockito.verify(oneToOneRepository).findById(oneToOne.getId());
    }

    @Test
    void FindByIdOrThrowExceptionShouldThrowException() {
        Mockito.when(oneToOneRepository.findById(oneToOne.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(OneToOneNotFoundException.class, () -> oneToOneUtil.findByIdOrThrowException(oneToOne.getId()));
    }

    @Test
    void checkTimeNotPassedShouldThrowException() {
        ZonedDateTime pastDateTime = ZonedDateTime.now().minusDays(1);
        Assertions.assertThrows(InappropriateTimeException.class, () -> oneToOneUtil.checkTimeNotPassed(pastDateTime));
    }

    @Test
    void checkTimeNotPassedShouldNotThrowException() {
        ZonedDateTime futureDateTime = ZonedDateTime.now().plusDays(1);
        Assertions.assertDoesNotThrow(() -> oneToOneUtil.checkTimeNotPassed(futureDateTime));
    }

    @Test
    void checkBeforeAppointedTimeShouldThrowException() {
        Assertions.assertThrows(InappropriateTimeException.class,
                () -> oneToOneUtil.checkBeforeAppointedTime(
                        oneToOne.getDateTime(),
                        oneToOne.getId()
                )
        );
    }

    @Test
    void checkBeforeAppointedTimeShouldNotThrowException() {
        Assertions.assertDoesNotThrow(
                () -> oneToOneUtil.checkBeforeAppointedTime(
                        oneToOne.getDateTime().minusDays(DAYS_TO_121 + 1),
                        oneToOne.getId()
                        )
        );
    }

    @Test
    void checkIfCommentWithinLimitShouldThrowException() {
        Assertions.assertThrows(FieldTooLongException.class,
                () -> oneToOneUtil.checkIfCommentWithinLimit(oneToOne.getComment() + "a")
        );
    }

    @Test
    void checkIfCommentWithinLimitShouldNotThrowException() {
        Assertions.assertDoesNotThrow(
                () -> oneToOneUtil.checkIfCommentWithinLimit(oneToOne.getComment())
        );
    }
}

