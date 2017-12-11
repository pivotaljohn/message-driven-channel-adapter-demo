package io.pivotal.labs.la.keychest.messagedrivenchanneladapterdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;

import static io.pivotal.labs.la.keychest.messagedrivenchanneladapterdemo.MessageDrivenChannelAdapterDemoApplication.*;
import static io.pivotal.labs.la.keychest.messagedrivenchanneladapterdemo.MessageDrivenChannelAdapterDemoApplication.INBOUND_QUEUE_NAME;

@Configuration
public class InboundQueueToOrderChannelConfig {

    @Autowired
    private ConnectionFactory jmsConnectionFactory;
    @Autowired
    private JmsProperties jmsProperties;

    @Bean
    public IntegrationFlow fromInboundQueueToIncomingOrderChannel() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(jmsConnectionFactory)
                        .destination(INBOUND_QUEUE_NAME)
                        .configureListenerContainer(c -> {
                                    DefaultMessageListenerContainer defaultMessageListenerContainer = c.get();
                                    defaultMessageListenerContainer.setSessionTransacted(true);
                                    defaultMessageListenerContainer.setConcurrentConsumers(jmsProperties.getListener().getConcurrency());
                                    defaultMessageListenerContainer.setMaxConcurrentConsumers(jmsProperties.getListener().getMaxConcurrency());
                                }
                        )
                )
                .channel(INCOMING_ORDERS_CHANNEL_NAME)
//                .errorChannel()
//                .jmsMessageConverter()
                .get();
    }

}
