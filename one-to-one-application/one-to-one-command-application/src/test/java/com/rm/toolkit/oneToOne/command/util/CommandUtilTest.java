package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.command.Command;
import com.rm.toolkit.oneToOne.command.command.EmailCommandPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = CommandUtil.class)
@ExtendWith(SpringExtension.class)
public class CommandUtilTest {
    private final String authorId = "7368017b-f0d5-4e54-bbe4-72144e4d20e4";
    private final Instant time = Instant.now();
    private final EmailCommandPayload payload = EmailCommandPayload.builder().build();
    private final Command<EmailCommandPayload> resultingCommand = new Command<>();

    @BeforeEach
    void preparationOfData() {
        String id = "88e2a5b1-5041-4012-9b16-01fed1450018";
        resultingCommand.setId(id);
        resultingCommand.setAuthor(authorId);
        String context = "${spring.application.name}";
        resultingCommand.setContext(context);
        resultingCommand.setTime(time);
        resultingCommand.setPayload(payload);
    }

    @Autowired
    private CommandUtil commandUtil;

    @Test
    void uuidStringToLongShouldCorrectWork() {
        assertEquals(8315898343772737108L, CommandUtil.uuidStringToLong(authorId));
    }

    @Test
    void populatedCommandFieldsShouldCorrectWork() {
        Command<EmailCommandPayload> command = new Command<>();
        commandUtil.populateCommandFields(command, authorId, payload);
        command.setTime(resultingCommand.getTime());
        assertForCommand(resultingCommand, command);
    }

    private void assertForCommand(Command<EmailCommandPayload> c1, Command<EmailCommandPayload> c2) {
        assertEquals(c1.getAuthor(), c2.getAuthor());
        assertEquals(c1.getContext(), c2.getContext());
        assertTrue(Objects.nonNull((c1.getTime())));
        assertTrue(Objects.nonNull(c2.getTime()));
        assertEquals(c1.getPayload(), c2.getPayload());
    }
}
