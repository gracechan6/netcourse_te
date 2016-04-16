package pers.nbu.netcoursetea.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcoursetea.BaseApplication;
import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.AttendEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class AttendDetailActivity extends BaseActivity {

    private TextView pName,cName,tName,week,time,staName,status,type;
    private Button submit;

    private int AttendNum;
    private String sta,statu;

    private ColorStateList color;

    private DB db=DB.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_detail);

        initToolBar();
        setTitle("考勤签到情况");
        setRightOfToolbar(true);
        setLeftOfToolbar(true);
        initView();
        AttendNum = getIntent().getIntExtra(SystemConfig.ATTDENCENUM,-1);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        pName = (TextView) findViewById(R.id.pName);
        cName = (TextView) findViewById(R.id.cName);
        tName = (TextView) findViewById(R.id.tName);
        week = (TextView) findViewById(R.id.week);
        time = (TextView) findViewById(R.id.time);
        staName = (TextView) findViewById(R.id.staName);
        status = (TextView) findViewById(R.id.status);
        type = (TextView) findViewById(R.id.type);

        color=status.getTextColors();

        submit = (Button) findViewById(R.id.submit);

        sta=getIntent().getStringExtra(SystemConfig.STANAME);
        statu=getIntent().getStringExtra(SystemConfig.STATUS);

        pName.setText("发布地点："+getIntent().getStringExtra(SystemConfig.PLACENAME));
        cName.setText("课程："+getIntent().getStringExtra(SystemConfig.COURNAME));
        tName.setText("发布人："+getIntent().getStringExtra(SystemConfig.TEACHNAME));
        week.setText(getIntent().getStringExtra(SystemConfig.ATTDENCEWEEK)+"考勤");
        time.setText("考勤时间："+getIntent().getStringExtra(SystemConfig.STATUSTIME));
        staName.setText("考勤状态："+sta);
        if (getIntent().getStringExtra(SystemConfig.STATUS).equals("缺课"))
            status.setTextColor(getResources().getColor(R.color.red_select));

        status.setText("状态："+statu);
        type.setText("考勤类别："+getIntent().getStringExtra(SystemConfig.ATTDENCECLASS));

        if (sta.equals("正在考勤中") &&
                statu.equals("缺课")){
            submit.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 从本地重新获取该条记录 并更新界面
     */
    protected void freshView(){
        AttendEntity attend = db.getAttendByANum(AttendNum);

        sta=attend.getStaName();
        statu=attend.getStatus();
        staName.setText("考勤状态："+sta);
        if (sta.equals("缺课"))
            status.setTextColor(getResources().getColor(R.color.red_select));
        else
            status.setTextColor(color);
        status.setText("状态："+statu);

        if (sta.equals("正在考勤中") &&
                statu.equals("缺课")){
            submit.setVisibility(View.VISIBLE);
        }else
            submit.setVisibility(View.GONE);
    }

    /**
     * 签到按钮操作
     * @param view
     */
    public void doSubmit(View view){
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params=new RequestParams();
        params.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        params.put("AttendNum",AttendNum);
        String ip =getLocalIpAddress();
        if (ip.isEmpty())
            params.put("ip","fromMobile");
        else
            params.put("ip","'"+ip+"'");

        client.post(SystemConfig.URL_UPDATESERVERATTEND,params,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        int var = db.updateAttend(AttendNum,"已结束考勤","已到",1000);
                        if (var > 0) {
                            //从本地重新获取该条记录 并更新界面
                            freshView();
                        }
                        LogUtil.i("test", getClass().getSimpleName() + "成功更新本地列表，更新数量：" + var);
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "无法成功提交到服务器");
                        Toast.makeText(getApplicationContext(), "无法成功提交到服务器！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "连接失败！");
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }

    /**
     * @return ip地址
     */
    public String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        //LogUtil.e("WifiPreference IpAddress", inetAddress.getHostAddress().toString());
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            LogUtil.e("WifiPreference IpAddress", ex.toString());
        }
        return "";
    }


    @Override
    public void doRefreshClick() {
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params=new RequestParams();
        params.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        params.put("AttendNum", AttendNum);
        client.post(SystemConfig.URL_UPDATEATTEND, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !(response.getString("staName").equals(sta) && response.getString("status").equals(statu))) {
                        int var;
                        if (response.getString("staName").equals("已结束考勤")){
                            var = db.updateAttend(response.getInt("AttendNum"),response.getString("staName"),
                                    response.getString("status"),1000);
                        }else{
                            var = db.updateAttend(response.getInt("AttendNum"),response.getString("staName"),
                                    response.getString("status"),0);
                        }
                        if (var > 0){
                            freshView();
                            LogUtil.i("test", getClass().getSimpleName() + "成功更新本地列表，更新数量：" + var);
                        }
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "无法更新");
                        Toast.makeText(getApplicationContext(), "已是最新", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "连接失败！");
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }
}
