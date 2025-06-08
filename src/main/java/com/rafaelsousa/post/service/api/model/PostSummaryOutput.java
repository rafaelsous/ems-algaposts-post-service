package com.rafaelsousa.post.service.api.model;

import com.rafaelsousa.post.service.domain.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryOutput {
    private UUID id;
    private String title;
    private String summary;
    private String author;

    public static PostSummaryOutput convertToSummaryModel(Post post) {
        return PostSummaryOutput.builder()
                .id(post.getId())
                .title(post.getTitle())
                .summary(post.getBody().lines().limit(3).collect(Collectors.joining(" ")))
                .author(post.getAuthor())
                .build();
    }
}