package ru.iia.fartman.datareader;

import com.gargoylesoftware.htmlunit.html.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.iia.fartman.orm.entity.DataEntity;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.entity.Link;
import ru.iia.fartman.orm.repositories.DataEntityRepository;
import ru.iia.fartman.orm.repositories.LinkRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LigaDataReader implements ILigaDataReader {

    Logger logger = LogManager.getLogger(LigaDataReader.class);
    @Autowired
    DataEntityRepository dataRepository;
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    ApplicationContext context;
    private ExecuteStart start;

    @Override
    public List<DataEntity> read(HtmlPage page, Link link) {
        try {
            List<DataEntity> result = new ArrayList<>();
            try {
                linkRepository.save(link);
            } catch (Throwable ex) {
                logger.error("save to data base", ex);
            }
            DataEntity resTr = trData(page, link);
            if (resTr != null) {
                result.add(resTr);
            }
            getTableData(page, result, link);
        } catch (Throwable e) {
            logger.error("", e);
        }
        return null;
    }

    private void getTableData(HtmlPage page, List<DataEntity> result, Link link) {
        List<DomElement> elements1 = page.getElementsByTagName("table");
        for (DomElement domElement : elements1) {
            if (domElement.hasAttribute("class")
                    && "line topic__table".equals(domElement.getAttribute("class"))) {
                List<HtmlElement> elementsTh = domElement.getElementsByTagName("th");
                String currentDate = "" + elementsTh.get(0).asText();
                List<HtmlElement> elementsTr = domElement.getElementsByTagName("tr");
                for (DomElement domElementTr : elementsTr) {
                    DomNodeList<DomNode> childs = domElementTr.getChildNodes();
                    if (childs.size() > 5 && "td".equals(childs.get(0).getNodeName())) {
                        DataEntity resultEl = DataEntity.newDataEntity();
                        resultEl.setAdditionalNumber(childs.get(0).asText());
                        resultEl.setDateString(currentDate + " time " + childs.get(1).asText());
                        resultEl.setNameString(childs.get(2).asText());
                        resultEl.setwFirstString(childs.get(3).asText());
                        resultEl.setDrawString(childs.get(4).asText());
                        resultEl.setwSecondString(childs.get(5).asText());
                        resultEl.setResource(link);
                        resultEl.setStart(start);
                        initCosts(resultEl);
                        result.add(resultEl);
                        logger.trace(resultEl.toString());
                        try {
                            dataRepository.save(resultEl);
                        } catch (Throwable ex) {
                            logger.error("save to data base", ex);
                        }
                    }
                }
            }
        }
    }

    private void initCosts(DataEntity resultEl) {
        try {
            double f = Double.parseDouble(resultEl.getwFirstString().trim());
            double s = Double.parseDouble(resultEl.getwFirstString().trim());
            double d = Double.parseDouble(resultEl.getwFirstString().trim());
            if (f != 0) {
                resultEl.setwFirst(f / (f + s + d));
            }
            if (s != 0) {
                resultEl.setwFirst(s / (f + s + d));
            }
            if (d != 0) {
                resultEl.setwFirst(d / (f + s + d));
            }
        } catch (Throwable ex) {
            logger.error("save to data base", ex);
        }
    }

    private DataEntity trData(HtmlPage page, Link link) {
        DataEntity result = DataEntity.newDataEntity();
        List<DomElement> elements = page.getElementsByTagName("tr");
        elements.forEach(domElement -> {
            if (domElement.hasAttribute("class")
                    && "open-optional".equals(domElement.getAttribute("class"))) {
                DomNodeList<DomNode> childs = domElement.getChildNodes();
                if (childs.size() > 5) {
                    result.setAdditionalNumber(childs.get(0).asText());
                    result.setDateString(childs.get(1).asText());
                    result.setNameString(childs.get(2).asText());
                    result.setwFirstString(childs.get(3).asText());
                    result.setDrawString(childs.get(4).asText());
                    result.setwSecondString(childs.get(5).asText());
                    initCosts(result);
                    result.setResource(link);
                    result.setStart(start);
                    logger.trace(result.toString());
                    dataRepository.save(result);
                }
            }
        });
        return result;
    }

    public ExecuteStart getStart() {
        return start;
    }

    public void setStart(ExecuteStart start) {
        this.start = start;
    }
}
