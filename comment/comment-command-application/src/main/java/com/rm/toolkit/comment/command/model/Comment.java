package com.rm.toolkit.comment.command.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "comments")
public class Comment implements Versionable{
    @Id
    @Column(nullable = false)
    private String id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "sender_id", nullable = false)
    private String senderId;
    @Column(name = "text")
    private String text;
    @Column(name = "date_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime dateTime;
    @Column(name = "version")
    private Integer version;
    @Column(name = "DELETED")
    private boolean deleted;
}
