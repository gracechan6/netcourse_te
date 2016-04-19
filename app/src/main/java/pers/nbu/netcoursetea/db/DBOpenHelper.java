package pers.nbu.netcoursetea.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chenss on 2015/10/9.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public final String CREATE_BOOK_ANN = "create table tbAnn("
            + "AnnNum integer ,"
            + "AnnTitle varchar(100) ,"
            + "AnnCon text ,"
            + "AnnTime varchar(20) ,"
            + "AnnUrl varchar(200) ,"
            + "TeachNum varchar(20) ,"
            + "AnnType varchar(50) ,"
            + "Treeid integer)";

    public final String CREATE_BOOK_COURSE = "create table tbCourse("
            + "Treeid integer ,"
            + "CourNum varchar(50) ,"
            + "CourName varchar(100) )";

    public final String CREATE_BOOK_TREE = "create table tbTree("
            + "Treeid integer,"
            + "CourNum varchar(20) ,"
            + "TreeName varchar(100) )";

    public final String CREATE_BOOK_ACT = "create table tbAct("
            + "ActNum varchar(20),"
            + "CourNum varchar(20) ,"
            + "ClassName varchar(50) )";

    public final String CREATE_BOOK_ANNShow = "create table tbAnnShow("
            + "AnnId integer primary key autoincrement ,"
            + "AnnNum integer ,"
            + "AnnTitle varchar(100) ,"
            + "AnnCon text ,"
            + "AnnTime varchar(20) ,"
            + "AnnUrl varchar(200) ,"
            + "TeachName varchar(20) ,"
            + "CourName varchar(20))";

    public final String CREATE_BOOK_TASKINFO = "create table tbTaskInfo("
            + "TaskNum integer ,"
            + "TeachNum varchar(20) ,"
            + "TaskTitle varchar(100) ,"
            + "TaskRequire varchar(100) ,"
            + "YorNSub varchar(10),"
            + "YorNVis varchar(10),"
            + "TaskUrl varchar(200),"
            + "FileOn varchar(10),"
            + "Video varchar(10),"
            + "Annex varchar(10),"
            + "IsStuDown varchar(10) ,"
            + "IsShowResult varchar(10) ,"
            + "TaskTime varchar(20) ,"
            + "EndTime varchar(20),"
            + "ActNum varchar(20),"
            + "Treeid integer)";

    public final String CREATE_BOOK_ATTENDADMIN = "create table tbAttendAdmin("
            + "AttdenceNum varchar(20) ,"
            + "StatusTime varchar(50) ,"
            + "TeachNum varchar(20) ,"
            + "ActNum varchar(20) ,"
            + "AttOpen integer,"
            + "AttdenceClass varchar(50),"
            + "AttdenceWeek varchar(20),"
            + "PlaceName varchar(50),"
            + "Remark varchar(50))";
    
//    public final String CREATE_BOOK_TaskShow = "create table tbTaskShow("
//            + "TaskId integer primary key autoincrement ,"
//            + "TaskNum integer ,"
//            + "TaskTitle varchar(100) ,"
//            + "TaskRequire varchar(100) ,"
//            + "CourName varchar(50) ,"
//            + "TeachName varchar(20) ,"
//            + "TaskTime varchar(20) ,"
//            + "EndTime varchar(20))";
//
//    public final String CREATE_BOOK_TaskManageShow = "create table tbTaskManageShow("
//            + "TaskId integer primary key autoincrement ,"
//            + "TaskNum integer ,"
//            + "Treeid integer ,"
//            + "TeachName varchar(20) ,"
//            + "TaskTitle varchar(100) ,"
//            + "CourName varchar(50) ,"
//            + "TaskTime varchar(20) ,"
//            + "EndTime varchar(20),"
//            + "OpusNum integer ) ";
//
//    public final String CREATE_BOOK_AttendShow = "create table tbAttendShow("
//            + "AttdenceId integer primary key autoincrement ,"
//            + "AttdenceNum integer ,"
//            + "ActNum integer ,"
//            + "PlaceName varchar(20)  ,"
//            + "CourName varchar(50) ,"
//            + "TeachName varchar(20) ,"
//            + "AttdenceWeek varchar(20) ,"
//            + "StatusTime varchar(20) ,"
//            + "StaName varchar(20) ,"
//            + "Status varchar(10) ,"
//            + "AttdenceClass varchar(10),"
//            + "UpdateNum integer)";

    private Context context;

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_ANNShow);
        db.execSQL(CREATE_BOOK_ANN);
        db.execSQL(CREATE_BOOK_ACT);
        db.execSQL(CREATE_BOOK_TREE);
        db.execSQL(CREATE_BOOK_COURSE);
        db.execSQL(CREATE_BOOK_TASKINFO);
        db.execSQL(CREATE_BOOK_ATTENDADMIN);
//        db.execSQL(CREATE_BOOK_TaskShow);
//        db.execSQL(CREATE_BOOK_TaskManageShow);
//        db.execSQL(CREATE_BOOK_AttendShow);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
