package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;


}
