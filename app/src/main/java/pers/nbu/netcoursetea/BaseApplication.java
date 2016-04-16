package pers.nbu.netcoursetea;

import android.app.Application;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by Chenss on 2015/10/30.
 */
public class BaseApplication extends Application {


    private AsyncHttpClient sharedHttpClient;

    private static Context context;

    public static Context getContext() {
        return context;
    }
    public AsyncHttpClient getSharedHttpClient(){
        return sharedHttpClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sharedHttpClient = new AsyncHttpClient();
    }
}
