package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.auth.LoginUser;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.exception.ErrorCode;
import com.hunseong.lolcruit.service.CommentService;
import com.hunseong.lolcruit.web.dto.comment.CommentRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Hunseong on 2022/05/25
 */
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Long> add(
            @LoginUser SessionUser user,
            @PathVariable Long postId,
            @RequestBody CommentRequestDto commentRequestDto) {

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return ResponseEntity.ok(commentService.add(user, postId, commentRequestDto));
    }
}
