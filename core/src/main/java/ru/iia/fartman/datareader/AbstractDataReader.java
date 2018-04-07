package ru.iia.fartman.datareader;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.iia.fartman.orm.entity.DataEntity;

import java.util.List;

public abstract class AbstractDataReader {
	public abstract List<DataEntity> read(HtmlPage page);
}
