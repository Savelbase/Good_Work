package com.rm.toolkit.mediaStorage.command.event.avatar;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("AVATAR_UPLOADED")
public class UploadAvatarCreatedEvent extends Event<UploadAvatarCreatedEvent.Payload> {

    public UploadAvatarCreatedEvent() {
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