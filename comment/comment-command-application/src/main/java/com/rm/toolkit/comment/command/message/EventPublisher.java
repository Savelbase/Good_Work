package com.rm.toolkit.comment.command.message;

import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;
import com.rm.toolkit.comment.command.event.UnsentEvent;
import com.rm.toolkit.comment.command.repository.EventRepository;
import com.rm.toolkit.comment.command.repository.UnsentEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    private final EventSender eventSender;
    private final EventRepository eventRepository;
    private final UnsentEventRepository unsentEventRepository;

    @Transactional
    public void publishNoReupload(Event<? extends EventPayload> event) {
        publishUnsentEventsRelatedToEntity(event.getEntityId());

        eventSender.send(event);
        eventRepository.save(event);
    }

    @Transactional
    public void publishWithReupload(Event<? extends EventPayload> event) {
        try {
            publishUnsentEventsRelatedToEntity(event.getEntityId());
            eventSender.send(event);
            eventRepository.save(event);
        } catch (RuntimeException ex) {
            eventRepository.save(event);
        }
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void publishUnsentEventsRelatedToEntity(String entityId) {
        for (UnsentEvent unsentEvent : unsentEventRepository.findAllByEntityId(entityId)) {
            Event<? extends EventPayload> event = eventRepository.findById(unsentEvent.getEventId()).orElseThrow();
            eventSender.send(event);
            unsentEventRepository.delete(unsentEvent);

            log.info("Отправлено неотправленное событие с id {}", unsentEvent.getEventId());
        }
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void publishAllUnsentEvents() {
        for (UnsentEvent unsentEvent : unsentEventRepository.findAll()) {
            Event<? extends EventPayload> event = eventRepository.findById(unsentEvent.getEventId()).orElseThrow();
            eventSender.send(event);
            unsentEventRepository.delete(unsentEvent);

            log.info("Отправлено неотправленное событие с id {}", unsentEvent.getEventId());
        }
    }
}
