package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

/**
 * Created by Hunseong on 2022/05/25
 */
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
}
