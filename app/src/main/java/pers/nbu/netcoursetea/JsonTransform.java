package pers.nbu.netcoursetea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.ActEntity;
import pers.nbu.netcoursetea.entity.AnnEntity;
import pers.nbu.netcoursetea.entity.AnnInfoEntity;
import pers.nbu.netcoursetea.entity.AttendEntity;
import pers.nbu.netcoursetea.entity.AttendInfoEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.entity.TaskEntity;
import pers.nbu.netcoursetea.entity.TaskInfoEntity;
import pers.nbu.netcoursetea.entity.TaskManageEntity;
import pers.nbu.netcoursetea.entity.TreeEntity;

/**
 * Created by GraceChan on 2016/4/6.
 */
public class JsonTransform {
    private static JsonTransform jsonTransform;
    private DB db= DB.getInstance();

    private JsonTransform(){

    }

    public synchronized static JsonTransform getInstance(){
        if (jsonTransform==null){
            jsonTransform=new JsonTransform();
        }
        return jsonTransform;
    }

    /**
     * 将获取到的json格式公告信息转换成公告阅读格式
     * @param jsonObject
     * @return
     */
    public Boolean turnToAnnLists(JSONObject jsonObject) {
        ArrayList<AnnEntity> lists = new ArrayList<>();
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AnnEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new AnnEntity(jobj.getInt("annNum"),
                        jobj.getString("annTitle"),
                        jobj.getString("annCon"),
                        jobj.getString("annUrl"),
                        jobj.getString("annTime"),
                        jobj.getString("teachName"),
                        jobj.getString("courName")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveAnnInfo(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式任务信息转换成任务阅读格式
     * @param jsonObject
     * @return
     */
    public Boolean turnToTaskLists(JSONObject jsonObject) {
        ArrayList<TaskEntity> lists = new ArrayList<>();

        //解析获取到的json数据串（所有任务信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            TaskEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new TaskEntity(jobj.getInt("taskNum"),
                        jobj.getString("taskTitle"),
                        jobj.getString("courName"),
                        jobj.getString("teachName"),
                        jobj.getString("taskTime"),
                        jobj.getString("endTime"),
                        jobj.getString("taskRequire")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveTaskShow(lists);
            return true;
        }
        return false;
    }


    /**
     * 将获取到的json格式任务上交情况信息转换成任务上交情况阅读格式
     * @param jsonObject
     * @return
     */
    public Boolean turnToTaskMLists(JSONObject jsonObject) {
        ArrayList<TaskManageEntity> lists = new ArrayList<>();

        //解析获取到的json数据串（所有任务信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            TaskManageEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new TaskManageEntity(jobj.getInt("taskNum"),
                        jobj.getInt("treeid"),
                        jobj.getString("teachName"),
                        jobj.getString("taskTitle"),
                        jobj.getString("courName"),
                        jobj.getString("taskTime"),
                        jobj.getString("endTime"),
                        jobj.getInt("opusNum")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveTaskManageShow(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式学生考勤情况信息转换成考勤格式
     * @param jsonObject
     * @return
     */
    public Boolean turnToAttendMLists(JSONObject jsonObject) {
        ArrayList<AttendEntity> lists = new ArrayList<>();

        //解析获取到的json数据串（所有出勤信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AttendEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new AttendEntity(
                        jobj.getInt("attdenceNum"),
                        jobj.getInt("actNum"),
                        jobj.getString("placeName"),
                        jobj.getString("courName"),
                        jobj.getString("teachName"),
                        jobj.getString("attdenceWeek"),
                        jobj.getString("statusTime"),
                        jobj.getString("staName"),
                        jobj.getString("status"),
                        jobj.getString("attdenceClass"),
                        0
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveAttend(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式公告信息转换成公告格式
     * @param jsonObject
     * @return
     */
    public Boolean toAnnInfoLists(JSONObject jsonObject) {
        ArrayList<AnnInfoEntity> lists = new ArrayList<>();
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AnnInfoEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new AnnInfoEntity(jobj.getInt("annNum"),
                        jobj.getString("annTitle"),
                        jobj.getString("annCon"),
                        jobj.getString("annTime"),
                        jobj.getString("annUrl"),
                        jobj.getString("teachNum"),
                        jobj.getString("annType"),
                        jobj.getInt("treeid")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveAnn(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式课程信息转换成课程格式
     * @param jsonObject
     * @return
     */
    public Boolean toCourseLists(JSONObject jsonObject) {
        ArrayList<CourseEntity> lists = new ArrayList<>();
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            CourseEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new CourseEntity(
                        jobj.getString("courName"),
                        jobj.getString("courNum"),
                        jobj.getInt("treeid")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveCourse(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式任务信息转换成任务格式
     * @param jsonObject
     * @return
     */
    public Boolean toTaskInfoLists(JSONObject jsonObject) {
        ArrayList<TaskInfoEntity> lists = new ArrayList<>();
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            TaskInfoEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new TaskInfoEntity(jobj.getInt("taskNum"),
                        jobj.getString("teachNum"),
                        jobj.getString("taskTitle"),
                        jobj.getString("taskRequire"),
                        jobj.getString("yorNSub"),
                        jobj.getString("yorNVis"),
                        jobj.getString("taskUrl"),
                        jobj.getString("fileOn"),
                        jobj.getString("video"),
                        jobj.getString("annex"),
                        jobj.getString("taskTime"),
                        jobj.getString("endTime"),
                        jobj.getInt("treeid"),
                        jobj.getString("isStuDown"),
                        jobj.getString("isShowResult"),
                        jobj.getString("actNum")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveTaskInfo(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式章节信息转换成本地章节格式
     * @param jsonObject
     * @return
     */
    public Boolean toTreeLists(JSONObject jsonObject) {
        ArrayList<TreeEntity> lists = new ArrayList<>();
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            TreeEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new TreeEntity(
                        jobj.getInt("treeid"),
                        jobj.getString("courNum"),
                        jobj.getString("treeName")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveTree(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式章节信息转换成本地章节格式
     * @param jsonObject
     * @return
     */
    public Boolean toActLists(JSONObject jsonObject) {
        ArrayList<ActEntity> lists = new ArrayList<>();
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            ActEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new ActEntity(
                        jobj.getString("actNum"),
                        jobj.getString("courNum"),
                        jobj.getString("className")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveAct(lists);
            return true;
        }
        return false;
    }

    /**
     * 将获取到的json格式考勤信息转换成本地考勤 格式
     * @param jsonObject
     * @return
     */
    public Boolean toAttendInfoLists(JSONObject jsonObject) {
        ArrayList<AttendInfoEntity> lists = new ArrayList<>();
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AttendInfoEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new AttendInfoEntity(
                        jobj.getString("attdenceNum"),
                        jobj.getString("statusTime"),
                        jobj.getString("teachNum"),
                        jobj.getString("actNum"),
                        jobj.getInt("attOpen"),
                        jobj.getString("attdenceClass"),
                        jobj.getString("attdenceWeek"),
                        jobj.getString("placeName"),
                        jobj.getString("remark")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveAttendInfo(lists);
            return true;
        }
        return false;
    }

}
