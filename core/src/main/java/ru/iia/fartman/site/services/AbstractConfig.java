package ru.iia.fartman.site.services;

import ru.iia.fartman.datareader.IDataReader;
import ru.iia.fartman.site.filter.AbstractLinkFilter;

public abstract class AbstractConfig<T extends AbstractLinkFilter, W extends IDataReader> {

	protected abstract Class<T> getFilterClass();

	protected abstract Class<W> getDataReaderClass();

	protected abstract String getStartLinkString();

	protected abstract String getThreadsName();

}
