package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/18.
 */
public class AttendInfoEntity {
    private String AttdenceNum;
    private String StatusTime;
    private String TeachNum;
    private String ActNum;
    private Integer AttOpen;



    private String AttdenceClass;
    private String AttdenceWeek;
    private String PlaceName;
    private String Remark;
    private String ClassName;
    private String CourName;

    public AttendInfoEntity(String attdenceNum, String statusTime, String teachNum, String actNum, Integer attOpen, String attdenceClass, String attdenceWeek, String placeName, String remark) {
        AttdenceNum = attdenceNum;
        StatusTime = statusTime;
        TeachNum = teachNum;
        ActNum = actNum;
        AttOpen = attOpen;
        AttdenceClass = attdenceClass;
        AttdenceWeek = attdenceWeek;
        PlaceName = placeName;
        Remark = remark;
    }



    public AttendInfoEntity(String attdenceNum, String statusTime, String teachNum, String actNum, Integer attOpen,
                            String attdenceClass, String attdenceWeek, String placeName, String remark,
                            String className,String courName) {
        AttdenceNum = attdenceNum;
        StatusTime = statusTime;
        TeachNum = teachNum;
        ActNum = actNum;
        AttOpen = attOpen;
        AttdenceClass = attdenceClass;
        AttdenceWeek = attdenceWeek;
        PlaceName = placeName;
        Remark = remark;
        ClassName = className;
        CourName = courName ;

    }

    public AttendInfoEntity(String attdenceNum) {
        AttdenceNum = attdenceNum;
    }


    public String getCourName() {
        return CourName;
    }

    public void setCourName(String courName) {
        CourName = courName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getAttdenceNum() {
        return AttdenceNum;
    }

    public void setAttdenceNum(String attdenceNum) {
        AttdenceNum = attdenceNum;
    }

    public String getStatusTime() {
        return StatusTime;
    }

    public void setStatusTime(String statusTime) {
        StatusTime = statusTime;
    }

    public String getTeachNum() {
        return TeachNum;
    }

    public void setTeachNum(String teachNum) {
        TeachNum = teachNum;
    }

    public String getActNum() {
        return ActNum;
    }

    public void setActNum(String actNum) {
        ActNum = actNum;
    }

    public Integer getAttOpen() {
        return AttOpen;
    }

    public void setAttOpen(Integer attOpen) {
        AttOpen = attOpen;
    }

    public String getAttdenceClass() {
        return AttdenceClass;
    }

    public void setAttdenceClass(String attdenceClass) {
        AttdenceClass = attdenceClass;
    }

    public String getAttdenceWeek() {
        return AttdenceWeek;
    }

    public void setAttdenceWeek(String attdenceWeek) {
        AttdenceWeek = attdenceWeek;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
