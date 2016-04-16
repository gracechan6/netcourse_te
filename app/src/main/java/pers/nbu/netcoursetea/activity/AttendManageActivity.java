package pers.nbu.netcoursetea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcoursetea.BaseApplication;
import pers.nbu.netcoursetea.JsonTransform;
import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.adapter.AttendAdapter;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.AttendEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class AttendManageActivity extends BaseActivity {

    private SwipeRefreshLayout attendLayout;
    private SwipeMenuListView attendLsv;
    private AttendAdapter attendAdapter;
    private ArrayList<AttendEntity> attendLists = new ArrayList<>();

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private int showNum=8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_manage);

        initToolBar();
        setTitle("考勤管理");
        setRightOfToolbar(true);
        setLeftOfToolbar(true);
        initView();
        initData(1);
    }


    private void initView(){
        attendLayout = (SwipeRefreshLayout) findViewById(R.id.attendLayout);
        attendLayout.setOnRefreshListener(attendShowRefresh);
        attendLsv = (SwipeMenuListView) findViewById(R.id.attendLsv);
        attendAdapter = new AttendAdapter(attendLists,getApplicationContext());
        attendLsv.setAdapter(attendAdapter);
        attendLsv.setOnItemClickListener(attendClickListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAttendFromDB(showNum);
    }

    /**
     * 初始化数据
     * @param flag    区分下拉刷新2还是首次1
     */
    private void initData(int flag){
        //获取当前出勤的最后一条消息的AttendNum，与服务器端比较看最新消息是否为这个id
        //是，则不操作,否，获取此id后的所有消息，然后保存到本地并更新进度
        ArrayList<AttendEntity> arrayList = db.getAttend(1);
        if (arrayList != null && arrayList.size()>0) {
            LogUtil.d("test", String.valueOf(arrayList.get(0).getAttdenceNum()));
            getAttend(flag, PreferenceUtils.getUserId(getApplicationContext()),arrayList.get(0).getAttdenceNum());
        }
        else
            getAttend(flag, PreferenceUtils.getUserId(getApplicationContext()),0);
    }

    SwipeRefreshLayout.OnRefreshListener attendShowRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initData(2);
        }
    };

    /**
     * 更新本地出勤显示数据
     * @param show   从本地数据库获取条数
     */
    protected void getAttendFromDB(int show){
        attendLists.clear();
        attendLists.addAll(db.getAttend(show));
        if (attendLists.size()>= 8 && attendLists.size()<db.countData(db.TABLE_TASKMANAGESHOW)) {
            attendLists.add(new AttendEntity("LOADINGMORE"));
        }
        attendAdapter.notifyDataSetChanged();
    }

    /**
     * 从服务器端获取数据
     * @param flag   区分下拉刷新2还是初始化获取1
     * @param userNum    用户名
     * @param attendNum   考勤号
     */
    protected void getAttend(final int flag,String userNum,int attendNum){
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams();
        params.put("UserNum", userNum);
        params.put("AttendNum",attendNum);
        client.post(SystemConfig.URL_GETATTEND,params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                super.onStart();
                if (flag==1){dialog.show();}
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.turnToAttendMLists(response)) {
                            getAttendFromDB(showNum);
                        }
                        LogUtil.i("test", getClass().getSimpleName()+"true,获取数据成功");
                    } else {
                        if (flag==1) {
                            LogUtil.i("test",getClass().getSimpleName()+"服务器端未有新的出勤记录!");
                            getAttendFromDB(showNum);
                        }
                        else Toast.makeText(getApplicationContext(), "已是最新", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "连接失败！");
                if (flag==1)
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                if (flag==1){dialog.dismiss();}
                else attendLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 左上角图标刷新操作
     * 更新本地出勤，去服务器端查询当前出勤记录是否已经结束，结束返回
     * 该学生对应出勤id下的出勤情况，然后本地这边比较下 看是否有更新
     */
    @Override
    public void doRefreshClick() {
        ArrayList<AttendEntity> lists=db.getNeedUpdateAttend();
        if (lists!=null && lists.size()>0){
            AsyncHttpClient client = ((BaseApplication) getApplication()).getSharedHttpClient();
            RequestParams params;
            for(int i=0;i<lists.size();i++) {
                params = new RequestParams();
                params.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
                params.put("AttendNum", lists.get(i).getAttdenceNum());
                LogUtil.i("test",getClass().getSimpleName()+"AttendNum:"+lists.get(i).getAttdenceNum());
                client.post(SystemConfig.URL_UPDATEATTEND, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                int var;
                                if (response.getString("staName").equals("已结束考勤")){
                                    var = db.updateAttend(response.getInt("AttendNum"),response.getString("staName"),
                                            response.getString("status"),1000);
                                }else{
                                    var = db.updateAttend(response.getInt("AttendNum"),response.getString("staName"),
                                            response.getString("status"),0);
                                }
                                if (var > 0) getAttendFromDB(showNum);
                                LogUtil.i("test", getClass().getSimpleName() + "成功更新本地列表，更新数量：" + var);
                            } else {
                                LogUtil.i("test", getClass().getSimpleName() + "无法更新");
                                //Toast.makeText(getApplicationContext(), "已是最新", Toast.LENGTH_SHORT).show();
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
    }

    protected AdapterView.OnItemClickListener attendClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position+1==attendLists.size()){
                showNum +=7;
                getAttendFromDB(showNum);
            }
            else{
                Intent intent = new Intent(getApplicationContext(),AttendDetailActivity.class);
                intent.putExtra(SystemConfig.ATTDENCENUM,attendLists.get(position).getAttdenceNum());
                intent.putExtra(SystemConfig.PLACENAME,attendLists.get(position).getPlaceName());
                intent.putExtra(SystemConfig.COURNAME,attendLists.get(position).getCourName());
                intent.putExtra(SystemConfig.TEACHNAME,attendLists.get(position).getTeachName());
                intent.putExtra(SystemConfig.ATTDENCEWEEK,attendLists.get(position).getAttdenceWeek());
                intent.putExtra(SystemConfig.STATUSTIME,attendLists.get(position).getStatusTime());
                intent.putExtra(SystemConfig.STANAME,attendLists.get(position).getStaName());
                intent.putExtra(SystemConfig.STATUS,attendLists.get(position).getStatus());
                intent.putExtra(SystemConfig.ATTDENCECLASS,attendLists.get(position).getAttdenceClass());
                startActivity(intent);
            }
        }
    };
}
