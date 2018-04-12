package ru.iia.fartman.site.filter;

import org.springframework.stereotype.Service;
import ru.iia.fartman.orm.entity.Link;

@Service
public class LigaLinkFilter extends AbstractLinkFilter {
	@Override
	protected boolean doFilter(Link link) {
		String link1 = link.getLink();
		if (false && link1.contains("www.ligastavok.ru")) {
			if (link1.contains("@") || link1.contains("#")) {
				return false;
			}
			if (link1.contains("/Live") || link1.contains("/Registration")) {
				return false;
			}
			if (link1.contains("/bets/bets/")) {
				link.setLink(link1.replace("/bets/bets/", "/bets/"));
			}
			return true;
		}
		return false;
	}
}
