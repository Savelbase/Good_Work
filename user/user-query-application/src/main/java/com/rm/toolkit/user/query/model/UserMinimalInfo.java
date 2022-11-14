package com.rm.toolkit.user.query.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status != 'DELETED'")
@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class UserMinimalInfo extends UserBase {

    public UserMinimalInfo(String id, String firstName, String lastName, String avatarPath, boolean isRm) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarPath = avatarPath;
        this.isRm = isRm;
    }

    public UserMinimalInfo(UserMediumInfo userMediumInfo) {
        this(userMediumInfo.getId(), userMediumInfo.getFirstName(), userMediumInfo.getLastName(),
                userMediumInfo.getAvatarPath(), userMediumInfo.isRm());
    }
}
