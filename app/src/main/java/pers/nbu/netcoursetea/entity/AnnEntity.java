package pers.nbu.netcoursetea.entity;

public class AnnEntity {
	private Integer AnnId;
	private Integer AnnNum;
	private String AnnTitle;//1
	private String AnnCon;//2
	private String AnnUrl;//3
	private String AnnTime;//4
	private String TeachName;
	private String CourName;

    //private Integer read;//0已读 1未读


	public AnnEntity(String annTitle, String annCon) {
		AnnTitle = annTitle;
		AnnCon = annCon;
	}

	public AnnEntity(Integer annNum, String annTitle, String annCon, String annUrl, String annTime, String teachName, String courName) {
		AnnNum = annNum;
		AnnTitle = annTitle;
		AnnCon = annCon;
		AnnUrl = annUrl;
		AnnTime = annTime;
		TeachName = teachName;
		CourName = courName;
	}

	public AnnEntity(Integer annId,Integer annNum, String annTitle, String annCon, String annUrl, String annTime, String teachName, String courName) {
		AnnId = annId;
		AnnNum = annNum;
		AnnTitle = annTitle;
		AnnCon = annCon;
		AnnUrl = annUrl;
		AnnTime = annTime;
		TeachName = teachName;
		CourName = courName;
	}

	public Integer getAnnId() {
		return AnnId;
	}

	public void setAnnId(Integer annId) {
		AnnId = annId;
	}

	public Integer getAnnNum() {
		return AnnNum;
	}

	public void setAnnNum(Integer annNum) {
		AnnNum = annNum;
	}

	public String getAnnTitle() {
		return AnnTitle;
	}

	public void setAnnTitle(String annTitle) {
		AnnTitle = annTitle;
	}

	public String getAnnCon() {
		return AnnCon;
	}

	public void setAnnCon(String annCon) {
		AnnCon = annCon;
	}

	public String getAnnUrl() {
		return AnnUrl;
	}

	public void setAnnUrl(String annUrl) {
		AnnUrl = annUrl;
	}

	public String getAnnTime() {
		return AnnTime;
	}

	public void setAnnTime(String annTime) {
		AnnTime = annTime;
	}

	public String getTeachName() {
		return TeachName;
	}

	public void setTeachName(String teachName) {
		TeachName = teachName;
	}

	public String getCourName() {
		return CourName;
	}

	public void setCourName(String courName) {
		CourName = courName;
	}
}
