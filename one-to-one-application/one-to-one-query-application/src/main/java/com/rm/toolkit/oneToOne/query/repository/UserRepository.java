package com.rm.toolkit.oneToOne.query.repository;

import com.rm.toolkit.oneToOne.query.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
