package ru.iia.fartman.site.services;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.iia.fartman.datareader.IDataReader;
import ru.iia.fartman.orm.entity.DataEntity;
import ru.iia.fartman.orm.entity.Link;
import ru.iia.fartman.site.filter.AbstractLinkFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

/**
 * Page reader class implements logic of reading web page
 */
@Component
public class PageReader implements Runnable {

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
    private String name;
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
    private QueueUrls queue;
    /**
     *
     */
    private String baseURI;
    /**
     *
     */
    private IDataReader dataReader;

    private String threadName;
    private boolean isWorking;

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
        this.queue = queue;
        this.filter = filter;
        this.dataReader = dataReader;
        this.isWorking = false;
        queue.startWorkThread(this);

    }

    public void init(AbstractLinkFilter filter, IDataReader dataReader, QueueUrls queue, String baseURI) {
        this.baseURI = baseURI;
        this.queue = queue;
        this.filter = filter;
        this.dataReader = dataReader;
        this.isWorking = false;
        queue.startWorkThread(this);

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

    @Override
    public void run() {
        try {
            while (queue.isEmptyThreads() || queue.isWorking(getThreadName())) {
                isWorking = true;
                Link currentLink = queue.getNextUrl();

                HtmlPage page = getPage(currentLink.getLink(), null);
                if (page != null) {
                    List<DataEntity> dataEntity = dataReader.read(page, currentLink);
                    List<HtmlAnchor> list = page.getAnchors();

                    for (HtmlAnchor anchor : list) {
                        try {
                            if (!anchor.getHrefAttribute().contains("http:") && !anchor.getHrefAttribute().contains("https:")) {
                                Link link = Link.createLink(baseURI, anchor.getHrefAttribute());
                                if (link != null && filter.filter(link)) {
                                    if (queue.addUrl(link)) {
                                        logger.debug("link: " + link.getLink());
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            logger.error("anchor:" + anchor.toString(), e);
                        }
                    }
			/*List<HtmlLink> listLink = page.();
			 for (HtmlLink anchor : list) {
			 Link link = Link.createLink(baseURI, anchor.getHrefAttribute());
			 if (link != null && filter.filter(link)) {
			 queue.addUrl(link);
			 }
			 }
			 */

                }
                isWorking = false;
                if (queue.isEmptyThreads()) {
                    break;
                }
            }
        } catch (Throwable e){
            logger.error("",e);
        }finally {
            queue.endThread(getThreadName());
        }
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public boolean isWorking() {
        return isWorking;
    }
}
