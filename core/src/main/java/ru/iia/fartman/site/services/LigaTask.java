package ru.iia.fartman.site.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import ru.iia.fartman.datareader.IDataReader;
import ru.iia.fartman.datareader.LigaDataReader;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.entity.Link;
import ru.iia.fartman.orm.repositories.ExecuteStartRepository;
import ru.iia.fartman.site.filter.AbstractLinkFilter;
import ru.iia.fartman.site.filter.LigaLinkFilter;

import java.util.Date;
import java.util.TimerTask;

public class LigaTask extends TimerTask {

    private ApplicationContext context;
    private static final Logger logger = LogManager.getLogger(LigaTask.class);


    public LigaTask(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            QueueUrls queueUrls = new QueueUrls();
            queueUrls.addUrl(Link.createLink("https://www.ligastavok.ru"));
            ExecuteStart executeStart = new ExecuteStart();
            executeStart.setDate(new Date());
            try {
                ExecuteStartRepository dataRepository = context.getBean(ExecuteStartRepository.class);
                dataRepository.save(executeStart);
            } catch (Throwable ex) {
                logger.error("save to data base", ex);
            }
            AbstractLinkFilter linkFilter = context.getBean(LigaLinkFilter.class);
            IDataReader dataReader = context.getBean(LigaDataReader.class);
            for (int i = 0; i < 1; i++) {
                logger.debug("start liga" + i);
                PageReader pageReader = new PageReader(linkFilter, dataReader, queueUrls, "https://www.ligastavok.ru/bets");
                try {
                    Environment env = context.getBean(Environment.class);
                    if (env.getProperty("proxy.url") != null) {
                        pageReader.initProxy(env.getProperty("proxy.url"));
                    } else if (env.getProperty("proxy.host") != null && env.getProperty("proxy.port") != null
                            && env.getProperty("proxy.isSocks") != null) {
                        pageReader.initProxy(env.getProperty("proxy.host"),
                                Integer.parseInt(env.getProperty("proxy.port")),
                                Boolean.parseBoolean(env.getProperty("proxy.host")));
                    }
                } catch (Exception ex) {
                    logger.error("error proxy init", ex);
                }
                pageReader.setThreadName("liga" + i);
                Thread thread = new Thread(pageReader, "liga");
                queueUrls.startThread(pageReader.getThreadName());
                thread.setDaemon(true);
                thread.start();
            }
            while (queueUrls.isWorking("")) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {

        }
    }
}
