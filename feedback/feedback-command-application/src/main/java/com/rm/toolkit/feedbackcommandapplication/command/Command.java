package com.rm.toolkit.feedbackcommandapplication.command;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@MappedSuperclass
@EqualsAndHashCode(of = "id")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Command<T> {

    /**
     * Идентификатор команды
     */
    @Id
    private String id;

    /**
     * Тип команды
     */
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private CommandType type;

    /**
     * Контекст команды
     */
    @Column
    private String context;

    /**
     * Время команды
     */
    @Column
    private Instant time;

    /**
     * Объект
     */
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private T payload;
}