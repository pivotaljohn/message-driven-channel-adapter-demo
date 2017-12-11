package io.pivotal.labs.la.keychest.messagedrivenchanneladapterdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

import static io.pivotal.labs.la.keychest.messagedrivenchanneladapterdemo.MessageDrivenChannelAdapterDemoApplication.INBOUND_QUEUE_NAME;
import static java.lang.String.format;

@Component
public class OrderTaker {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void generateOrders() {
        IntStream.range(0, 100_000)
                .forEach((i) -> jmsTemplate.convertAndSend(INBOUND_QUEUE_NAME, format("Order #%d", i)));
    }

}
