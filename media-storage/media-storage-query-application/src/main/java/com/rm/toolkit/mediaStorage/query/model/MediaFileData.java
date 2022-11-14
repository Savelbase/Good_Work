package com.rm.toolkit.mediaStorage.query.model;

import com.rm.toolkit.mediaStorage.query.model.iface.Versionable;
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
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name="media_storage_query")
public class MediaFileData implements Versionable {

    @Id
    @Column
    private String id;

    @Column
    private String url;

    @Column
    private boolean isConfirmed;

    @Column
    private Date uploadDate;

    private Integer version;



}