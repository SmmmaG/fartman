package ru.iia.fartman.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.iia.fartman.datareader.AbstractDataReader;
import ru.iia.fartman.datareader.LigaDataReader;
import ru.iia.fartman.orm.entity.Link;
import ru.iia.fartman.site.filter.AbstractLinkFilter;
import ru.iia.fartman.site.filter.LigaLinkFilter;
import ru.iia.fartman.site.services.PageReader;
import ru.iia.fartman.site.services.QueueUrls;


public class Strat {

	private static final Logger logger = LogManager.getLogger(Strat.class);

	public static void main(String... args) {
		try {
			QueueUrls queueUrls = new QueueUrls();
			queueUrls.addUrl(Link.createLink("https://www.ligastavok.ru"));
			AbstractLinkFilter linkFilter = new LigaLinkFilter();
			AbstractDataReader dataReader = new LigaDataReader();
			for (int i = 0; i < 50; i++) {
				logger.debug("start liga" + i);
				PageReader pageReader = new PageReader(linkFilter, dataReader, queueUrls, "https://www.ligastavok.ru/bets");
				pageReader.setThreadName("liga" + i);
				Thread thread = new Thread(pageReader, "liga");
				queueUrls.startThread(pageReader.getThreadName());
				thread.setDaemon(true);
				thread.start();
			}
			while (queueUrls.isWorking()) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException ex) {

		}
		logger.debug("end all ");
	}
}
