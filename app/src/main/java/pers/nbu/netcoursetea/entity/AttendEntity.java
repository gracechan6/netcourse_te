package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/15.
 */
public class AttendEntity {
    private int AttdenceId;
    private int AttdenceNum;
    private int ActNum;
    private String PlaceName;
    private String CourName;
    private String TeachName;
    private String AttdenceWeek;
    private String StatusTime;
    private String StaName;
    private String Status;
    private String AttdenceClass;
    private int UpdateNum;

    public int getAttdenceId() {
        return AttdenceId;
    }

    public void setAttdenceId(int attdenceId) {
        AttdenceId = attdenceId;
    }

    public int getUpdateNum() {
        return UpdateNum;
    }

    public void setUpdateNum(int updateNum) {
        UpdateNum = updateNum;
    }

    public AttendEntity(String status) {
        Status = status;
    }

    public AttendEntity(int attdenceId,int attdenceNum, int actNum, String placeName,
                        String courName, String teachName, String attdenceWeek,
                        String statusTime, String staName, String status,
                        String attdenceClass,int updateNum) {
        super();
        AttdenceId = attdenceId ;
        AttdenceNum = attdenceNum;
        ActNum = actNum;
        PlaceName = placeName;
        CourName = courName;
        TeachName = teachName;
        AttdenceWeek = attdenceWeek;
        StatusTime = statusTime;
        StaName = staName;
        Status = status;
        AttdenceClass = attdenceClass;
        UpdateNum = updateNum;
    }

    public AttendEntity(int attdenceNum, int actNum, String placeName,
                      String courName, String teachName, String attdenceWeek,
                      String statusTime, String staName, String status,
                      String attdenceClass,int updateNum) {
        super();
        AttdenceNum = attdenceNum;
        ActNum = actNum;
        PlaceName = placeName;
        CourName = courName;
        TeachName = teachName;
        AttdenceWeek = attdenceWeek;
        StatusTime = statusTime;
        StaName = staName;
        Status = status;
        AttdenceClass = attdenceClass;
        UpdateNum = updateNum;
    }
    public int getAttdenceNum() {
        return AttdenceNum;
    }
    public void setAttdenceNum(int attdenceNum) {
        AttdenceNum = attdenceNum;
    }
    public int getActNum() {
        return ActNum;
    }
    public void setActNum(int actNum) {
        ActNum = actNum;
    }
    public String getPlaceName() {
        return PlaceName;
    }
    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }
    public String getCourName() {
        return CourName;
    }
    public void setCourName(String courName) {
        CourName = courName;
    }
    public String getTeachName() {
        return TeachName;
    }
    public void setTeachName(String teachName) {
        TeachName = teachName;
    }
    public String getAttdenceWeek() {
        return AttdenceWeek;
    }
    public void setAttdenceWeek(String attdenceWeek) {
        AttdenceWeek = attdenceWeek;
    }
    public String getStatusTime() {
        return StatusTime;
    }
    public void setStatusTime(String statusTime) {
        StatusTime = statusTime;
    }
    public String getStaName() {
        return StaName;
    }
    public void setStaName(String staName) {
        StaName = staName;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }
    public String getAttdenceClass() {
        return AttdenceClass;
    }
    public void setAttdenceClass(String attdenceClass) {
        AttdenceClass = attdenceClass;
    }
}
