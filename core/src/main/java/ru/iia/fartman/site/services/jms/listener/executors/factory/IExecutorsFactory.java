package ru.iia.fartman.site.services.jms.listener.executors.factory;

import ru.iia.fartman.site.services.jms.listener.executors.IJMSMessageConsumer;

import java.util.Set;

/**
 * @author IvIsmakaev
 */
public interface IExecutorsFactory {
    /**
     * Получение списка обработчиков загеристрированных в фабрике
     *
     * @return Список обработчиков
     */
    Set<IJMSMessageConsumer> getExcutorslist();

    /**
     * Получение списка имен обработчиков загеристрированных в фабрике
     *
     * @return Список имен обработчиков
     */
    Set<String> getExecutorsNames();

    /**
     * Добавление обработчки в фабрику
     *
     * @param executor
     *         обработчик
     */
    void addMessageExecutor(IJMSMessageConsumer executor);

    /**
     * Получение обработчика загеристрированных в фабрике по имени
     *
     * @param handlerName
     *         имя обработчика, если обработчика с таким имененм нет или имя равно null, то возвращает null
     * @return обработчик
     */
    IJMSMessageConsumer getByName(String handlerName);
}
