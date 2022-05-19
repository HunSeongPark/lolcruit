package com.hunseong.lolcruit.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Hunseong on 2022/05/19
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByIdDesc(Pageable pageable);

    Page<Post> findAllByPositionOrderByIdDesc(Pageable pageable, Position position);
}
