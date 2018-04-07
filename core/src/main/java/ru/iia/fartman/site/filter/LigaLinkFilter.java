package ru.iia.fartman.site.filter;

import ru.iia.fartman.orm.entity.Link;

public class LigaLinkFilter extends AbstractLinkFilter {
	@Override
	protected boolean doFilter(Link link) {
		if (link.getLink().contains("www.ligastavok.ru")) {
			if(link.getLink().contains("/bets/bets/")){
				link.setLink(link.getLink().replace("/bets/bets/","/bets/"));
			}
			return true;
		}
		return false;
	}
}
