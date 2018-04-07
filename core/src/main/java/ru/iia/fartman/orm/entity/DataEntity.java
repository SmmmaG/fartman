package ru.iia.fartman.orm.entity;

import java.util.UUID;

@Entity
public class DataEntity {

	private String dateString;
	private String nameString;
	private String wFirstString;
	private String drawString;
	private String wSecondString;
	private String additionalNumber;

	private UUID uuid;

	private DataEntity() {

	}

	public static DataEntity newDataEntity() {
		DataEntity result = new DataEntity();
		result.uuid = UUID.randomUUID();
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


	public UUID getUuid() {
		return uuid;
	}

	protected void setUuid(UUID uuid) {
		this.uuid = uuid;
	}


	public String toString() {
		return uuid.toString() + "@" + dateString + ";" + nameString + ";" + wFirstString + ";" + drawString + ";"
				+ wSecondString + ";" + additionalNumber;
	}
}
