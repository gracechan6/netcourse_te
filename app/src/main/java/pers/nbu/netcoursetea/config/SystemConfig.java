package pers.nbu.netcoursetea.config;

import android.os.Environment;

/**
 * Created by gracechan on 2015/11/23.
 */
public class SystemConfig {
    //ENCODING------GBK
    public static final String SERVER_CHAR_SET="UTF-8";

    //=======================URL config
    public static final String SERVER_IP = "192.168.137.1";//192.168.137.1  10.22.152.114
    public static final String SERVER_PORT = "8080";
    public static final String URL_BASE = "http://" + SERVER_IP + ":" + SERVER_PORT;
    public static final String URL_ALLANN = URL_BASE + "/netcourse/getAllAnnounInfo.action";
    public static final String URL_UPDATEANN = URL_BASE + "/netcourse/getAnnounInfoByNum.action";
    public static final String URL_GETTASK = URL_BASE + "/netcourse/getAllTask.action";
    public static final String URL_GETTASKM = URL_BASE + "/netcourse/getAllTaskManage.action";
    public static final String URL_UPDATETASKM = URL_BASE + "/netcourse/updateTaskManage.action";
    public static final String URL_LOGINVAILD = URL_BASE + "/netcourse/loginVaildT.action";
    public static final String URL_GETATTEND = URL_BASE + "/netcourse/getAttend.action";
    public static final String URL_UPDATEATTEND = URL_BASE + "/netcourse/updateAttend.action";
    public static final String URL_UPDATESERVERATTEND = URL_BASE + "/netcourse/updateServerAttend.action";


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
    public static final String COURNAME="CourName";
    public static final String ANNNUM="AnnNum";
    public static final String ANNID="AnnId";

        //------TASK
    public static final String ENDTIME="EndTime";
    public static final String TASKTIME="TaskTime";
    public static final String TASKTITLE="TaskTitle";
    public static final String TASKNUM="TaskNum";
    public static final String TASKID="TaskId";
    public static final String TASKREQUIRE="TaskRequire";

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

    //本地头像保存的路径
    public static final String PATH_HEAD= Environment.getExternalStorageDirectory()+"/VClassHead/";
    public static final String HEAD_TYPE= ".png";

}
