package ru.iia.fartman.site.services.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Map;
import java.util.UUID;

@Service
public class JMSSenderService implements IJMSSenderService {
	/**
	 * logger used  for logging information for diagnostic
	 */
	private static final Logger logger = LogManager.getLogger(JMSSenderService.class);


	@Autowired(required = false)
	private JmsTemplate jmsTemplate;

	@Override
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}


	@Override
	public void sendObject(String queueName, Object messageBody, final String messageType) {
		if (logger.isDebugEnabled()) {
			logger.debug("Send object:" + messageBody.toString() + " to " + queueName + "with header JMSType:"
					+ messageType);
		}
		try {
			jmsTemplate.convertAndSend(queueName, messageBody, new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) throws JMSException {
					message.setJMSType(messageType);
					// на всякий случай добавляем ID для сообщения
					String initID = UUID.randomUUID().toString();
					logger.debug("Sending message via JMS with id = {0}", initID);
					message.setJMSCorrelationID(initID);
					message.setJMSMessageID(initID);
					if (logger.isTraceEnabled()) {
						logger.trace(message.toString());
					}
					return message;
				}
			});
		} catch (JmsException ex) {
			logger.error("", ex);
		}
	}


	@Override
	public void sendObject(String queueName, Object messageBody, final Map<String, Object> messageHeader) {
		if (logger.isDebugEnabled()) {
			logger.debug("Send object:" + messageBody.toString() + " to " + queueName);
		}
		try {
			jmsTemplate.convertAndSend(queueName, messageBody, new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) throws JMSException {
					// инициализация заголовка сообщения
					for (String key : messageHeader.keySet()) {
						message.setObjectProperty(key, messageHeader.get(key));
						if (logger.isDebugEnabled()) {
							logger.debug("Message header value " + key + " : " + messageHeader.get(key));
						}
					}
					// на всякий случай добавляем ID для сообщения
					String initID = UUID.randomUUID().toString();
					logger.debug("Sending message via JMS with id = {0}", initID);
					message.setJMSCorrelationID(initID);
					message.setJMSMessageID(initID);
					if (logger.isTraceEnabled()) {
						logger.trace(message.toString());
					}
					return message;
				}
			});
		} catch (JmsException ex) {
			logger.error("", ex);
		}
	}
}
