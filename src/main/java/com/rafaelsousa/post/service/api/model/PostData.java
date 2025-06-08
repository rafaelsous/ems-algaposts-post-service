package com.rafaelsousa.post.service.api.model;

import com.rafaelsousa.post.service.domain.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostData {
    private UUID id;
    private String title;
    private String body;
    private String author;
    private Long wordCount;
    private Double calculatedValue;

    public static PostData convertToModel(Post post) {
        return PostData.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .author(post.getAuthor())
                .wordCount(post.getWordCount())
                .calculatedValue(post.getCalculatedValue())
                .build();
    }
}