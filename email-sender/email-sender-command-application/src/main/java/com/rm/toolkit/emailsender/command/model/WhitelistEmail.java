package com.rm.toolkit.emailsender.command.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "uk_whitelist_email_email"))
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class WhitelistEmail {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String email;
}