package ru.iia.fartman.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.iia.fartman.site.services.LigaTask;

import java.util.Timer;


public class Start {

    private static final Logger logger = LogManager.getLogger(Start.class);

    public static void main(String... args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("ru.iia");
        Timer time = new Timer();
        LigaTask st = new LigaTask(context);
        time.schedule(st, 0, 1000000);
        logger.debug("end all ");
    }
}
