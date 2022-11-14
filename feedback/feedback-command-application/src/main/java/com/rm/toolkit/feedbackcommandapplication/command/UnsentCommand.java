package com.rm.toolkit.feedbackcommandapplication.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unsent_email_command")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnsentCommand {

    @Id
    private String id;

    @Column
    private String commandId;
}