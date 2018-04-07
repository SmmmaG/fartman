package ru.iia.fartman.site.filter;

import ru.iia.fartman.orm.entity.Link;

/**
 * Abstract filter for links. It used  for filtering links for your purposes
 */
public abstract class AbstractLinkFilter {

	/**
	 * filter link
	 *
	 * @param link link object
	 * @return
	 */
	public boolean filter(Link link) {
		if (null != link && null != link.getLink() && link.getLink().startsWith("http") && link.getLink().length() > 5) {
			return doFilter(link);
		}
		return false;
	}

	protected abstract boolean doFilter(Link link);

}
