package com.rm.toolkit.emailsender.command.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "email")
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Email {

    @Id
    private String id;

    @Column
    private String emailTo;

    @Column
    private String subject;

    @Column
    private String text;

    @Column
    private ZonedDateTime sendingDateTime;

}
