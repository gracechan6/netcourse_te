package pers.nbu.netcoursetea.util;

import android.util.Log;

/**
 * Created by Chenss on 2015/10/16.
 */
public class LogUtil {

    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    public static final int LEVEL = 20 ;
    //最后只需将LEVEL 置为NOTHING,程序运行时则不会打印日志

    public static void v(String tag,String msg){
        if (LEVEL <= VERBOSE)
            Log.v(tag,msg);
    }

    public static void d(String tag,String msg){
        if (LEVEL <= DEBUG)
            Log.d(tag,msg);
    }

    public static void i(String tag,String msg){
        if (LEVEL <= INFO)
            Log.i(tag,msg);
    }

    public static void w(String tag,String msg){
        if (LEVEL <= WARN)
            Log.w(tag,msg);
    }

    public static void e(String tag,String msg){
        if (LEVEL <= ERROR)
            Log.e(tag,msg);
    }
}
