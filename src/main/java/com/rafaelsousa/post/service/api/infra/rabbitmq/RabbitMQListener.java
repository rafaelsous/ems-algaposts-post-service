package com.rafaelsousa.post.service.api.infra.rabbitmq;

import com.rafaelsousa.post.service.api.model.PostData;
import com.rafaelsousa.post.service.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.rafaelsousa.post.service.api.infra.rabbitmq.RabbitMQConfig.POST_PROCESSING_RESULT_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {
    private final PostService postService;

    @SuppressWarnings("java:S112")
    @RabbitListener(queues = POST_PROCESSING_RESULT_QUEUE, concurrency = "2-3")
    public void handleTextProcessorService(@Payload PostData postData) {
        log.info("Received post processing result: {}", postData);

        if (postData.getTitle().equalsIgnoreCase("Text update failure test")) {
            throw new RuntimeException(String.format("Failed to persist updated post with ID: %s", postData.getId()));
        }

        postService.updatePostWithProcessingResult(postData);
    }
}