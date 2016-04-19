package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/17.
 */
public class TaskInfoEntity {
    private Integer TaskNum;
    private String TeachNum;
    private String TaskTitle;
    private String TaskRequire;
    private String YorNSub;
    private String YorNVis;
    private String TaskUrl;
    private String FileOn;
    private String  Video;
    private String Annex;
    private String TaskTime;
    private String EndTime;
    private Integer Treeid;
    private String IsStuDown;
    private String IsShowResult;
    private String TreeName;
    private String CourseName;
    private String ClassName;


    public String getActNum() {
        return ActNum;
    }

    public void setActNum(String actNum) {
        ActNum = actNum;
    }

    private String ActNum;

    public TaskInfoEntity(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public TaskInfoEntity(Integer taskNum, String teachNum, String taskTitle, String taskRequire,
                          String yorNSub, String yorNVis, String taskUrl, String fileOn, String video,
                          String annex, String taskTime, String endTime, Integer treeid, String isStuDown,
                          String isShowResult,String actNum) {
        TaskNum = taskNum;
        TeachNum = teachNum;
        TaskTitle = taskTitle;
        TaskRequire = taskRequire;
        YorNSub = yorNSub;
        YorNVis = yorNVis;
        TaskUrl = taskUrl;
        FileOn = fileOn;
        Video = video;
        Annex = annex;
        TaskTime = taskTime;
        EndTime = endTime;
        Treeid = treeid;
        IsStuDown = isStuDown;
        IsShowResult = isShowResult;
        ActNum = actNum;
    }

    public TaskInfoEntity(Integer taskNum, String teachNum, String taskTitle, String taskRequire,
                          String yorNSub, String yorNVis, String taskUrl, String fileOn, String video,
                          String annex, String taskTime, String endTime, Integer treeid, String isStuDown,
                          String isShowResult,String actNum,String treeName,String courseName,String className) {
        TaskNum = taskNum;
        TeachNum = teachNum;
        TaskTitle = taskTitle;
        TaskRequire = taskRequire;
        YorNSub = yorNSub;
        YorNVis = yorNVis;
        TaskUrl = taskUrl;
        FileOn = fileOn;
        Video = video;
        Annex = annex;
        TaskTime = taskTime;
        EndTime = endTime;
        Treeid = treeid;
        IsStuDown = isStuDown;
        IsShowResult = isShowResult;
        ActNum = actNum;
        TreeName = treeName;
        CourseName = courseName ;
        ClassName = className;
    }

    public String getTreeName() {
        return TreeName;
    }

    public void setTreeName(String treeName) {
        TreeName = treeName;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getAnnex() {
        return Annex;
    }

    public void setAnnex(String annex) {
        Annex = annex;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getFileOn() {
        return FileOn;
    }

    public void setFileOn(String fileOn) {
        FileOn = fileOn;
    }

    public String getIsShowResult() {
        return IsShowResult;
    }

    public void setIsShowResult(String isShowResult) {
        IsShowResult = isShowResult;
    }

    public String getIsStuDown() {
        return IsStuDown;
    }

    public void setIsStuDown(String isStuDown) {
        IsStuDown = isStuDown;
    }

    public Integer getTaskNum() {
        return TaskNum;
    }

    public void setTaskNum(Integer taskNum) {
        TaskNum = taskNum;
    }

    public String getTaskRequire() {
        return TaskRequire;
    }

    public void setTaskRequire(String taskRequire) {
        TaskRequire = taskRequire;
    }

    public String getTaskTime() {
        return TaskTime;
    }

    public void setTaskTime(String taskTime) {
        TaskTime = taskTime;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public String getTaskUrl() {
        return TaskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        TaskUrl = taskUrl;
    }

    public String getTeachNum() {
        return TeachNum;
    }

    public void setTeachNum(String teachNum) {
        TeachNum = teachNum;
    }

    public Integer getTreeid() {
        return Treeid;
    }

    public void setTreeid(Integer treeid) {
        Treeid = treeid;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getYorNSub() {
        return YorNSub;
    }

    public void setYorNSub(String yorNSub) {
        YorNSub = yorNSub;
    }

    public String getYorNVis() {
        return YorNVis;
    }

    public void setYorNVis(String yorNVis) {
        YorNVis = yorNVis;
    }
}
