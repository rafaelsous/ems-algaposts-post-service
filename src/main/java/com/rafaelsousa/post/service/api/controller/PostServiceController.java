package com.rafaelsousa.post.service.api.controller;

import com.rafaelsousa.post.service.api.model.PostInput;
import com.rafaelsousa.post.service.api.model.PostData;
import com.rafaelsousa.post.service.api.model.PostSummaryOutput;
import com.rafaelsousa.post.service.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostServiceController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostData create(@RequestBody @Valid PostInput input) {
        return postService.create(input);
    }

    @GetMapping("/{postId}")
    public PostData getOne(@PathVariable UUID postId) {
        return postService.findOrFail(postId);
    }

    @GetMapping
    public Page<PostSummaryOutput> search(@PageableDefault Pageable pageable) {
        return postService.findAllPage(pageable);
    }
}