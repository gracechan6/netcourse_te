package pers.nbu.netcoursetea.config;

import android.os.Environment;

/**
 * Created by gracechan on 2015/11/23.
 */
public class SystemConfig {
    //ENCODING------GBK
    public static final String SERVER_CHAR_SET="UTF-8";

    //=======================URL config
    public static final String SERVER_IP = "10.22.152.114";//192.168.137.1  10.22.152.114
    public static final String SERVER_PORT = "8080";
    public static final String URL_BASE = "http://" + SERVER_IP + ":" + SERVER_PORT;
    public static final String URL_ALLANN = URL_BASE + "/netcourse/getAllAnnounInfo.action";
    public static final String URL_UPDATEANN = URL_BASE + "/netcourse/getAnnounInfoByNum.action";

    public static final String URL_GETTASK = URL_BASE + "/netcourse/getAllTask.action";
    public static final String URL_GETTASKM = URL_BASE + "/netcourse/getAllTaskManage.action";
    public static final String URL_UPDATETASKM = URL_BASE + "/netcourse/updateTaskManage.action";
    public static final String URL_GETATTEND = URL_BASE + "/netcourse/getAttend.action";
    public static final String URL_UPDATEATTEND = URL_BASE + "/netcourse/updateAttend.action";
    public static final String URL_UPDATESERVERATTEND = URL_BASE + "/netcourse/updateServerAttend.action";

    public static final String URL_LOGINVAILD = URL_BASE + "/netcourse/loginVaildT.action";
    public static final String URL_GETANN = URL_BASE + "/netcourse/getAnn.action";
    public static final String URL_DELANN = URL_BASE + "/netcourse/delAnn.action";
    public static final String URL_ALTERANN = URL_BASE + "/netcourse/updateAnn.action";
    public static final String URL_ADDANN = URL_BASE + "/netcourse/addAnn.action";
    public static final String URL_GETCOURSE = URL_BASE + "/netcourse/getCourse.action";

    public static final String URL_GETTASKINFO = URL_BASE + "/netcourse/getTaskInfo.action";
    public static final String URL_DELTASKINFO = URL_BASE + "/netcourse/delTaskInfo.action";
    public static final String URL_UPDATETASKINFO = URL_BASE + "/netcourse/updateTaskInfo.action";
    public static final String URL_ADDTASKINFO = URL_BASE + "/netcourse/addTaskInfo.action";

    public static final String URL_GETATTENDINFO = URL_BASE + "/netcourse/getAttendInfo.action";
    public static final String URL_DELATTENDINFO = URL_BASE + "/netcourse/delAttendInfo.action";
    public static final String URL_UPDATEATTENDINFO = URL_BASE + "/netcourse/updateAttendInfo.action";
    public static final String URL_ADDATTENDINFO = URL_BASE + "/netcourse/addAttendInfo.action";
    public static final String URL_UPDATEANDROIDATTENDINFO = URL_BASE + "/netcourse/updateAndroidAttendInfo.action";

    public static final String URL_GETTREE = URL_BASE + "/netcourse/getTree.action";
    public static final String URL_GETACT = URL_BASE + "/netcourse/getAct.action";


    //=================params
        //LOGIN
    public static final String LOGINNAME="name";
    public static final String LOGINPWD="pwd";
        //-----ANN
    public static final String ANNTITLE="AnnTitle";
    public static final String ANNCON="AnnCon";
    public static final String ANNTIME="AnnTime";
    public static final String ANNURL="AnnUrl";
    public static final String TEACHNAME="TeachName";
    public static final String TEACHNUM="TeachNum";
    public static final String COURNAME="CourName";
    public static final String COURNUM="CourNum";
    public static final String ANNNUM="AnnNum";
    public static final String ANNID="AnnId";
    public static final String ANNTYPE="AnnType";

        //------TASK
    public static final String ENDTIME="EndTime";
    public static final String TASKTIME="TaskTime";
    public static final String TASKTITLE="TaskTitle";
    public static final String TASKNUM="TaskNum";
    public static final String TASKID="TaskId";
    public static final String TASKREQUIRE="TaskRequire";
    public static final String YORNSUB="YorNSub";
    public static final String YORNVIS="YorNVis";
    public static final String TASKURL="TaskUrl";
    public static final String FILEON="FileOn";
    public static final String VIDEO="Video";
    public static final String ANNEX="Annex";
    public static final String ISSTUDOWN="IsStuDown";
    public static final String ISSHOWRESULT="IsShowResult";
    public static final String TREENAME="TreeName";


        //------TASKMANAGE
    public static final String TREEID="Treeid";
    public static final String OPUSNUM="OpusNum";

        //Attend
    public static final String ATTDENCEID="AttdenceId";
    public static final String ATTDENCENUM="AttdenceNum";
    public static final String ACTNUM="ActNum";
    public static final String PLACENAME="PlaceName";
    public static final String ATTDENCEWEEK="AttdenceWeek";
    public static final String STATUSTIME="StatusTime";
    public static final String STANAME="StaName";
    public static final String STATUS="Status";
    public static final String ATTDENCECLASS="AttdenceClass";
    public static final String UPDATENUM="UpdateNum";
    public static final String CLASSNAME="ClassName";
    public static final String REMARK="Remark";
    public static final String ATTOPEN="AttOpen";

    //本地头像保存的路径
    public static final String PATH_HEAD= Environment.getExternalStorageDirectory()+"/VClassHead/";
    public static final String HEAD_TYPE= ".png";

}
