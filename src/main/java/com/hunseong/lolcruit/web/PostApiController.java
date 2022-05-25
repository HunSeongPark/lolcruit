package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Hunseong on 2022/05/24
 */
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(postService.delete(id));
    }
}
