package com.rm.toolkit.oneToOne.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.rm.toolkit.oneToOne.query.model.OneToOne;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneToOneInfoResponse {

    @Schema(example = "d6b750d2-bf32-4ef3-b79d-4362ae894a13")
    private String oneToOneId;
    @Schema(example = "7368017b-f0d5-4e54-bbe4-72144e4d20e4")
    private String resourceManagerId;
    private ZonedDateTime dateTime;
    @Schema(example = "Узнать прогресс в изучении языков Fortran и COBOL")
    private String comment;
    private Boolean isOver;

    public OneToOneInfoResponse(OneToOne oneToOne) {
        this.oneToOneId = oneToOne.getId();
        this.resourceManagerId = oneToOne.getResourceManagerId();
        this.dateTime = oneToOne.getDateTime();
        this.comment = oneToOne.getComment();
        this.isOver = oneToOne.getIsOver();
    }
}