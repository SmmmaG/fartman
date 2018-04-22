package ru.iia.fartman.site.services.jms.listener;

import javax.jms.MessageFormatException;

/**
 * Интерфейс обработчика сообщений из очереди
 *
 * @author IvIsmakaev
 */
public interface IMessageQueueHandler {
    /**
     * метод слушателя сообщения
     *
     * @param obj
     *         тело сообщения из очереди
     * @throws MessageFormatException
     *         ошибки формата сообщения - не соответствие тела сообщения обработчку
     */
    void listen(Object obj) throws MessageFormatException;

    String getSelector();
}
