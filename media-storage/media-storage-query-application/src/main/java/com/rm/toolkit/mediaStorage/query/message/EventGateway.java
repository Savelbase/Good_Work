package com.rm.toolkit.mediaStorage.query.message;

import com.rm.toolkit.mediaStorage.query.event.Event;
import com.rm.toolkit.mediaStorage.query.event.EventPayload;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        switch (event.getType()) {
            case AVATAR_UPLOADED:
                eventHandler.handle((AvatarUploadedEvent) event);
                break;
            case AVATAR_CONFIRMED:
                eventHandler.handle((AvatarConfirmedEvent) event);
                break;
            case AVATAR_DELETED:
                eventHandler.handle((AvatarDeletedEvent) event);
                break;
        }
    }
}
