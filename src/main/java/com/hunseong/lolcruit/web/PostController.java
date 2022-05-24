package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.auth.LoginUser;
import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.service.post.PostService;
import com.hunseong.lolcruit.web.dto.post.PostEditDto;
import com.hunseong.lolcruit.web.dto.post.PostIndexResponseDto;
import com.hunseong.lolcruit.web.dto.post.PostRequestDto;
import com.hunseong.lolcruit.web.dto.post.PostReadDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hunseong.lolcruit.constants.PagingConst.BLOCK_PAGE_COUNT;

/**
 * Created by Hunseong on 2022/05/19
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String index(
            @RequestParam(value = "pos", required = false) Position position,
            @RequestParam(value = "keyword", required = false) String keyword,
            @LoginUser SessionUser user,
            Pageable pageable,
            Model model
    ) {

        if (user != null) {
            model.addAttribute("user", user);
        }
        Page<PostIndexResponseDto> posts = postService.findAll(pageable, position, keyword);

        // 임의로 URL을 조작하여 페이지 범위를 벗어나는 index에 접근할 경우 throw
        // TODO Exception Handling
        if (pageable.getPageNumber() >= posts.getTotalPages() && posts.getTotalElements() != 0) {
            throw new RuntimeException("page index exception");
        }

        // 0~4 페이지 : block 0
        // 5~9 페이지 : block 1
        // ...
        int currentBlock = posts.getNumber() / BLOCK_PAGE_COUNT;

        // 현재 block에 5페이지를 채울 수 있는지의 여부에 따른 현재 block 마지막 페이지 분기
        int endPage;
        if ((currentBlock + 1) * BLOCK_PAGE_COUNT < posts.getTotalPages()) {
            endPage = (currentBlock + 1) * BLOCK_PAGE_COUNT - 1;
        } else if (posts.getTotalElements() == 0) {
            endPage = 0;
        } else {
            endPage = posts.getTotalPages() - 1;
        }

        // 가장 마지막 페이지 index
        int lastPage;
        if (posts.getTotalElements() == 0) {
            lastPage = 0;
        } else {
            lastPage = posts.getTotalPages() - 1;
        }

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
        model.addAttribute("last", lastPage);
        model.addAttribute("position", position);
        model.addAttribute("keyword", keyword);

        return "index";
    }

    @GetMapping("/posts/add")
    public String addForm(@ModelAttribute("post") PostRequestDto postRequestDto) {
        return "post/add";
    }

    @PostMapping("/posts/add")
    public String add(
            @Validated @ModelAttribute("post") PostRequestDto postRequestDto,
            BindingResult bindingResult,
            @LoginUser SessionUser user) {

        if (bindingResult.hasErrors()) {
            return "post/add";
        }

        if (user == null) {
            // TODO
            throw new RuntimeException("유저 정보가 없습니다.");
        }

        postService.add(postRequestDto, user);
        return "redirect:/";
    }

    @GetMapping("posts/{id}")
    public String read(
            @LoginUser SessionUser user,
            @PathVariable Long id,
            Model model
    ) {
        PostReadDto post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("user", user);
        return "post/post";
    }

    @GetMapping("/posts/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model
    ) {
        PostEditDto post = postService.findByIdForEdit(id);
        model.addAttribute("post", post);
        return "post/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String edit(
            @Validated @ModelAttribute("post") PostEditDto postEditDto,
            BindingResult bindingResult,
            @PathVariable Long id
    ) {
        if (bindingResult.hasErrors()) {
            return "post/edit";
        }

        postService.update(postEditDto, id);
        return "redirect:/posts/" + id;
    }

}
