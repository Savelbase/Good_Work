package com.rm.toolkit.oneToOne.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "unsent_email_command")
public class UnsentCommand {

    @Id
    private String id;

    @Column
    private String commandId;
}