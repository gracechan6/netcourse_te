package pers.nbu.netcoursetea;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Chenss on 2015/10/27.
 */
public class DateTransform {

    public final static Date string2Date(String dateString){

        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:MM", Locale.CHINESE);

        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取现在时间
     * @return返回字符串格式 yyyy年MM月dd日,周x,HH:mm
     */

    public static String getStringNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日,EE,HH:mm",Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将String类型时间转成Date类型
     * @param dateString
     * @return
     */
    public static Date makeString2Date(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日,EE,HH:mm",Locale.getDefault());
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int compareInterval(Date date1,Date date2){
        return Math.abs((int)(((date1.getTime() - date2.getTime())/(1000*60))));
    }

    public static String isThisYear(String dataString) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy",Locale.getDefault());
        String year = formatter.format(currentTime);

        if (year.equals(dataString.substring(0,4))){
            return dataString.substring(5,dataString.length());
        }
        else
            return dataString;
    }

}
