package com.hunseong.lolcruit.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by Hunseong on 2022/05/19
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    // ALL
    Page<Post> findAllByOrderByIdDesc(Pageable pageable);

    // ALL & Search
    Page<Post> findAllByTitleContainingOrderByIdDesc(Pageable pageable, String keyword);

    // position
    Page<Post> findAllByPositionOrderByIdDesc(Pageable pageable, Position position);

    // position && Search
    @Query("select p from Post p where p.position = :position and p.title like %:keyword% order by p.id desc")
    Page<Post> findAllByPositionAndKeyword(
            Pageable pageable,
            @Param("position") Position position,
            @Param("keyword") String keyword
    );

    @Query("select distinct p from Post p left join fetch p.comments c left join fetch c.user where p.id = :id")
    Optional<Post> findByIdFetchComments(@Param("id") Long id);
}
