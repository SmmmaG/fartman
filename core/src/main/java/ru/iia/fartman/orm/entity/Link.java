package ru.iia.fartman.orm.entity;

import org.apache.commons.validator.UrlValidator;

import javax.persistence.*;

/**
 * Class link for using in application
 */
@Entity
public class Link {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;
    /**
     * link string
     */
    @Column(name = "link", length = 4096)
    private String link;

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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                || (obj instanceof Link && ((Link) obj).link.equals(link));
    }

    public Long getUuid() {
        return uuid;
    }

    protected void setUuid(Long uuid) {
        this.uuid = uuid;
    }

}
