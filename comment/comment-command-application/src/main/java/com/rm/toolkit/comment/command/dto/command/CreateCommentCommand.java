package com.rm.toolkit.comment.command.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentCommand {
    private String id;
    private ZonedDateTime dateTime;
    private String userId;
    private String senderId ;
    private String text;

}
