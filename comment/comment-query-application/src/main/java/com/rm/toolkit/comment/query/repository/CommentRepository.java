package com.rm.toolkit.comment.query.repository;

import com.rm.toolkit.comment.query.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query("Select c from Comment c where c.userId=:userId and c.deleted = false")
    Optional<List<Comment>> findAllCommentsByUserIdOrderByDateTimeDesc(@Param("userId") String userId, Pageable pageable);
}
