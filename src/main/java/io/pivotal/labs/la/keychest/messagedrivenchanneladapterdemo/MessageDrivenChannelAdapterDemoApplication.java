package io.pivotal.labs.la.keychest.messagedrivenchanneladapterdemo;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.jms.Queue;

@SpringBootApplication
@EnableIntegration
public class MessageDrivenChannelAdapterDemoApplication {

    public static final String INBOUND_QUEUE_NAME = "inbound";
    public static final String INCOMING_ORDERS_CHANNEL_NAME = "incoming-orders";

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(MessageDrivenChannelAdapterDemoApplication.class)
                .web(false)
                .run(args);

        app.getBean(OrderTaker.class).generateOrders();
    }

    @Bean
    public Queue inboundQueue() {
        return new ActiveMQQueue(INBOUND_QUEUE_NAME);
    }

    @Bean
    public IntegrationFlow processIncomingOrders() {
        return IntegrationFlows.from(INCOMING_ORDERS_CHANNEL_NAME)
                .handle((message) -> {
                    try {
                        Thread.currentThread().sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(message.getPayload());
                })
                .get();
    }
}