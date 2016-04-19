package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/17.
 */
public class ActEntity {
    private String ActNum;
    private String CourNum;
    private String ClassName;

    public ActEntity(String actNum, String courNum, String className) {
        ActNum = actNum;
        CourNum = courNum;
        ClassName = className;
    }

    public String getActNum() {
        return ActNum;
    }

    public void setActNum(String actNum) {
        ActNum = actNum;
    }

    public String getCourNum() {
        return CourNum;
    }

    public void setCourNum(String courNum) {
        CourNum = courNum;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }
}
