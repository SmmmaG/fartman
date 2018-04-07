package ru.iia.fartman.datareader;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.iia.fartman.orm.entity.DataEntity;

import java.util.ArrayList;
import java.util.List;

public class LigaDataReader extends AbstractDataReader {

	Logger logger = LogManager.getLogger(LigaDataReader.class);

	@Override
	public List<DataEntity> read(HtmlPage page) {
		try {
			List<DataEntity> result = new ArrayList<>();
			DataEntity resTr = trData(page);
			if (resTr != null) {
				result.add(resTr);
			}
			getTableData(page, result);
		} catch (Throwable e) {
			logger.error("", e);
		}
		return null;
	}

	private void getTableData(HtmlPage page, List<DataEntity> result) {
		List<DomElement> elements1 = page.getElementsByTagName("table");
		for (DomElement domElement : elements1) {
			if (domElement.hasAttribute("class")
					&& "line topic__table".equals(domElement.getAttribute("class"))) {
				List<HtmlElement> elementsTr = domElement.getElementsByTagName("tr");
				for (DomElement domElementTr : elementsTr) {
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
						logger.trace(resultEl.toString());
					}
				}
			}
		}
	}

	private DataEntity trData(HtmlPage page) {
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
					logger.trace(result.toString());
				}
			}
		});
		return result;
	}
}
