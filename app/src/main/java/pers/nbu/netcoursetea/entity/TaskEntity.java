package pers.nbu.netcoursetea.entity;

/**
 * Created by GraceChan on 2016/4/7.
 */
public class TaskEntity {
    private Integer TaskId;
    private Integer TaskNum;
    private String TaskTitle;
    private String CourName;
    private String TeachName;
    private String TaskTime;
    private String EndTime;
    private String TaskRequire;

    public TaskEntity(String taskTitle){
        TaskTitle = taskTitle;
    }

    public TaskEntity(Integer taskNum, String taskTitle, String courName, String teachName, String taskTime, String endTime,String taskRequire) {
        TaskNum = taskNum;
        TaskTitle = taskTitle;
        CourName = courName;
        TeachName = teachName;
        TaskTime = taskTime;
        EndTime = endTime;
        TaskRequire = taskRequire;
    }

    public TaskEntity(Integer taskId,Integer taskNum, String taskTitle, String courName, String teachName, String taskTime, String endTime,String taskRequire) {
        TaskId = taskId;
        TaskNum = taskNum;
        TaskTitle = taskTitle;
        CourName = courName;
        TeachName = teachName;
        TaskTime = taskTime;
        EndTime = endTime;
        TaskRequire = taskRequire;
    }

    public String getTaskRequire() {
        return TaskRequire;
    }

    public void setTaskRequire(String taskRequire) {
        TaskRequire = taskRequire;
    }
    public String getCourName() {
        return CourName;
    }

    public void setCourName(String courName) {
        CourName = courName;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public Integer getTaskId() {
        return TaskId;
    }

    public void setTaskId(Integer taskId) {
        TaskId = taskId;
    }

    public Integer getTaskNum() {
        return TaskNum;
    }

    public void setTaskNum(Integer taskNum) {
        TaskNum = taskNum;
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

    public String getTeachName() {
        return TeachName;
    }

    public void setTeachName(String teachName) {
        TeachName = teachName;
    }
}
