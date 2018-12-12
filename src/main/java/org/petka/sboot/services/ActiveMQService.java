package org.petka.sboot.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Logger logger;

    public void send(String message) {
        logger.info("Sending message {}.", message);
        jmsTemplate.convertAndSend("mailbox", message);
    }

    @JmsListener(destination = "mailbox")
    private void receive(String message) {
        logger.info("Received {}.", message);
    }
}
