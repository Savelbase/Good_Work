package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.UnsentEvent;
import com.rm.toolkit.user.command.repository.EventRepository;
import com.rm.toolkit.user.command.repository.UnsentEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
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
     * Если сообщение не удастся отправить сразу, то оно добавится в очередь неотравленных событий, и когда-нибудь отправится
     *
     * @param event Отправляемое событие
     */
    @Transactional
    public void publishWithReupload(Event<? extends EventPayload> event) {
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
     * Метод пытается отправить первое событие. Если отправка была успешной, то продолжает отправлять до конца.
     * Если отравка первого сообщения не была успешной, то кидается ошибка, чтобы её увидел пользователь.
     * Если первое сообщений отправилось успешно, то потом где-то в середине возникла ошибка, то оставшиеся события добавляются в очередь неотправленных событий. Это так называемый Outbox pattern.
     * Перед отправкой первого события метод проверяет, нет ли неотравленных событий, и если есть, то сначала пытается отправить их.
     *
     * @param events Очередь событий, которые нужно отрпавить
     * @throws RuntimeException Первое событие из queue не удалось отправить
     */
    @Transactional
    public void publish(Queue<Event<? extends EventPayload>> events) {
        var event = events.poll();
        // Чтобы IDE предупреждение не показывал на строчке eventRepository.save(event)
        if (event == null)
            throw new NullPointerException();

        publishUnsentEventsRelatedToEntity(event.getEntityId());

        eventSender.send(event);
        eventRepository.save(event);

        try {
            while (!events.isEmpty()) {
                event = events.poll();
                publishUnsentEventsRelatedToEntity(event.getEntityId());

                eventSender.send(event);
                eventRepository.save(event);
            }
        } catch (RuntimeException ex) {
            List<Event<? extends EventPayload>> eventList = new ArrayList<>();
            eventList.add(event);
            eventList.addAll(events);

            eventRepository.saveAll(eventList);
            unsentEventRepository.saveAll(eventList.stream().map(e -> UnsentEvent.builder().eventId(e.getId()).build())
                    .collect(Collectors.toList()));
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
