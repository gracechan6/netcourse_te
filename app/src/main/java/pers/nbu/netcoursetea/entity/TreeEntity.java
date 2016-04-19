package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/17.
 */
public class TreeEntity {
    private Integer Treeid;
    private String CourNum;
    private String TreeName;

    public TreeEntity(Integer treeid, String courNum, String treeName) {
        Treeid = treeid;
        CourNum = courNum;
        TreeName = treeName;
    }

    public Integer getTreeid() {
        return Treeid;
    }

    public void setTreeid(Integer treeid) {
        Treeid = treeid;
    }

    public String getCourNum() {
        return CourNum;
    }

    public void setCourNum(String courNum) {
        CourNum = courNum;
    }

    public String getTreeName() {
        return TreeName;
    }

    public void setTreeName(String treeName) {
        TreeName = treeName;
    }
}
