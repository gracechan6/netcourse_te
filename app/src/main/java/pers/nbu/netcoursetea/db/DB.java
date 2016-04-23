package pers.nbu.netcoursetea.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pers.nbu.netcoursetea.BaseApplication;
import pers.nbu.netcoursetea.config.SystemConfig;
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
 * Created by GraceChan on 2015/10/12.
 */
public class DB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "AIOC";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static DB DB;

    private SQLiteDatabase db;


    /**
     * 表名
     */
    public static final String TABLE_ANNSHOW = "tbAnnShow";
    public static final String TABLE_ANN = "tbAnn";
    public static final String TABLE_ACT = "tbAct";
    public static final String TABLE_TREE = "tbTree";
    public static final String TABLE_COURSE = "tbCourse";
    public static final String TABLE_TASKINFO = "tbTaskInfo";
    public static final String TABLE_ATTENDADMIN = "tbAttendAdmin";



    public static final String TABLE_TASKSHOW = "tbTaskShow";
    public static final String TABLE_TASKMANAGESHOW = "tbTaskManageShow";
    public static final String TABLE_ATTENDSHOW = "tbAttendShow";


    /**
     * 将构造方法私有化
     */
    private DB() {
        DBOpenHelper dbHelper = new DBOpenHelper(BaseApplication.getContext(),
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CabinetGridDB的实例。
     */
    public synchronized static DB getInstance() {
        if (DB == null) {
            DB = new DB();
        }
        return DB;
    }

//公共方法
    /**
     * 判断表中是否存在数据
     * @param tableName
     */
    public int ifexistData(String tableName,String num){
        Cursor cursor=db.query(tableName,null,null,null,null,null,null);
        int count=1;
        int annnum=0;
        if (cursor.moveToFirst()){
            do {
                if (count == 2)
                    return cursor.getInt(cursor.getColumnIndex(num));
                annnum=cursor.getInt(cursor.getColumnIndex(num));
                count++;
            }while (cursor.moveToNext());
        }
        return annnum;
    }

    /**
     * 表中数据总条数
     * @param tableName
     */
    public int countData(String tableName){
        Cursor cursor=db.query(tableName,null,null,null,null,null,null);

        return cursor.getCount();
    }

    public int delData(String tableName){
        return  db.delete(tableName, null, null);
    }
//start Ann DB
    /**
     * 将服务器端获取到的公告信息同步存储到本地数据
     * @param anns
     */
    public void saveAnnInfo(List<AnnEntity> anns){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < anns.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.ANNNUM, anns.get(i).getAnnNum());
            values.put(SystemConfig.ANNTITLE, anns.get(i).getAnnTitle());
            values.put(SystemConfig.ANNCON, anns.get(i).getAnnCon());
            values.put(SystemConfig.ANNURL, anns.get(i).getAnnUrl());
            values.put(SystemConfig.ANNTIME, anns.get(i).getAnnTime());
            values.put(SystemConfig.TEACHNAME, anns.get(i).getTeachName());
            values.put(SystemConfig.COURNAME, anns.get(i).getCourName());
            db.insert(TABLE_ANNSHOW, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num
     * @return
     */
    public ArrayList<AnnEntity> getAnnInfo(int num){
        ArrayList<AnnEntity> anns= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_ANNSHOW+" order by AnnId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                AnnEntity ann = new AnnEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.ANNID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ANNNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNCON)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNURL)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)));
                anns.add(ann);
            }while (cursor.moveToNext());
        }
        return anns;
    }

//end Ann DB
//start Task DB
    /**
     * 将服务器端获取到的任务展示信息同步存储到本地数据
     * @param tasks
     */
    public void saveTaskShow(List<TaskEntity> tasks){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < tasks.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.TASKNUM, tasks.get(i).getTaskNum());
            values.put(SystemConfig.TASKTITLE, tasks.get(i).getTaskTitle());
            values.put(SystemConfig.TASKTIME, tasks.get(i).getTaskTime());
            values.put(SystemConfig.ENDTIME, tasks.get(i).getEndTime());
            values.put(SystemConfig.TEACHNAME, tasks.get(i).getTeachName());
            values.put(SystemConfig.COURNAME, tasks.get(i).getCourName());
            values.put(SystemConfig.TASKREQUIRE, tasks.get(i).getCourName());
            db.insert(TABLE_TASKSHOW, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num
     * @return
     */
    public ArrayList<TaskEntity> getTaskShow(int num){
        ArrayList<TaskEntity> tasks= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_TASKSHOW+" order by TaskId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                TaskEntity task = new TaskEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ENDTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKREQUIRE)));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }
//end Task DB

//start TaskManage DB
    /**
     * 将服务器端获取到的任务管理信息同步存储到本地数据
     * @param tasks
     */
    public void saveTaskManageShow(List<TaskManageEntity> tasks){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < tasks.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.TASKNUM, tasks.get(i).getTaskNum());
            values.put(SystemConfig.TREEID, tasks.get(i).getTreeid());
            values.put(SystemConfig.TEACHNAME, tasks.get(i).getTeachName());
            values.put(SystemConfig.TASKTITLE, tasks.get(i).getTaskTitle());
            values.put(SystemConfig.COURNAME, tasks.get(i).getCourName());
            values.put(SystemConfig.TASKTIME, tasks.get(i).getTaskTime());
            values.put(SystemConfig.ENDTIME, tasks.get(i).getEndTime());
            values.put(SystemConfig.OPUSNUM, tasks.get(i).getOpusNum());
            db.insert(TABLE_TASKMANAGESHOW, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num
     * @return
     */
    public ArrayList<TaskManageEntity> getTaskManageShow(int num){
        ArrayList<TaskManageEntity> tasks= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_TASKMANAGESHOW+" order by TaskId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                TaskManageEntity task = new TaskManageEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKNUM)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TREEID)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ENDTIME)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.OPUSNUM)));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }

    /**
     * 从本地数据库数据
     * @param b    以此来区分是要取已交的部分还是未交的部分
     * @return
     */
    public ArrayList<TaskManageEntity> getTaskManageShowOver(Boolean b){
        ArrayList<TaskManageEntity> tasks= new ArrayList<>();
        String sql;
        if (b){
            sql="select TaskNum,TaskId,OpusNum from "+TABLE_TASKMANAGESHOW+" where OpusNum>0 order by TaskId";
        }else{
            sql="select TaskNum,TaskId,OpusNum from "+TABLE_TASKMANAGESHOW+" where OpusNum=0 order by TaskId";
        }
        Cursor cursor=db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                TaskManageEntity task = new TaskManageEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKNUM)));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }

    /**
     * 从本地数据库数据
     * @param id     taskId
     * @param num   OptNum
     * @return
     */
    public int updateTaskManageShowOver(int id,int num){

        ContentValues value=new ContentValues();
        value.put(SystemConfig.OPUSNUM,num);

        String[] args = {String.valueOf(id)};
        return db.update(TABLE_TASKMANAGESHOW, value, "TaskNum=?" ,args);
    }

//end TaskManage DB

//start Attend DB
    /**
     * 将服务器端获取到的出勤信息同步存储到本地数据
     * @param attends    需要存储到本地的列表
     */
    public void saveAttend(List<AttendEntity> attends){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < attends.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.ATTDENCENUM, attends.get(i).getAttdenceNum());
            values.put(SystemConfig.ACTNUM, attends.get(i).getActNum());
            values.put(SystemConfig.PLACENAME, attends.get(i).getPlaceName());
            values.put(SystemConfig.COURNAME, attends.get(i).getCourName());
            values.put(SystemConfig.TEACHNAME, attends.get(i).getTeachName());
            values.put(SystemConfig.ATTDENCEWEEK, attends.get(i).getAttdenceWeek());
            values.put(SystemConfig.STATUSTIME, attends.get(i).getStatusTime());
            values.put(SystemConfig.STANAME, attends.get(i).getStaName());
            values.put(SystemConfig.STATUS, attends.get(i).getStatus());
            values.put(SystemConfig.ATTDENCECLASS, attends.get(i).getAttdenceClass());
            values.put(SystemConfig.UPDATENUM, attends.get(i).getUpdateNum());
            db.insert(TABLE_ATTENDSHOW, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num 需要获取的条数
     * @return
     */
    public ArrayList<AttendEntity> getAttend(int num){
        ArrayList<AttendEntity> attends= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_ATTENDSHOW+" order by AttdenceId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                AttendEntity attend = new AttendEntity(
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTDENCEID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTDENCENUM)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.PLACENAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCEWEEK)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STATUSTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STANAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STATUS)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCECLASS)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.UPDATENUM)));
                attends.add(attend);
            }while (cursor.moveToNext());
        }
        return attends;
    }

    /**
     * @return 返回可以更新的出勤记录 去服务器比较
     */
    public ArrayList<AttendEntity> getNeedUpdateAttend(){
        ArrayList<AttendEntity> attends= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_ATTENDSHOW+" where UpdateNum<>1000 and Status ='缺课'",null);
        if (cursor.moveToFirst()){
            do {
                AttendEntity attend = new AttendEntity(
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTDENCEID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTDENCENUM)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.PLACENAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCEWEEK)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STATUSTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STANAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STATUS)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCECLASS)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.UPDATENUM)));
                attends.add(attend);
            }while (cursor.moveToNext());
        }
        return attends;
    }

    /**
     * 更新本地出勤记录
     * @param id  出勤id
     * @param staName 考勤状态
     * @param status 状态
     * @return 更新条数
     */
    public int updateAttend(int id,String staName,String status,int num){
        ContentValues value=new ContentValues();
        value.put(SystemConfig.STANAME,staName);
        value.put(SystemConfig.STATUS,status);
        value.put(SystemConfig.UPDATENUM,num);

        String[] args = {String.valueOf(id)};
        return db.update(TABLE_ATTENDSHOW, value, "AttdenceNum=?" ,args);
    }

    public AttendEntity getAttendByANum(int num){
        Cursor cursor=db.rawQuery("select * from "+TABLE_ATTENDSHOW+" where AttdenceNum="+num,null);
        if (cursor.moveToFirst()){
            AttendEntity attend = new AttendEntity(
                    cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTDENCEID)),
                    cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTDENCENUM)),
                    cursor.getInt(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.PLACENAME)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCEWEEK)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.STATUSTIME)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.STANAME)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.STATUS)),
                    cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCECLASS)),
                    cursor.getInt(cursor.getColumnIndex(SystemConfig.UPDATENUM)));
            return attend;
        }
        return null;
    }
