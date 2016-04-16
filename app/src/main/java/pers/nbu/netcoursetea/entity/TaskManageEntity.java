package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/13.
 */
public class TaskManageEntity {
    private Integer TaskId;
    private Integer TaskNum;
    private Integer Treeid;
    private String TeachName;
    private String TaskTitle;
    private String CourName;
    private String TaskTime;
    private String EndTime;
    private Integer OpusNum;

    public TaskManageEntity(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public TaskManageEntity(Integer taskId,Integer taskNum) {
        TaskId = taskId;
        TaskNum = taskNum;
    }

    public TaskManageEntity(Integer taskId,Integer taskNum, Integer treeid, String teachName,
                            String taskTitle, String courName, String taskTime, String endTime,
                            Integer opusNum) {
        TaskId = taskId;
        TaskNum = taskNum;
        Treeid = treeid;
        TeachName = teachName;
        TaskTitle = taskTitle;
        CourName = courName;
        TaskTime = taskTime;
        EndTime = endTime;
        OpusNum = opusNum;
    }

    public TaskManageEntity(Integer taskNum, Integer treeid, String teachName,
                          String taskTitle, String courName, String taskTime, String endTime,
                          Integer opusNum) {
        TaskNum = taskNum;
        Treeid = treeid;
        TeachName = teachName;
        TaskTitle = taskTitle;
        CourName = courName;
        TaskTime = taskTime;
        EndTime = endTime;
        OpusNum = opusNum;
    }
    public Integer getTaskNum() {
        return TaskNum;
    }
    public void setTaskNum(Integer taskNum) {
        TaskNum = taskNum;
    }
    public Integer getTreeid() {
        return Treeid;
    }
    public void setTreeid(Integer treeid) {
        Treeid = treeid;
    }
    public String getTeachName() {
        return TeachName;
    }
    public void setTeachName(String teachName) {
        TeachName = teachName;
    }
    public String getTaskTitle() {
        return TaskTitle;
    }
    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }
    public String getCourName() {
        return CourName;
    }
    public void setCourName(String courName) {
        CourName = courName;
    }
    public String getTaskTime() {
        return TaskTime;
    }
    public void setTaskTime(String taskTime) {
        TaskTime = taskTime;
    }
    public String getEndTime() {
        return EndTime;
    }
    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
    public Integer getOpusNum() {
        return OpusNum;
    }
    public void setOpusNum(Integer opusNum) {
        OpusNum = opusNum;
    }

}
