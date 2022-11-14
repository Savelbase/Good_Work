package com.rm.toolkit.oneToOne.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;

public class InappropriateTimeException extends ResponseStatusException {
    public InappropriateTimeException(String oneToOneId, ZonedDateTime dateTime) {
        super(HttpStatus.CONFLICT, "121 встреча с id: " + oneToOneId +
                " не может быть завершена раньше " + dateTime);
    }
    public InappropriateTimeException(ZonedDateTime dateTime) {
        super(HttpStatus.CONFLICT, "121 встреча не может быть назначена на прошедшую дату " + dateTime + " или изменена прошедшая встреча");
    }
}
