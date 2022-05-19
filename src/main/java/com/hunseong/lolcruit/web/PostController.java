package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.service.post.PostService;
import com.hunseong.lolcruit.web.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        List<PostResponseDto> posts = postService.findAll();

        model.addAttribute("posts", posts);
        return "index";
    }
}
