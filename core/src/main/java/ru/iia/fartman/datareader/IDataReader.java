package ru.iia.fartman.datareader;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.iia.fartman.orm.entity.DataEntity;
import ru.iia.fartman.orm.entity.ExecuteStart;
import ru.iia.fartman.orm.entity.Link;

import java.util.List;

public interface IDataReader {
	List<DataEntity> read(HtmlPage page, Link link);

	ExecuteStart getStart();

	void setStart(ExecuteStart start);
}
