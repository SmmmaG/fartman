package ru.iia.fartman.site.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.entity.Link;
import ru.iia.fartman.orm.entity.Site;
import ru.iia.fartman.orm.repositories.ExecuteStartRepository;
import ru.iia.fartman.orm.repositories.LinkRepository;
import ru.iia.fartman.orm.repositories.SiteRepository;
import ru.iia.fartman.site.services.jms.IJMSSenderService;

import java.util.Date;

@Service
public class LigaTask {
	private static final Logger logger = LogManager.getLogger(LigaTask.class);
	@Autowired
	ExecuteStartRepository dataRepository;
	@Autowired
	LinkRepository linkRepository;
	@Autowired
	SiteRepository siteRepository;
	@Autowired
	IJMSSenderService jmsSenderService;
	@Value("${taskName.LigaTask}")
	private String taskName;
	@Value("${taskName.queueName}")
	private String queueName;
	@Value("${taskName.messageType}")
	private String messageType;
	public String name = "liga";

	private void addUrl() {
		Site site = siteRepository.findByName(name).get(0);
		if (dataRepository.findBySiteAndStartedAndFinished(site, true, false).isEmpty()) {
			return;
		}
		if (dataRepository.findBySiteAndStartedAndFinished(site, false, false).isEmpty()) {
			return;
		}
		ExecuteStart executeStart = new ExecuteStart();
		executeStart.setDate(new Date());
		executeStart.setFinished(false);
		executeStart.setStarted(false);

		Link toStart = Link.createLink("https://www.ligastavok.ru/bets/soccer/t/10581,36639,8307,10317,13227,10761,10582,50290," +
				"37057,49633,5,7211,9243,7212,51854,7562,15079,7789,8016,2616,7383,34610,2613,9432,7297,7296,34607,2617,39336," +
				"10848,34387,2618,9433,7703,2614,15745,45642,14494,14672,14653,33902,38521,15300,15284,7173,14510,14757,8401," +
				"14631,14399,15222,7204,8413,7862,8056,7861,7467,7513,7131,7132,7133,6911,32863,7224,7226,8553,7225,7223,7134," +
				"7183,10880,7404,7384,45292,38629,38777,8183,14756,7996,10742,38969,39394,38973,39418,38112,39800,8367,39384," +
				"54683,39588,38971,39393,38457,9138,11339,11015,26850,7323,7187,7135,7506,7437,7507,43725,7926,7436,7653,7435," +
				"7778,7509,7607,7780,7901,7335,7419,7438,7503,7209,39398,6874,7127,8109,26583,7674,38747,38746,18154,8065,54670," +
				"7174,20749,8785,44746,7175,17718,17832,7177,10554,7248,17104,8080,7877,19749,37709,53211,7190,8304,11025,7201," +
				"7496,51157,51381,8770,7178,51403,8510,9065,8180,7166,51684,8257,7606,19609,7494,37526,39608,21800,17229,21991," +
				"7261,36995,8558,39525,39523,7416,7415,7650,17524,14759,53573,7712,48445,7652,7711,27816,7925,17847,7197,18194," +
				"17846,17963,18196,7253,10942,39195,8305,37517,7311,7879,14650,8440,7456,7270,7772,7505,54968,7801,32615,7734,32862,52120,43715," +
				"7364,7455,8525,8522,15200,8532,8531,8638,8533,8534,7800,8258,7167,7169,7378,17306,7170,38267,7308,7721,16809," +
				"18067,7554,17970,8079,7768,20540,7741");
		toStart.setStart(executeStart);
		toStart.setStarted(false);
		try {
			dataRepository.save(executeStart);
		} catch (Throwable ex) {
			logger.error("save to data base", ex);
		}
		try {
			linkRepository.save(toStart);
		} catch (Throwable ex) {
			logger.error("save to data base", ex);
		}


		jmsSenderService.sendObject(queueName, toStart, messageType);

		/*for (int i = 0; i < 1; i++) {
			logger.debug("start liga" + i);
			PageReader pageReader = new PageReader(linkFilter, dataReader, "https://www.ligastavok.ru/bets");
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
			}*/

	}
}
