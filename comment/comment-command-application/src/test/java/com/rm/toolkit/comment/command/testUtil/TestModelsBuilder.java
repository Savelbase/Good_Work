package com.rm.toolkit.comment.command.testUtil;

import com.rm.toolkit.comment.command.model.Comment;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TestModelsBuilder {
    private final String value = "Test";

    public Comment getTestComment() {
        return Comment.builder()
                .id(value)
                .userId(value)
                .text(value)
                .senderId(value)
                .deleted(false)
                .dateTime(ZonedDateTime.now())
                .version(0)
                .build();
    }
}
