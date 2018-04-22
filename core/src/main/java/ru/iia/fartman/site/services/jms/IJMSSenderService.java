package ru.iia.fartman.site.services.jms;

import org.springframework.jms.core.JmsTemplate;

import java.util.Map;

public interface IJMSSenderService {
	void setJmsTemplate(JmsTemplate jmsTemplate);

	JmsTemplate getJmsTemplate();

	void sendObject(String queueName, Object messageBody, String messageType);

	void sendObject(String queueName, Object messageBody, Map<String, Object> messageHeader);
}