//end Attend DB



//start AnnInfo DB
    /**
     * 将服务器端获取到的公告信息同步存储到本地数据
     * @param anns
     */
    public void saveAnn(List<AnnInfoEntity> anns){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < anns.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.ANNNUM, anns.get(i).getAnnNum());
            values.put(SystemConfig.ANNTITLE, anns.get(i).getAnnTitle());
            values.put(SystemConfig.ANNCON, anns.get(i).getAnnCon());
            values.put(SystemConfig.ANNTIME, anns.get(i).getAnnTime());
            values.put(SystemConfig.ANNURL, anns.get(i).getAnnUrl());
            values.put(SystemConfig.TEACHNUM, anns.get(i).getTeachNum());
            values.put(SystemConfig.ANNTYPE, anns.get(i).getAnnType());
            values.put(SystemConfig.TREEID, anns.get(i).getTreeid());
            db.insert(TABLE_ANN, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 将插入一条数据
     * @param anns
     */
    public void insertAnn(AnnInfoEntity anns){

        ContentValues values=null;
        db.beginTransaction();
        values=new ContentValues();
        values.put(SystemConfig.ANNNUM, anns.getAnnNum());
        values.put(SystemConfig.ANNTITLE, anns.getAnnTitle());
        values.put(SystemConfig.ANNCON, anns.getAnnCon());
        values.put(SystemConfig.ANNTIME, anns.getAnnTime());
        values.put(SystemConfig.ANNURL, anns.getAnnUrl());
        values.put(SystemConfig.TEACHNUM, anns.getTeachNum());
        values.put(SystemConfig.ANNTYPE, anns.getAnnType());
        values.put(SystemConfig.TREEID, anns.getTreeid());

        db.insert(TABLE_ANN, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num  获取条数
     * @param id  用户id
     * @return
     */
    public ArrayList<AnnInfoEntity> getAnn(int num,String id){
        ArrayList<AnnInfoEntity> anns= new ArrayList<>();
        AnnInfoEntity ann;
        Cursor cursor=db.rawQuery("select * from "+TABLE_ANN+" a,"+TABLE_COURSE+" b where a.Treeid=b.Treeid and TeachNum ="+ id +" order by AnnNum desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                ann = new AnnInfoEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.ANNNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNCON)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNURL)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNTYPE)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TREEID)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)));
                anns.add(ann);
            }while (cursor.moveToNext());
        }
        return anns;
    }

    /**
     * 更新本地公告信息
     * @param ann 更新的AnnInfoEntity
     * @return 更新条数
     */
    public int updateAnn(AnnInfoEntity ann){
        ContentValues values=new ContentValues();
        values.put(SystemConfig.ANNTITLE, ann.getAnnTitle());
        values.put(SystemConfig.ANNCON, ann.getAnnCon());
        values.put(SystemConfig.ANNTIME, ann.getAnnTime());
        values.put(SystemConfig.ANNURL, ann.getAnnUrl());
        values.put(SystemConfig.TEACHNUM, ann.getTeachNum());
        values.put(SystemConfig.ANNTYPE, ann.getAnnType());
        values.put(SystemConfig.TREEID, ann.getTreeid());

        String[] args = {String.valueOf(ann.getAnnNum())};
        return db.update(TABLE_ANN, values, "AnnNum=?" ,args);
    }

    /**
     * 删除本地公告
     * @param num AnnNum
     * @return 删除条数
     */
    public int delAnn(int num){
        String[] args = {String.valueOf(num)};
        db.delete(TABLE_ANNSHOW, "AnnNum=?", args);
        return  db.delete(TABLE_ANN, "AnnNum=?", args);
    }

    /**
     * 将服务器端获取到的课程信息同步存储到本地数据
     * @param courses
     */
    public void saveCourse(List<CourseEntity> courses){
        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < courses.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.COURNAME, courses.get(i).getCourName());
            values.put(SystemConfig.COURNUM, courses.get(i).getCourNum());
            values.put(SystemConfig.TREEID, courses.get(i).getTreeid());
            db.insert(TABLE_COURSE, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num  获取条数
     * @param id  用户id
     * @return
     */
    public ArrayList<CourseEntity> getCourse(int num,String id){
        ArrayList<CourseEntity> cours= new ArrayList<>();
        CourseEntity cour;
        Cursor cursor=db.rawQuery("select * from "+TABLE_COURSE+" order by Treeid desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                cour = new CourseEntity(
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNUM)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TREEID)));
                cours.add(cour);
            }while (cursor.moveToNext());
        }
        return cours;
    }

