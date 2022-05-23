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
import java.util.Random;

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

            for (int i = 0; i < 500; i++) {
                Position[] pos = Position.values();
                int rand = new Random().nextInt(5);

                Post post = Post.builder()
                        .title("title" + i)
                        .writer("writer" + i)
                        .position(pos[rand])
                        .build();

                em.persist(post);
            }
        }
    }
}
