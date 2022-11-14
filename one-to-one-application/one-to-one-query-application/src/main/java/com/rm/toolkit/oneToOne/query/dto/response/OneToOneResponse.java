package com.rm.toolkit.oneToOne.query.dto.response;

import com.rm.toolkit.oneToOne.query.model.OneToOne;
import com.rm.toolkit.oneToOne.query.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class OneToOneResponse {

    @Schema(description = "ID встречи", example = "d6b750d2-bf32-4ef3-b79d-4362ae894a13")
    private final String id;
    @Schema(description = "ID ресурсного менеджера, который является интервьюером", example = "7368017b-f0d5-4e54-bbe4-72144e4d20e4")
    private final String resourceManagerId;
    @Schema(description = "Имя ресурсного менеджера, который является интервьюером", example = "Иван")
    private final String managerFirstName;
    @Schema(description = "Фамилия ресурсного менеджера, который является интервьюером", example = "Иванов")
    private final String managerLastName;
    @Schema(description = "Дата и время встречи в стандарте UTC", example = "2021-08-20T13:37:19.408Z")
    private final ZonedDateTime dateTime;
    @Schema(description = "Комментарий к встрече", example = "Узнать прогресс в изучении языков Fortran и COBOL")
    private final String comment;
    @Schema(description = "Флаг завершена ли встреча", example = "false")
    private final Boolean isOver;
    @Schema(description = "ID сотрудника, который является интервьюируемым", example = "4c353585-be59-41a2-9e36-edc7a0b57b3c")
    private final String userId;
    @Schema(description = "Имя сотрудника, который является интервьюируемым", example = "Петр")
    private final String userFirstName;
    @Schema(description = "Фамилия сотрудника, который является интервьюируемым", example = "Петров")
    private final String userLastName;

    public OneToOneResponse(OneToOne oneToOne, User user, User manager) {
        this.id = oneToOne.getId();
        this.resourceManagerId = manager.getId();
        this.managerFirstName = manager.getFirstName();
        this.managerLastName = manager.getLastName();
        this.dateTime = oneToOne.getDateTime();
        this.comment = oneToOne.getComment();
        this.isOver = oneToOne.getIsOver();
        this.userId = user.getId();
        this.userFirstName = user.getFirstName();
        this.userLastName = user.getLastName();
    }
}