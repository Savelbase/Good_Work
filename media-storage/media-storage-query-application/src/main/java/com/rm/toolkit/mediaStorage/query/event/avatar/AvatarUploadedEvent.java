package com.rm.toolkit.mediaStorage.query.event.avatar;

import com.rm.toolkit.mediaStorage.query.event.Event;
import com.rm.toolkit.mediaStorage.query.event.EventPayload;
import com.rm.toolkit.mediaStorage.query.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AvatarUploadedEvent extends Event<AvatarUploadedEvent.Payload> {

    public AvatarUploadedEvent() {
        this.type = EventType.AVATAR_UPLOADED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        protected String url;

        protected boolean isConfirmed;

        protected Date uploadDate;
    }
}
