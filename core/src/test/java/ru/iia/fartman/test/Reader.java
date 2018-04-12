package ru.iia.fartman.test;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import ru.iia.fartman.orm.entity.DataEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Reader {
    public static final void getpage(String url, String fileName) throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            WebRequest request = new WebRequest(new URL(url));
            request.setEncodingType(FormEncodingType.URL_ENCODED);
            request.setHttpMethod(HttpMethod.GET);

            request.setCharset(Charset.forName("windows-1251"));
            final HtmlPage page = webClient.getPage(request);
            String title = page.getTitleText();
            String pageAsXml = page.asXml();
            saveToFile(pageAsXml, new File("C:/temp/out/" +
                    new Date().toLocaleString().replace(' ', 'd').
                            replace(':', 'd').replace('.', 'd')
                    + new Date().hashCode() + fileName));

            List<HtmlAnchor> anchors = page.getAnchors();
            List<String> listTowrite = new ArrayList<>();
            for (HtmlAnchor anchor : anchors) {
                listTowrite.add(page.getBaseURI() + "/" + anchor.getHrefAttribute());
            }
            saveToFile(listTowrite, new File("C:/temp/out/" +
                    new Date().toLocaleString().replace(' ', 'd').
                            replace(':', 'd').replace('.', 'd')
                    + new Date().hashCode() + "_anchors_" + fileName + ".txt"));

            listTowrite = new ArrayList<>();
            List<DomElement> inputss = page.getElementsByName("theSportsTable");
            for (DomElement element : inputss) {
                listTowrite.add(element.getTextContent());
            }
            saveToFile(listTowrite, new File("C:/temp/out/" +
                    new Date().toLocaleString().replace(' ', 'd').
                            replace(':', 'd').replace('.', 'd')
                    + new Date().hashCode() + "_theSportsTable_" + fileName + ".txt"));


            List<DomElement> elements = page.getElementsByTagName("tr");
            elements.forEach(domElement -> {
                if (domElement.hasAttribute("class")
                        && "open-optional".equals(domElement.getAttribute("class"))) {
                    DomNodeList<DomNode> childs = domElement.getChildNodes();
                    System.out.println(childs.get(0).asText());
                    System.out.println(childs.get(1).asText());
                    System.out.println(childs.get(2).asText());
                    System.out.println(childs.get(3).asText());
                    System.out.println(childs.get(4).asText());
                    System.out.println(childs.get(5).asText());
                }
            });

            try {
                List<DataEntity> result = new ArrayList<>();
                List<DomElement> elements1 = page.getElementsByTagName("table");
                for (DomElement domElement : elements1) {
                    if (domElement.hasAttribute("class")
                            && "line topic__table".equals(domElement.getAttribute("class"))) {
                        List<HtmlElement> elementsTr = domElement.getElementsByTagName("tr");
                        for (DomElement domElementTr : elementsTr) {
                            System.out.println(domElementTr.asText());
                            DomNodeList<DomNode> childs = domElementTr.getChildNodes();
                            if (childs.size() > 5 && "td".equals(childs.get(0).getNodeName())) {
                                DataEntity resultEl = DataEntity.newDataEntity();
                                resultEl.setAdditionalNumber(childs.get(0).asText());
                                resultEl.setDateString(childs.get(1).asText());
                                resultEl.setNameString(childs.get(2).asText());
                                resultEl.setwFirstString(childs.get(3).asText());
                                resultEl.setDrawString(childs.get(4).asText());
                                resultEl.setwSecondString(childs.get(5).asText());
                                result.add(resultEl);
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    private static void saveToFile(String pageContenet, File file) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            out.write(pageContenet);
        } catch (IOException e) {
            System.out.println("Exception ");

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("Exception ");

                }
            }
        }

    }

    private static void saveToFile(List<String> pageContenet, File file) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            for (String string : pageContenet) {
                out.write(string);
                out.newLine();
            }
        } catch (IOException e) {
            System.out.println("Exception ");

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("Exception ");

                }
            }
        }

    }


    public static void main(String... args) {
/*
		try {
			getpage("https://www.ligastavok.ru/bets/soccer/sbornye-chempionat-mira-2018-gruppovoi-etap-id-50290/14-06-2018-18-00-id-8379313-rossiia-saudovskaia-araviia", "ligastavok.html");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
        try {
            getpage("https://www.ligastavok.ru/bets/soccer/rossiia-premer-liga-maksimalnye-vyplaty-id-10581", "ligastavok1.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
		/*try {
			getpage("https://www.ligastavok.ru", "ligastavok.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getpage("https://www.leon.ru/", "leon.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getpage("https://winline.ru/", "winline.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getpage("https://betcity.ru/ru/", "betcity.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/

    }
}
