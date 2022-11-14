package com.rm.toolkit.feedbackcommandapplication.model;

import com.rm.toolkit.feedbackcommandapplication.model.type.Versionable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback_interval")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackInterval implements Versionable {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private Integer interval;

    private Integer version;
}
