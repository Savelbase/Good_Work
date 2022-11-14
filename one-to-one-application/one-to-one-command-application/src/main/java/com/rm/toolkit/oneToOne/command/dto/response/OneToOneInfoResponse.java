package com.rm.toolkit.oneToOne.command.dto.response;

import com.rm.toolkit.oneToOne.command.model.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneToOneInfoResponse {

    private String oneToOneId;
    private String resourceManagerId;
    private ZonedDateTime dateTime;
    private String comment;
    private boolean isOver;

    public OneToOneInfoResponse(OneToOne oneToOne) {
        this.oneToOneId = oneToOne.getId();
        this.resourceManagerId = oneToOne.getResourceManagerId();
        this.dateTime = oneToOne.getDateTime();
        this.comment = oneToOne.getComment();
    }
}