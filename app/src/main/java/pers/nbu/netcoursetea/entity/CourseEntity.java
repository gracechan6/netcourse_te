package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/17.
 */
public class CourseEntity {
    private Integer Treeid;
    private String CourNum;
    private String CourName;

    public CourseEntity(String courName, String courNum, Integer treeid) {
        CourName = courName;
        CourNum = courNum;
        Treeid = treeid;
    }

    public String getCourName() {
        return CourName;
    }

    public void setCourName(String courName) {
        CourName = courName;
    }

    public String getCourNum() {
        return CourNum;
    }

    public void setCourNum(String courNum) {
        CourNum = courNum;
    }

    public Integer getTreeid() {
        return Treeid;
    }

    public void setTreeid(Integer treeid) {
        Treeid = treeid;
    }
}
