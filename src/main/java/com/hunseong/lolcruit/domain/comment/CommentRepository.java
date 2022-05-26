package com.hunseong.lolcruit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by Hunseong on 2022/05/25
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.id = :id")
    Optional<Comment> findByIdFetchUser(@Param("id") Long commentId);
}
