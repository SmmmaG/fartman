package ru.iia.fartman.orm.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ExecuteStart {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;
    @Column(name = "dateTime", nullable = false)
    private Date date;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
