package ru.iia.fartman.site.services;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.iia.fartman.datareader.IDataReader;
import ru.iia.fartman.orm.entity.Link;
import ru.iia.fartman.orm.repositories.LinkRepository;
import ru.iia.fartman.site.filter.AbstractLinkFilter;
import ru.iia.fartman.site.services.jms.IJMSSenderService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Page reader class implements logic of reading web page
 */
@Component
public class PageReader {

	/**
	 * logger used  for logging information for diagnostic
	 */
	private static final Logger logger = LogManager.getLogger(PageReader.class);
	/**
	 * default locale
	 */
	public static Locale DEFAULT_LOCALE = Locale.ENGLISH;
	/**
	 * default encoding for russian internet
	 */
	public static String DEFAULT_ENCODING = "windows-1251";
	/**
	 * web clent - instance of virtual browser
	 */
	private WebClient webClient;
	/**
	 *
	 */
	private AbstractLinkFilter filter;
	/**
	 *
	 */
	private String baseURI;
	/**
	 *
	 */
	private IDataReader dataReader;

	@Autowired
	LinkRepository linkRepository;

	@Autowired
	IJMSSenderService jmsTemplate;

	/**
	 *
	 */
	public PageReader() {
		this(null, null);
	}

	/**
	 * @param locale
	 * @param encoding
	 */
	private PageReader(Locale locale, String encoding) {
		webClient = new WebClient(BrowserVersion.CHROME);
		Locale.setDefault(locale != null ? locale : DEFAULT_LOCALE);

		webClient.getOptions().setCssEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
	}

	/**
	 * @param filter
	 * @param queue
	 * @param baseURI
	 */
	public PageReader(AbstractLinkFilter filter, IDataReader dataReader, QueueUrls queue, String baseURI) {
		this();
		this.baseURI = baseURI;
		this.filter = filter;
		this.dataReader = dataReader;

	}

	public void init(AbstractLinkFilter filter, IDataReader dataReader, QueueUrls queue, String baseURI) {
		this.baseURI = baseURI;
		this.filter = filter;
		this.dataReader = dataReader;

	}

	public void initProxy(String url) {
		webClient.getOptions().getProxyConfig().setProxyAutoConfigUrl(url);
	}

	public void initProxy(String host, int port, boolean isSocks) {
		webClient.getOptions().getProxyConfig().setSocksProxy(isSocks);
		webClient.getOptions().getProxyConfig().setProxyHost(host);
		webClient.getOptions().getProxyConfig().setProxyPort(port);
	}

	/**
	 * Method gets web html page by link string
	 *
	 * @param link     link to page
	 * @param encoding client encoding
	 * @return html page object
	 */
	public HtmlPage getPage(String link, String encoding) {
		try {
			logger.debug(" get page " + link);
			WebRequest request = new WebRequest(new URL(link));
			request.setEncodingType(FormEncodingType.URL_ENCODED);
			request.setHttpMethod(HttpMethod.GET);
			request.setCharset(Charset.forName(encoding != null ? encoding : DEFAULT_ENCODING));
			HtmlPage page = webClient.getPage(request);
			return page;
		} catch (FailingHttpStatusCodeException ex) {
			logger.error("", ex);
		} catch (MalformedURLException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}


	public void doReadLink(Link currentLink) {
		currentLink.setStarted(true);
		linkRepository.save(currentLink);
		HtmlPage page = getPage(currentLink.getLink(), null);
		if (page != null) {
			dataReader.read(page, currentLink);
			List<HtmlAnchor> list = page.getAnchors();
			for (HtmlAnchor anchor : list) {
				try {
					if (!anchor.getHrefAttribute().contains("http:") && !anchor.getHrefAttribute().contains("https:")) {
						Link link = Link.createLink(baseURI, anchor.getHrefAttribute());
						if (link != null && filter.filter(link)) {
							List<Link> links = linkRepository.findByExecuteStartAndLinkstringAndStarted(currentLink.getStart(), link.getLink(), true);
							if (links.isEmpty()) {
								link.setStarted(false);
								linkRepository.save(link);
								jmsTemplate.sendObject("", new Object(), new HashMap<>());


							}
						}
					}
				} catch (Throwable e) {
					logger.error("anchor:" + anchor.toString(), e);
				}
			}
		}
	}
}
