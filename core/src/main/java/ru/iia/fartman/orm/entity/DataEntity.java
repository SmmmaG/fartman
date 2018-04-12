package ru.iia.fartman.orm.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DataEntity {
	@Column(name = "date", length = 128, nullable = false)
	private String dateString;
	@Column(name = "name", length = 128, nullable = false)
	private String nameString;
	@Column(name = "win_first", length = 128, nullable = false)
	private String wFirstString;
	@Column(name = "draw", length = 128, nullable = false)
	private String drawString;
	@Column(name = "win_second", length = 128, nullable = false)
	private String wSecondString;
	@Column(name = "event_number", length = 128, nullable = false)
	private String additionalNumber;
	@ManyToOne
	@JoinColumn(name = "resource")
	private Link resource;
	@ManyToOne
	@JoinColumn(name = "startId")
	private ExecuteStart start;

	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uuid;

	public DataEntity() {

	}

	public static DataEntity newDataEntity() {
		DataEntity result = new DataEntity();
		//result.uuid = UUID.randomUUID();
		return result;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getNameString() {
		return nameString;
	}

	public void setNameString(String nameString) {
		this.nameString = nameString;
	}

	public String getwFirstString() {
		return wFirstString;
	}

	public void setwFirstString(String wFirstString) {
		this.wFirstString = wFirstString;
	}

	public String getDrawString() {
		return drawString;
	}

	public void setDrawString(String drawString) {
		this.drawString = drawString;
	}

	public String getwSecondString() {
		return wSecondString;
	}

	public void setwSecondString(String wSecondString) {
		this.wSecondString = wSecondString;
	}

	public String getAdditionalNumber() {
		return additionalNumber;
	}

	public void setAdditionalNumber(String additionalNumber) {
		this.additionalNumber = additionalNumber;
	}


	public Long getUuid() {
		return uuid;
	}

	protected void setUuid(Long uuid) {
		this.uuid = uuid;
	}


	public String toString() {
		return resource + "::" + uuid + "@" + dateString + ";" + nameString + ";" + wFirstString + ";" + drawString + ";"
				+ wSecondString + ";" + additionalNumber;
	}

	public ExecuteStart getStart() {
		return start;
	}

	public void setStart(ExecuteStart resource) {
		this.start = resource;
	}

	public Link getResource() {
		return resource;
	}

	public void setResource(Link resource) {
		this.resource = resource;
	}
}
