package com.hunseong.lolcruit.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Hunseong on 2022/05/25
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
