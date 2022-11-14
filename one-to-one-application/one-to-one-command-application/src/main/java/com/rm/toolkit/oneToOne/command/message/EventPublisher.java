package com.rm.toolkit.oneToOne.command.message;

import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.event.UnsentEvent;
import com.rm.toolkit.oneToOne.command.repository.EventRepository;
import com.rm.toolkit.oneToOne.command.repository.UnsentEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
public class EventPublisher {

    private final EventSender eventSender;
    private final EventRepository eventRepository;
    private final UnsentEventRepository unsentEventRepository;

    /**
     * Если событие не удастся отправить сразу, то выбросит RuntimeException
     *
     * @param event Отправляемое событие
     * @throws RuntimeException Событие не удалось отправить
     */
    @Transactional
    public void publishNoReupload(Event<? extends EventPayload> event) {
        publishUnsentEventsRelatedToEntity(event.getEntityId());

        eventSender.send(event);
        eventRepository.save(event);
    }

    /**
     * Метод пытается отправить первое событие. Если отправка была успешной, то продолжает отправлять до конца.
     * Если отравка первого сообщения не была успешной, то кидается ошибка, чтобы её увидел пользователь.
     * Если первое сообщений отправилось успешно, то потом где-то в середине возникла ошибка, то оставшиеся события добавляются в очередь неотправленных событий. Это так называемый Outbox pattern.
     * Перед отправкой первого события метод проверяет, нет ли неотравленных событий, и если есть, то сначала пытается отправить их.
     *
     * @param event Cобытие, которое нужно отправить
     * @throws RuntimeException Первое событие не удалось отправить
     */
    @Transactional
    public void publish(Event<? extends EventPayload> event) {

        publishUnsentEventsRelatedToEntity(event.getEntityId());

        eventSender.send(event);
        eventRepository.save(event);

        try {
                publishUnsentEventsRelatedToEntity(event.getEntityId());

                eventSender.send(event);
                eventRepository.save(event);
        } catch (RuntimeException ex) {

            eventRepository.save(event);
            unsentEventRepository.save(UnsentEvent.builder().eventId(event.getId()).build());
        }
    }

    /**
     * @param entityId неотправленные события, связанные с этой сущностью будут отправлены
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void publishUnsentEventsRelatedToEntity(String entityId) {
        for (UnsentEvent unsentEvent : unsentEventRepository.findAllByEntityId(entityId)) {
            Event<? extends EventPayload> event = eventRepository.findById(unsentEvent.getEventId()).orElseThrow();
            eventSender.send(event);
            unsentEventRepository.delete(unsentEvent);

            log.info("Отправлено неотправленное событие с id {}", unsentEvent.getEventId());
        }
    }

    /**
     * Раз в 1 минуту отправляет неотравленные события, если они есть
     */
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
