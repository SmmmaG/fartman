package ru.iia.fartman.site.services.jms.listener.executors;

/**
 * Интерфейс обработчкиа сообщения из очереди
 *
 * @author IvIsmakaev
 */
public interface IJMSMessageConsumer<T extends Object> {
    /**
     * Выполнение бизнес логики обработки сообщения
     *
     * @param messageValue
     *         объект тела сообщения
     */
    void execute(T messageValue);

    /**
     * получение имени обработчика
     *
     * @return имя обработчки
     */
    String getName();

    /**
     * получение ограничения обработчика
     *
     * @return ограничения обработчика
     */
    String getRestriction();
}
