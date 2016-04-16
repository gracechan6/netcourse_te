package pers.nbu.netcoursetea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.AnnEntity;
import pers.nbu.netcoursetea.entity.AttendEntity;
import pers.nbu.netcoursetea.entity.TaskEntity;
import pers.nbu.netcoursetea.entity.TaskManageEntity;

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
//        int annNum = 0;
        //boolean find = false;
        //annNum = db.ifexistData(DB.TABLE_ANNINFO);
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AnnEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
//                if (annNum > 0) {
//                    if (annNum == jobj.getInt("annNum")) {
//                        entity = new AnnEntity(jobj.getInt("annNum"), jobj.getString("annTitle"),
//                                jobj.getString("annCon"), jobj.getString("annUrl"),
//                                jobj.getString("annTime"), jobj.getString("teachName"),
//                                jobj.getString("courName"));
//                        lists.add(entity);
//                        find = true;
//                    } else if (find) {
//                        entity = new AnnEntity(jobj.getInt("annNum"), jobj.getString("annTitle"),
//                                jobj.getString("annCon"), jobj.getString("annUrl"),
//                                jobj.getString("annTime"), jobj.getString("teachName"),
//                                jobj.getString("courName"));
//                        lists.add(entity);
//                    }
//                } else {
                entity = new AnnEntity(jobj.getInt("annNum"),
                        jobj.getString("annTitle"),
                        jobj.getString("annCon"),
                        jobj.getString("annUrl"),
                        jobj.getString("annTime"),
                        jobj.getString("teachName"),
                        jobj.getString("courName")
                );
                lists.add(entity);
//                }
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

        //解析获取到的json数据串（所有公告信息）
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
}
