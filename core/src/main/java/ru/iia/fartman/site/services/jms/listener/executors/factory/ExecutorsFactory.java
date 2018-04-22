package ru.iia.fartman.site.services.jms.listener.executors.factory;


import ru.iia.fartman.site.services.jms.listener.executors.IJMSMessageConsumer;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Фабрика исполнителей обработчиков ссообщений JMS
 *
 * @author IvIsmakaev
 */
public class ExecutorsFactory implements IExecutorsFactory {
    /**
     * Сет имен исполнителей бизнес логики
     */
    Set<String> names;
    /**
     * Сет исполнителей бизнес логики по сообщениям из очередей
     */
    Set<IJMSMessageConsumer> executors;

    /**
     * Конструктор.
     *
     *  Пример использования:
     *
     *  <bean id="ru.ga.jms.server.executors.factory.IExecutorsFactory"
     *      class="ru.ga.jms.listener.executors.factory.ExecutorsFactory" scope="singleton">
     *      <constructor-arg index="0">
     *          <!-- Сет бинов обработчиков обязательный аргумент -->
     *          <set>
     *              <ref bean="BeanExecutor"/>
     *              ...
     *          </set>
     *      </constructor-arg>
     *  </bean>
     *  <bean id="BeanExecutor" .../>
     *
     *
     * @param executorSet Сет бинов обработчиков
     */
    public ExecutorsFactory(Set<IJMSMessageConsumer> executorSet) {
        executors = executorSet;
        names = new LinkedHashSet();
        for (IJMSMessageConsumer executor : executorSet) {
            names.add(executor.getName());
        }
    }

    @Override
    public Set<IJMSMessageConsumer> getExcutorslist() {
        return executors;
    }

    @Override
    public Set<String> getExecutorsNames() {
        return names;
    }

    @Override
    public void addMessageExecutor(IJMSMessageConsumer executor) {
        names.add(executor.getName());
        executors.add(executor);
    }

    @Override
    public IJMSMessageConsumer getByName(String handlerName) {
        if (handlerName != null) {
            for (IJMSMessageConsumer executor : executors) {
                if (handlerName.equals(executor.getName())) {
                    return executor;
                }
            }
        }
        return null;
    }
}
