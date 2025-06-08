package com.rafaelsousa.post.service.api.infra.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
    private static final String POST_PROCESSING_PREFIX_QUEUE = "text-processor-service.post-processing.v1";
    public static final String POST_PROCESSING_QUEUE = POST_PROCESSING_PREFIX_QUEUE + ".q";
    public static final String POST_PROCESSING_DEAD_LETTER_QUEUE = POST_PROCESSING_PREFIX_QUEUE + ".dlq";

    private static final String POST_PROCESSING_RESULT_PREFIX_QUEUE = "post-service.post-processing-result.v1";
    public static final String POST_PROCESSING_RESULT_QUEUE = POST_PROCESSING_RESULT_PREFIX_QUEUE + ".q";

    public static final String FANOUT_EXCHANGE_PROCESS_TEXT = "text-processor.process-text.v1.e";

    public static final String FANOUT_ROUTING_KEY = ""; // Fanout exchanges do not use routing keys

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Queue queueTextProcessorService() {
        Map<String, Object> args = Map.of(
                "x-dead-letter-exchange", "",
                "x-dead-letter-routing-key", POST_PROCESSING_DEAD_LETTER_QUEUE
        );

        return QueueBuilder.durable(POST_PROCESSING_QUEUE)
                .withArguments(args)
                .build();
    }

    @Bean
    public Queue deadLetterQueueTextProcessorService() {
        return QueueBuilder.durable(POST_PROCESSING_DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public FanoutExchange exchangeTextProcessorService() {
        return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE_PROCESS_TEXT).build();
    }
}