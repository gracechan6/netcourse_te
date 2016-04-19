package pers.nbu.netcoursetea.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Chenss on 2015/10/27.
 */
public class PhoneScreenUtils {

    /**
     * 获取屏幕宽度
     */
    public static final int getScreenWidth(Context context)
    {
        DisplayMetrics metric = getWindowInfo(context);
        return metric.widthPixels;
    }

    /**
     * 获取屏幕属性
     */
    public static final DisplayMetrics getWindowInfo(Context context)
    {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /**
     * 获取屏幕高度
     */
    public static final int getScreenHeight(Context context)
    {
        DisplayMetrics metric = getWindowInfo(context);
        return metric.heightPixels;
    }

    /*
   * 获取控件宽
   */
    public static int getWidgetWidth(View view)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /*
    * 获取控件高
    */
    public static int getWidgetHeight(View view)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }

}

