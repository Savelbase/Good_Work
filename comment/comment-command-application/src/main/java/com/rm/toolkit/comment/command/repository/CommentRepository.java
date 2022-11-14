package com.rm.toolkit.comment.command.repository;

import com.rm.toolkit.comment.command.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment , String> {


    @NonNull
    @Query("SELECT c FROM Comment c WHERE c.id=:id")
    Optional<Comment> findById(@Param("id") @NonNull String id);

    @Override
    @Modifying
    @Query("UPDATE Comment c set c.deleted = true  where c.id = :commentId")
    void deleteById(@Param("commentId") String commentId);

}
