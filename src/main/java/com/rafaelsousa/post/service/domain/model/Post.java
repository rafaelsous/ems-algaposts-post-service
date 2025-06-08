package com.rafaelsousa.post.service.domain.model;

import com.rafaelsousa.post.service.api.model.PostInput;
import com.rafaelsousa.post.service.common.GeneratorID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private UUID id;
    private String title;

    @Column(length = 1024)
    private String body;
    private String author;
    private Long wordCount;
    private Double calculatedValue;

    public static Post convertToDomain(PostInput input) {
        return Post.builder()
                .id(GeneratorID.generateTimeBasedUUID())
                .title(input.getTitle())
                .body(input.getBody())
                .author(input.getAuthor())
                .wordCount(null)
                .calculatedValue(null)
                .build();
    }
}