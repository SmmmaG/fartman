package ru.iia.fartman.site.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.iia.fartman.datareader.IDataReader;
import ru.iia.fartman.site.filter.AbstractLinkFilter;

public abstract class AbstractConfig<T extends AbstractLinkFilter, W extends IDataReader> {
	private static final Logger logger = LogManager.getLogger(AbstractConfig.class);

	protected abstract Class<T> getFilterClass();

	protected abstract Class<W> getDataReaderClass();


	protected abstract String getStartLinkString();

	protected abstract String getThreadsName();


}
