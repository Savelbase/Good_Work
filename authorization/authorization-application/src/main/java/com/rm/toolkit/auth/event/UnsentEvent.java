package com.rm.toolkit.auth.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UnsentEvent {

    @Id
    @GeneratedValue(generator = "unsent_event_id_seq")
    @GenericGenerator(
            name = "unsent_event_id_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "unsent_event_id_seq")
            }
    )
    private Long id;

    @Column
    private String eventId;

    @Column
    private String entityId;
}
