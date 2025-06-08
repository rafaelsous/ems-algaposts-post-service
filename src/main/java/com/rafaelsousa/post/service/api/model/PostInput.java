package com.rafaelsousa.post.service.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostInput {

    @NotBlank
    private String title;

    @NotBlank
    @Length(min = 3, max = 1024)
    private String body;

    @NotBlank
    private String author;
}