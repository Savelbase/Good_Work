package com.rm.toolkit.auth.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "hash", name = "uk_refresh_token_hash"))
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "hash")
@ToString(of = {"hash", "expiryDate"})
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(nullable = false, length = 64)
    private String hash;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private String userId;
}
