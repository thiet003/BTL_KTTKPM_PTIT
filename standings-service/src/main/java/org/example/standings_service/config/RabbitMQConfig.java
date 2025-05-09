package org.example.standings_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

// Config RabbitMQ
@Configuration
public class RabbitMQConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public TopicExchange f1Exchange() {
        return new TopicExchange("f1-exchange");
    }

    @Bean
    public Queue raceResultsQueue() {
        return new Queue("race-results-queue", true);
    }

    @Bean
    public Queue driverInfoQueue() {
        return new Queue("driver-info-queue", true);
    }

    @Bean
    public Queue teamInfoQueue() {
        return new Queue("team-info-queue", true);
    }

    @Bean
    public Binding raceResultsBinding(Queue raceResultsQueue, TopicExchange f1Exchange) {
        return BindingBuilder.bind(raceResultsQueue).to(f1Exchange).with("race.results.updated");
    }

    @Bean
    public Binding driverInfoBinding(Queue driverInfoQueue, TopicExchange f1Exchange) {
        return BindingBuilder.bind(driverInfoQueue).to(f1Exchange).with("driver.info.updated");
    }

    @Bean
    public Binding teamInfoBinding(Queue teamInfoQueue, TopicExchange f1Exchange) {
        return BindingBuilder.bind(teamInfoQueue).to(f1Exchange).with("team.info.updated");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * Singleton pattern implementation for RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}