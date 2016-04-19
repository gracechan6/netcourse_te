package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/16.
 */
public class AnnInfoEntity {

    private Integer AnnNum;
    private String AnnTitle;
    private String AnnCon;
    private String AnnTime;
    private String AnnUrl;
    private String TeachNum;
    private String AnnType;
    private Integer Treeid;
    private String CourName;

    public AnnInfoEntity(String annTitle) {
        AnnTitle = annTitle;
    }

    public AnnInfoEntity(String annTitle, String annCon,
                      String annTime, String annUrl, String teachNum, String annType,
                      Integer treeid) {
        AnnTitle = annTitle;
        AnnCon = annCon;
        AnnTime = annTime;
        AnnUrl = annUrl;
        TeachNum = teachNum;
        AnnType = annType;
        Treeid = treeid;
    }

    public AnnInfoEntity(String annTitle, String annCon,
                         String annTime, Integer annNum) {
        AnnTitle = annTitle;
        AnnCon = annCon;
        AnnTime = annTime;
        AnnNum = annNum;
    }

    public AnnInfoEntity(Integer annNum, String annTitle, String annCon,
                      String annTime, String annUrl, String teachNum, String annType,
                      Integer treeid) {
        AnnNum = annNum;
        AnnTitle = annTitle;
        AnnCon = annCon;
        AnnTime = annTime;
        AnnUrl = annUrl;
        TeachNum = teachNum;
        AnnType = annType;
        Treeid = treeid;
    }

    public AnnInfoEntity(Integer annNum, String annTitle, String annCon,
                         String annTime, String annUrl, String teachNum, String annType,
                         Integer treeid,String courName) {
        AnnNum = annNum;
        AnnTitle = annTitle;
        AnnCon = annCon;
        AnnTime = annTime;
        AnnUrl = annUrl;
        TeachNum = teachNum;
        AnnType = annType;
        Treeid = treeid;
        CourName = courName;
    }

    public String getCourName() {
        return CourName;
    }

    public void setCourName(String courName) {
        CourName = courName;
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
    public Integer getTreeid() {
        return Treeid;
    }
    public void setTreeid(Integer treeid) {
        Treeid = treeid;
    }
    public String getTeachNum() {
        return TeachNum;
    }
    public void setTeachNum(String teachNum) {
        TeachNum = teachNum;
    }
    public String getAnnType() {
        return AnnType;
    }
    public void setAnnType(String annType) {
        AnnType = annType;
    }
}
