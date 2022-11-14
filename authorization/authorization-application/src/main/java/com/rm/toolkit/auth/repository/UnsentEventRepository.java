package com.rm.toolkit.auth.repository;

import com.rm.toolkit.auth.event.UnsentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnsentEventRepository extends JpaRepository<UnsentEvent, String> {

    List<UnsentEvent> findAllByEntityId(String entityId);
}
