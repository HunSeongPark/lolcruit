package com.hunseong.lolcruit.util;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Hunseong on 2022/05/19
 */
@Profile("local")
@Component
@RequiredArgsConstructor
public class TestDBInit {

    private final TestDBInitService service;

    @PostConstruct
    private void init() {
        service.init();
    }

    @Component
    static class TestDBInitService {

        @PersistenceContext private EntityManager em;

        @Transactional
        public void init() {
            Post post1 = Post.builder()
                    .title("title1")
                    .writer("hunseong")
                    .position(Position.TOP)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .writer("hunseong2")
                    .position(Position.MID)
                    .build();

            Post post3 = Post.builder()
                    .title("title3title3title3title3title3e3tit" +
                            "le3title3title3title3title3title3title3title" +
                            "3title3title3title3titltitle3title3title3title3title3title3title3title3")
                    .writer("박훈성")
                    .position(Position.ADC)
                    .build();

            em.persist(post1);
            em.persist(post2);
            em.persist(post3);
        }
    }
}