//end AnnInfo DB

//start TaskInfo DB
    /**
     * 将服务器端获取到的公告信息同步存储到本地数据
     * @param tasks
     */
    public void saveTaskInfo(List<TaskInfoEntity> tasks){
        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < tasks.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.TASKNUM, tasks.get(i).getTaskNum());
            values.put(SystemConfig.TEACHNUM, tasks.get(i).getTeachNum());
            values.put(SystemConfig.TASKTITLE, tasks.get(i).getTaskTitle());
            values.put(SystemConfig.TASKREQUIRE, tasks.get(i).getTaskRequire());
            values.put(SystemConfig.YORNSUB, tasks.get(i).getYorNSub());
            values.put(SystemConfig.YORNVIS, tasks.get(i).getTeachNum());
            values.put(SystemConfig.TASKURL, tasks.get(i).getTaskUrl());
            values.put(SystemConfig.FILEON, tasks.get(i).getFileOn());
            values.put(SystemConfig.VIDEO, tasks.get(i).getVideo());
            values.put(SystemConfig.ANNEX, tasks.get(i).getAnnex());
            values.put(SystemConfig.TASKTIME, tasks.get(i).getTaskTime());
            values.put(SystemConfig.ENDTIME, tasks.get(i).getEndTime());
            values.put(SystemConfig.ISSTUDOWN, tasks.get(i).getIsStuDown());
            values.put(SystemConfig.ISSHOWRESULT, tasks.get(i).getIsShowResult());
            values.put(SystemConfig.TREEID, tasks.get(i).getTreeid());
            values.put(SystemConfig.ACTNUM, tasks.get(i).getActNum());
            db.insert(TABLE_TASKINFO, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 将插入一条数据
     * @param task
     */
    public void insertTaskInfo(TaskInfoEntity task){

        ContentValues values=null;
        db.beginTransaction();
        values=new ContentValues();
        values.put(SystemConfig.TASKNUM, task.getTaskNum());
        values.put(SystemConfig.TEACHNUM, task.getTeachNum());
        values.put(SystemConfig.TASKTITLE, task.getTaskTitle());
        values.put(SystemConfig.TASKREQUIRE, task.getTaskRequire());
        values.put(SystemConfig.YORNSUB, task.getYorNSub());
        values.put(SystemConfig.YORNVIS, task.getTeachNum());
        values.put(SystemConfig.TASKURL, task.getTaskUrl());
        values.put(SystemConfig.FILEON, task.getFileOn());
        values.put(SystemConfig.VIDEO, task.getVideo());
        values.put(SystemConfig.ANNEX, task.getAnnex());
        values.put(SystemConfig.TASKTIME, task.getTaskTime());
        values.put(SystemConfig.ENDTIME, task.getEndTime());
        values.put(SystemConfig.ISSTUDOWN, task.getIsStuDown());
        values.put(SystemConfig.ISSHOWRESULT, task.getIsShowResult());
        values.put(SystemConfig.TREEID, task.getTreeid());
        values.put(SystemConfig.ACTNUM, task.getActNum());
        db.insert(TABLE_TASKINFO, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num  获取条数
     * @param id  用户id
     * @return
     */
    public ArrayList<TaskInfoEntity> getTaskInfo(int num,String id){
        ArrayList<TaskInfoEntity> tasks= new ArrayList<>();
        TaskInfoEntity task;//+" a,"+TABLE_COURSE+" c,"+TABLE_ACT+" b where a.ActNum=b.ActNum and c.CourNum=b.CourNum and
        Cursor cursor=db.rawQuery("select * from "+TABLE_TASKINFO+" a,"+TABLE_COURSE+" c,"+TABLE_TREE+" d,"+TABLE_ACT+" b where a.Treeid=d.Treeid and c.CourNum=d.CourNum and c.CourNum=b.CourNum and a.ActNum=b.ActNum and TeachNum ="+ id +" order by TaskNum desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                task = new TaskInfoEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKREQUIRE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.YORNSUB)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.YORNVIS)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKURL)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.FILEON)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.VIDEO)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNEX)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ENDTIME)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TREEID)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ISSTUDOWN)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ISSHOWRESULT)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TREENAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.CLASSNAME)));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }

    /**
     * 更新本地公告信息
     * @param task
     * @return 更新条数
     */
    public int updateTaskInfo(TaskInfoEntity task){
        ContentValues values=new ContentValues();
        values.put(SystemConfig.TASKTITLE, task.getTaskTitle());
        values.put(SystemConfig.TASKREQUIRE, task.getTaskRequire());
        values.put(SystemConfig.YORNSUB, task.getYorNSub());
        values.put(SystemConfig.YORNVIS, task.getYorNVis());
        values.put(SystemConfig.FILEON, task.getFileOn());
        values.put(SystemConfig.VIDEO, task.getVideo());
        values.put(SystemConfig.ANNEX, task.getAnnex());
        values.put(SystemConfig.ENDTIME, task.getEndTime());
        values.put(SystemConfig.ISSTUDOWN, task.getIsStuDown());
        values.put(SystemConfig.ISSHOWRESULT, task.getIsShowResult());
        values.put(SystemConfig.ACTNUM, task.getActNum());


        String[] args = {String.valueOf(task.getTaskNum())};
        return db.update(TABLE_TASKINFO, values, "TaskNum=?" ,args);
    }

    /**
     * 删除本地公告
     * @param num AnnNum
     * @return 删除条数
     */
    public int delTaskInfo(int num){
        String[] args = {String.valueOf(num)};
        return  db.delete(TABLE_TASKINFO, "TaskNum=?", args);
    }

    /**
     * 将服务器端获取到的课程章节信息同步存储到本地数据
     * @param courses
     */
    public void saveTree(List<TreeEntity> courses){
        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < courses.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.TREENAME, courses.get(i).getTreeName());
            values.put(SystemConfig.COURNUM, courses.get(i).getCourNum());
            values.put(SystemConfig.TREEID, courses.get(i).getTreeid());
            db.insert(TABLE_TREE, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num  获取条数
     * @param id  用户id
     * @return
     */
    public ArrayList<TreeEntity> getTree(int num,String id){
        ArrayList<TreeEntity> cours= new ArrayList<>();
        TreeEntity tree;
        Cursor cursor=db.rawQuery("select * from "+TABLE_TREE+" where CourNum='"+id+"' order by Treeid asc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                tree = new TreeEntity(
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TREEID)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TREENAME))
                        );
                cours.add(tree);
            }while (cursor.moveToNext());
        }
        return cours;
    }

    /**
     * 从本地数据库最新一条TreeId
     * @return
     */
    public int getLatestTreeId(){
        Cursor cursor=db.rawQuery("select * from "+TABLE_TREE+" order by Treeid desc limit 0,1",null);
        if (cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex(SystemConfig.TREEID));
        }
        return 0;
    }

