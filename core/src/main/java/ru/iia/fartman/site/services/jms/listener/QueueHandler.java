package ru.iia.fartman.site.services.jms.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iia.fartman.site.services.jms.JMSSenderService;
import ru.iia.fartman.site.services.jms.listener.executors.IJMSMessageConsumer;
import ru.iia.fartman.site.services.jms.listener.executors.factory.IExecutorsFactory;

import javax.jms.MessageFormatException;
import java.util.Map;

/**
 * Абстрактный обработчки очереди
 *
 * @author IvIsmakaev
 */
public abstract class QueueHandler implements IMessageQueueHandler {
	/**
	 * logger used  for logging information for diagnostic
	 */
	private static final Logger logger = LogManager.getLogger(JMSSenderService.class);


	@Autowired
	IExecutorsFactory executorsFactory;

	/**
	 * Получение имени обработчика
	 *
	 * @return имя обработчика
	 */
	public abstract String getHandlerName();

	@Override
	public abstract String getSelector();

	@Override
	public void listen(Object obj) throws MessageFormatException {
		if (obj instanceof Map) {
			Map<String, Object> map = (Map) obj;
			String handlerName = getHandlerName();
			if (executorsFactory.getExecutorsNames().contains(handlerName)) {
				IJMSMessageConsumer<Map> executor = executorsFactory.getByName(handlerName);
				logger.info("Executor {0} for handler name {1}", executor.getClass().getName(), handlerName);
				StringBuilder sb = new StringBuilder();
				for (Object object : map.keySet()) {
					if (sb.length() > 1) {
						sb.append(",");
					} else {
						sb.append("Map ");
					}
					sb.append(object.toString()).append(":").append(map.get(object).toString());
				}
				logger.info(sb.toString());
				executor.execute(map);
			} else {
				MessageFormatException ex = new MessageFormatException("There are not executor for this type message" +
						" with handler : " + handlerName);
				logger.error("", ex);
				throw ex;
			}
		} else {
			MessageFormatException ex = new MessageFormatException("There are not executor for this type message: "
					+ obj.getClass());
			logger.error("", ex);
			throw ex;
		}
	}
}
