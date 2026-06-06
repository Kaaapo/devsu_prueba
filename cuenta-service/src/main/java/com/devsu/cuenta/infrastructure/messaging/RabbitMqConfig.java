package com.devsu.cuenta.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange.cliente}")
    private String clienteExchange;

    @Value("${rabbitmq.queue.cliente}")
    private String clienteQueue;

    @Value("${rabbitmq.routing-key.cliente}")
    private String routingKey;

    @Bean
    public TopicExchange clienteExchange() {
        return new TopicExchange(clienteExchange);
    }

    @Bean
    public Queue clienteQueue() {
        return new Queue(clienteQueue, true);
    }

    @Bean
    public Binding clienteBinding(Queue clienteQueue, TopicExchange clienteExchange) {
        return BindingBuilder.bind(clienteQueue).to(clienteExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
