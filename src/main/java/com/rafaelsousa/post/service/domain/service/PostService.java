package com.rafaelsousa.post.service.domain.service;

import com.rafaelsousa.post.service.api.exception.ResourceNotFoundException;
import com.rafaelsousa.post.service.api.model.PostInput;
import com.rafaelsousa.post.service.api.model.PostData;
import com.rafaelsousa.post.service.api.model.PostSummaryOutput;
import com.rafaelsousa.post.service.domain.model.Post;
import com.rafaelsousa.post.service.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.rafaelsousa.post.service.api.infra.rabbitmq.RabbitMQConfig.FANOUT_EXCHANGE_PROCESS_TEXT;
import static com.rafaelsousa.post.service.api.infra.rabbitmq.RabbitMQConfig.FANOUT_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public PostData create(PostInput input) {
        Post post = Post.convertToDomain(input);
        post = postRepository.saveAndFlush(post);

        PostData postData = PostData.convertToModel(post);
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_PROCESS_TEXT, FANOUT_ROUTING_KEY, postData);

        return postData;
    }

    public PostData findOrFail(UUID postId) {
        return postRepository.findById(postId)
                .map(PostData::convertToModel)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Post with ID %s not found", postId)));
    }

    public Page<PostSummaryOutput> findAllPage(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostSummaryOutput::convertToSummaryModel);
    }

    public void updatePostWithProcessingResult(PostData postData) {
        postRepository.saveAndFlush(
                Post.builder()
                        .id(postData.getId())
                        .title(postData.getTitle())
                        .body(postData.getBody())
                        .author(postData.getAuthor())
                        .wordCount(postData.getWordCount())
                        .calculatedValue(postData.getCalculatedValue())
                        .build()
        );
    }
}