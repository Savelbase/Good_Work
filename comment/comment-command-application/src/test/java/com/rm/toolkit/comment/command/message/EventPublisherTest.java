package com.rm.toolkit.comment.command.message;

import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;
import com.rm.toolkit.comment.command.repository.EventRepository;
import com.rm.toolkit.comment.command.repository.UnsentEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EventPublisher.class})
@ExtendWith(SpringExtension.class)
public class EventPublisherTest {
    @Autowired
    private EventPublisher eventPublisher;
    @MockBean
    private EventSender eventSender;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private UnsentEventRepository unsentEventRepository;

    @Test
    void publishNoReupload() {
        Event<EventPayload> event = new Event<>();
        Mockito.doNothing().when(eventSender).send(event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        eventPublisher.publishNoReupload(event);
        Mockito.verify(eventSender).send(event);
        Mockito.verify(eventRepository).save(event);
    }

    @Test
    void publishWithReupload() {
        Event<EventPayload> event = new Event<>();
        Mockito.doNothing().when(eventSender).send(event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        eventPublisher.publishWithReupload(event);
        Mockito.verify(eventSender).send(event);
        Mockito.verify(eventRepository).save(event);
    }

}
