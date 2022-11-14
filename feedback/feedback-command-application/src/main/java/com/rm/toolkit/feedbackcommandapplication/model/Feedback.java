package com.rm.toolkit.feedbackcommandapplication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rm.toolkit.feedbackcommandapplication.model.type.Versionable;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "feedback_command")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Feedback implements Versionable {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(name = "date_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime dateTime;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "rm_id", nullable = false)
    private String resourceManagerId;

    @Column(name = "overall_assessment")
    private int overAllAssessment;

    @Column(name = "professional_skills")
    private int professionalSkills;

    @Column(name = "work_quality")
    private int workQuality;

    @Column(name = "critical_thinking")
    private int criticalThinking;

    @Column(name = "reliability")
    private int reliability;

    @Column(name = "communication_skills")
    private int communicationSkills;

    @Column(name = "development_comment")
    private String development;

    @Column(name = "project_comment")
    private String project;

    @Column(name = "goals_comment")
    private String goals;

    @Column(name = "department_comment")
    private String department;

    @Column(name = "activities_comment")
    private String activities;

    @Column(name = "additionally_comment")
    private String additionally;

    @Column(name = "one_to_one_id")
    private String oneToOneId;

    private Integer version;
}