//end TaskInfo DB

//start AttendInfo DB
    /**
     * 将服务器端获取到的教学班信息同步存储到本地数据
     * @param acts
     */
    public void saveAct(List<ActEntity> acts){
        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < acts.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.ACTNUM, acts.get(i).getActNum());
            values.put(SystemConfig.COURNUM, acts.get(i).getCourNum());
            values.put(SystemConfig.CLASSNAME, acts.get(i).getClassName());
            db.insert(TABLE_ACT, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num  获取条数
     * @param id  用户id
     * @return
     */
    public ArrayList<ActEntity> getAct(int num,String id){
        ArrayList<ActEntity> acts= new ArrayList<>();
        ActEntity act;
        Cursor cursor=db.rawQuery("select * from "+TABLE_ACT+" where CourNum='"+id+"' order by ActNum desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                act = new ActEntity(
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.CLASSNAME))
                );
                acts.add(act);
            }while (cursor.moveToNext());
        }
        return acts;
    }

    /**
     * @return 从本地数据库获取最新活动班的id
     */
    public String getLatestAct(){
        Cursor cursor=db.rawQuery("select * from "+TABLE_ACT+"  order by ActNum desc limit 0,1",null);
        if (cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(SystemConfig.ACTNUM));
        }
        return "0";
    }

    /**
     * 从本地数据库获取num条数据
     * @param num  获取条数
     * @return
     */
    public ArrayList<ActEntity> getAct(int num){
        ArrayList<ActEntity> acts= new ArrayList<>();
        ActEntity act;
        Cursor cursor=db.rawQuery("select * from "+TABLE_ACT+"  order by ActNum desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                act = new ActEntity(
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.CLASSNAME))
                );
                acts.add(act);
            }while (cursor.moveToNext());
        }
        return acts;
    }

    /**
     * 将服务器端获取到的考勤信息同步存储到本地数据
     * @param attends
     */
    public void saveAttendInfo(List<AttendInfoEntity> attends){
        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < attends.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.ATTDENCENUM,attends.get(i).getAttdenceNum());
            values.put(SystemConfig.STATUSTIME,attends.get(i).getStatusTime());
            values.put(SystemConfig.TEACHNUM,attends.get(i).getTeachNum());
            values.put(SystemConfig.ACTNUM,attends.get(i).getActNum());
            values.put(SystemConfig.ATTOPEN,attends.get(i).getAttOpen());
            values.put(SystemConfig.ATTDENCECLASS,attends.get(i).getAttdenceClass());
            values.put(SystemConfig.ATTDENCEWEEK,attends.get(i).getAttdenceWeek());
            values.put(SystemConfig.PLACENAME,attends.get(i).getPlaceName());
            values.put(SystemConfig.REMARK,attends.get(i).getRemark());
            db.insert(TABLE_ATTENDADMIN, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 将插入一条数据
     * @param attend
     */
    public void insertAttendInfo(AttendInfoEntity attend){

        ContentValues values=null;
        db.beginTransaction();
        values=new ContentValues();
        values.put(SystemConfig.ATTDENCENUM,attend.getAttdenceNum());
        values.put(SystemConfig.STATUSTIME,attend.getStatusTime());
        values.put(SystemConfig.TEACHNUM,attend.getTeachNum());
        values.put(SystemConfig.ACTNUM,attend.getActNum());
        values.put(SystemConfig.ATTOPEN,attend.getAttOpen());
        values.put(SystemConfig.ATTDENCECLASS,attend.getAttdenceClass());
        values.put(SystemConfig.ATTDENCEWEEK,attend.getAttdenceWeek());
        values.put(SystemConfig.PLACENAME, attend.getPlaceName());
        values.put(SystemConfig.REMARK,attend.getRemark());

        db.insert(TABLE_ATTENDADMIN, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num  获取条数
     * @param id  用户id
     * @return
     */
    public ArrayList<AttendInfoEntity> getAttendInfo(int num,String id){
        ArrayList<AttendInfoEntity> attends= new ArrayList<>();
        AttendInfoEntity attend;
        Cursor cursor=db.rawQuery("select * from "+TABLE_ATTENDADMIN+" a,"+TABLE_COURSE+" c,"+TABLE_ACT+" b where a.ActNum=b.ActNum and c.CourNum=b.CourNum and TeachNum ="+ id +" order by AttdenceNum desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                attend = new AttendInfoEntity(cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCENUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STATUSTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTOPEN)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCECLASS)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCEWEEK)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.PLACENAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.REMARK)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.CLASSNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)));
                attends.add(attend);
            }while (cursor.moveToNext());
        }
        return attends;
    }

    /**
     * 更新本地考勤信息
     * @param attend
     * @return 更新条数
     */
    public int updateAttendInfo(AttendInfoEntity attend){
        ContentValues values=new ContentValues();
        values.put(SystemConfig.STATUSTIME,attend.getStatusTime());
        values.put(SystemConfig.TEACHNUM,attend.getTeachNum());
        values.put(SystemConfig.ACTNUM,attend.getActNum());
        values.put(SystemConfig.ATTOPEN,attend.getAttOpen());
        values.put(SystemConfig.ATTDENCECLASS,attend.getAttdenceClass());
        values.put(SystemConfig.ATTDENCEWEEK,attend.getAttdenceWeek());
        values.put(SystemConfig.PLACENAME,attend.getPlaceName());
        values.put(SystemConfig.REMARK,attend.getRemark());

        String[] args = {attend.getAttdenceNum()};
        return db.update(TABLE_ATTENDADMIN, values, "ATTDENCENUM=?" ,args);
    }

    /**
     * 删除本地考勤信息
     * @param num AnnNum
     * @return 删除条数
     */
    public int delAttendInfo(String num){
        String[] args = {num};
        return  db.delete(TABLE_ATTENDADMIN, "ATTDENCENUM=?", args);
    }

    /**
     * @return 返回本地可以更新的出勤记录 去服务器比较
     */
    public ArrayList<AttendInfoEntity> getNeedUpdateAttendInfo(String name){
        ArrayList<AttendInfoEntity> attends= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_ATTENDADMIN+" where AttOpen<>2 and TeachNum="+name,null);
        if (cursor.moveToFirst()){
            do {
                AttendInfoEntity attend = new AttendInfoEntity(
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCENUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.STATUSTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ACTNUM)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ATTOPEN)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCECLASS)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ATTDENCEWEEK)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.PLACENAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.REMARK)));
                attends.add(attend);
            }while (cursor.moveToNext());
        }
        return attends;
    }

    /**
     * 同步更新本地考勤状态
     * @param num
     * @param open
     * @return 更新条数
     */
    public int updateAttendInfo(String num,int open){
        ContentValues values=new ContentValues();
        values.put(SystemConfig.ATTOPEN,open);
        String[] args = {num};
        return db.update(TABLE_ATTENDADMIN, values, "ATTDENCENUM=?" ,args);
    }
//end AttendInfo DB
}
