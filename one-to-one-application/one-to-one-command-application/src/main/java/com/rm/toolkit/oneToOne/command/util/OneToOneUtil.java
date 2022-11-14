package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.exception.InappropriateTimeException;
import com.rm.toolkit.oneToOne.command.exception.OneToOneNotFoundException;
import com.rm.toolkit.oneToOne.command.exception.badrequest.FieldTooLongException;
import com.rm.toolkit.oneToOne.command.model.OneToOne;
import com.rm.toolkit.oneToOne.command.repository.OneToOneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class OneToOneUtil {

    private static final int MAX_COMMENT_LENGTH = 255;
    private final OneToOneRepository repository;

    public OneToOne findByIdOrThrowException(String id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("121 встреча с id: " + id + " не найдена");
            throw new OneToOneNotFoundException(id);
        });
    }

    /**
     * Проверка, что поле dateTime не содержит прошедшее время.
     *
     * @param dateTime значение даты и времени
     * @throws com.rm.toolkit.oneToOne.command.exception.InappropriateTimeException поля dateTime содержат прошедшее время
     */
    public void checkTimeNotPassed(ZonedDateTime dateTime) {
        if (dateTime.toLocalDateTime().isBefore(LocalDateTime.now())) {
            log.error("1-2-1 встреча не может быть назначена на прошедшую дату " + dateTime +
                    " или изменена прошедшая встреча");
            throw new InappropriateTimeException(dateTime);
        }
    }

    public void checkBeforeAppointedTime(ZonedDateTime dateTime, String oneToOneId) {
        if (dateTime.toLocalDateTime().isAfter(LocalDateTime.now())) {
            log.error("121 встреча не может быть завершена раньше " + dateTime);
            throw new InappropriateTimeException(oneToOneId, dateTime);
        }
    }

    /**
     * Проверка, что поле comment не превышает ограничение.
     *
     * @param comment значение комментария
     * @throws com/rm/toolkit/oneToOne/command/exception/badrequest/FieldTooLongException.java длина поля comment больше ограничения
     */
    public void checkIfCommentWithinLimit(String comment) {
        if (comment.length() > MAX_COMMENT_LENGTH) {
            log.error("Слишком длинное поле comment {}", comment);
            throw new FieldTooLongException(comment);
        }
    }
}
