package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.service.post.PostService;
import com.hunseong.lolcruit.web.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.hunseong.lolcruit.constants.PagingConst.BLOCK_PAGE_COUNT;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String index(
            @RequestParam(name = "pos", required = false) Position position,
            Pageable pageable,
            Model model
    ) {

        Page<PostResponseDto> posts = postService.findAll(pageable, position);

        // 임의로 URL을 조작하여 페이지 범위를 벗어나는 index에 접근할 경우 throw
        // TODO Exception Handling
        if (pageable.getPageNumber() >= posts.getTotalPages()) {
            throw new RuntimeException("page index exception");
        }

        // 0~4 페이지 : block 0
        // 5~9 페이지 : block 1
        // ...
        int currentBlock = posts.getNumber() / BLOCK_PAGE_COUNT;

        // 현재 block에 5페이지를 채울 수 있는지의 여부에 따른 endPage 분기
        int endPage = (currentBlock + 1) * BLOCK_PAGE_COUNT < posts.getTotalPages() ?
                (currentBlock + 1) * BLOCK_PAGE_COUNT - 1 : posts.getTotalPages() - 1;

        boolean hasPrev = currentBlock != 0;
        boolean hasNext = (currentBlock + 1) * BLOCK_PAGE_COUNT < posts.getTotalPages();

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("startPage", currentBlock * BLOCK_PAGE_COUNT);
        model.addAttribute("currentPage", posts.getNumber());
        model.addAttribute("currentBlock", currentBlock);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("endPage", endPage);
        model.addAttribute("first", 0);
        model.addAttribute("last", posts.getTotalPages() - 1);
        return "index";
    }
}
