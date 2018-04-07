package ru.iia.fartman.datareader;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.iia.fartman.orm.entity.DataEntity;

import java.util.List;

public interface IDataReader {
	List<DataEntity> read(HtmlPage page);
}
