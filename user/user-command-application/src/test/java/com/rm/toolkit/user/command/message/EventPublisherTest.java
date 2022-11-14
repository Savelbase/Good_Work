package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.UnsentEvent;
import com.rm.toolkit.user.command.repository.EventRepository;
import com.rm.toolkit.user.command.repository.UnsentEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ContextConfiguration(classes = {EventPublisher.class})
@ExtendWith(SpringExtension.class)
class EventPublisherTest {
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

    @Test
    void shouldCatchRuntimeExceptionWhilePublishWithReupload() {
        String eventId = "Test";
        Event<EventPayload> event = new Event<>();
        event.setId(eventId);
        UnsentEvent unsentEvent = new UnsentEvent();

        Mockito.doThrow(RuntimeException.class).when(eventSender).send(event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        Mockito.when(unsentEventRepository.save(UnsentEvent.builder().eventId(event.getId()).build()))
                .thenReturn(unsentEvent);

        eventPublisher.publishWithReupload(event);

        Mockito.verify(eventSender).send(event);
        Mockito.verify(eventRepository).save(event);
        Mockito.verify(unsentEventRepository).save(UnsentEvent.builder().eventId(event.getId()).build());
    }

    @Test
    void publish() {
        Event<EventPayload> event = new Event<>();
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        eventQueue.add(event);
        eventQueue.add(event);

        Mockito.doNothing().when(eventSender).send(event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        eventPublisher.publish(eventQueue);

        Mockito.verify(eventSender, Mockito.times(2)).send(event);
        Mockito.verify(eventRepository, Mockito.times(2)).save(event);
    }

    @Test
    void shouldCatchRuntimeExceptionDuringPublish() {
        Event<EventPayload> event = new Event<>();
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        eventQueue.add(event);
        eventQueue.add(event);
        List<Event<? extends EventPayload>> eventList = new ArrayList<>();
        eventList.add(event);

        Mockito.doNothing().doThrow(RuntimeException.class).when(eventSender).send(event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        Mockito.when(eventRepository.saveAll(eventList)).thenReturn(eventList);

        eventPublisher.publish(eventQueue);

        Mockito.verify(eventSender, Mockito.times(2)).send(event);
        Mockito.verify(eventRepository).save(event);
        Mockito.verify(eventRepository).saveAll(eventList);
    }

    @Test
    void publishUnsentEventsRelatedToEntity() {
        String entityId = "Test";
        String eventId = "Test";
        UnsentEvent unsentEvent = new UnsentEvent();
        unsentEvent.setEventId(eventId);
        List<UnsentEvent> unsentEventList = new ArrayList<>();
        unsentEventList.add(unsentEvent);
        Event<EventPayload> event = new Event<>();

        Mockito.when(unsentEventRepository.findAllByEntityId(entityId)).thenReturn(unsentEventList);
        Mockito.when(eventRepository.findById(unsentEvent.getEventId())).thenReturn(Optional.of(event));
        Mockito.doNothing().when(eventSender).send(event);
        Mockito.doNothing().when(unsentEventRepository).delete(unsentEvent);

        eventPublisher.publishUnsentEventsRelatedToEntity(entityId);

        Mockito.verify(unsentEventRepository).findAllByEntityId(entityId);
        Mockito.verify(eventRepository).findById(unsentEvent.getEventId());
        Mockito.verify(eventSender).send(event);
        Mockito.verify(unsentEventRepository).delete(unsentEvent);
    }

    @Test
    void publishAllUnsentEvents() {
        String unsentEventId = "Test";
        UnsentEvent unsentEvent = new UnsentEvent();
        unsentEvent.setEventId(unsentEventId);
        List<UnsentEvent> eventList = new ArrayList<>();
        eventList.add(unsentEvent);
        Event<EventPayload> event = new Event<>();

        Mockito.when(unsentEventRepository.findAll()).thenReturn(eventList);
        Mockito.when(eventRepository.findById(unsentEvent.getEventId())).thenReturn(Optional.of(event));
        Mockito.doNothing().when(eventSender).send(event);
        Mockito.doNothing().when(unsentEventRepository).delete(unsentEvent);

        eventPublisher.publishAllUnsentEvents();

        Mockito.verify(unsentEventRepository).findAll();
        Mockito.verify(eventRepository).findById(unsentEventId);
        Mockito.verify(eventSender).send(event);
        Mockito.verify(unsentEventRepository).delete(unsentEvent);
    }
}