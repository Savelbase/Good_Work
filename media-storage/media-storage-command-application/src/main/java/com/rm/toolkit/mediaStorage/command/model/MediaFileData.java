package com.rm.toolkit.mediaStorage.command.model;

import com.rm.toolkit.mediaStorage.command.model.iface.Versionable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="media_storage")
public class MediaFileData implements Versionable {

    @Id
    @Column
    private String id;

    @Column
    private String userId;

    @Column
    private String url;

    @Column
    private boolean isConfirmed;

    @Column
    private Date uploadDate;

    private Integer version;

}