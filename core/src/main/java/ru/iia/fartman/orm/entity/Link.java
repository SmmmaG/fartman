package ru.iia.fartman.orm.entity;

import org.apache.commons.validator.UrlValidator;

/**
 * Class link for using in application
 */
public class Link {
	/**
	 * link string
	 */
	private String link;

	/**
	 * Get http link string
	 *
	 * @return string link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Set http link
	 *
	 * @param link link string
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Create link from array of link parts
	 *
	 * @param linkArgs list of link parts
	 * @return link string
	 */
	public static Link createLink(String... linkArgs) {
		Link link = new Link();
		StringBuffer sb = new StringBuffer();
		for (String str : linkArgs) {
			sb.append(str);
		}
		String[] schemes = {"http", "https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (true || urlValidator.isValid(sb.toString())) {
			link.setLink(sb.toString());
			return link;
		}
		return null;
	}
}
