package pers.nbu.netcoursetea.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
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
import pers.nbu.netcoursetea.ToastMsg;
import pers.nbu.netcoursetea.adapter.AttendInfoAdapter;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.ActEntity;
import pers.nbu.netcoursetea.entity.AttendInfoEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PhoneScreenUtils;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class AttendManaActivity extends BaseActivity {

    private SwipeRefreshLayout attendLayout;
    private SwipeMenuListView attendLsv;
    private AttendInfoAdapter attendAdapter;
    private ArrayList<AttendInfoEntity> attendLists ;
    private SwipeMenuCreator creator;

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private int showNum=8;
    private int end=0,countSuccess=0,successdGet=0;//successGet 初始化获取判断
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_mana);

        initToolBar();
        setTitle("考勤管理");
        setRightOfToolbar(true);
        setLeft("新考勤");

        initView();
        updateLocalDate();
    }

    /**
     * 初始化View
     */
    private void initView(){
        attendLayout = (SwipeRefreshLayout) findViewById(R.id.attendLayout);
        attendLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initCourse(1);
            }
        });
        attendLsv = (SwipeMenuListView) findViewById(R.id.attendLsv);
//        annLsv.setScrollbarFadingEnabled(true);//滚动条隐藏
        attendLists = new ArrayList<>();

        attendAdapter = new AttendInfoAdapter(attendLists,getApplicationContext());
        initCreator();
        if (creator != null)
            attendLsv.setMenuCreator(creator);
        attendLsv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        //去服务器端删除该考勤记录，返回true，本地数据库也删除该考勤
                        dialog.show();
                        LogUtil.i("test", getClass().getSimpleName() + "删除当前考勤,id:" + attendLists.get(position).getAttdenceNum());
                        delAttend(attendLists.get(position).getAttdenceNum());
                        break;
                    case 1:
                        // 未设置
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        attendLsv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        attendLsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //点击公告进入公告详情页查看
                if (position + 1 == attendLists.size() && attendLists.get(position).getAttdenceNum().equals("LOADINGMORE")) {
                    showNum += 7;
                    getAttendFromDB();
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewAttendActivity.class);
                    intent.putExtra(SystemConfig.ATTDENCENUM, attendLists.get(position).getAttdenceNum());
                    intent.putExtra(SystemConfig.STATUSTIME, attendLists.get(position).getStatusTime());
                    intent.putExtra(SystemConfig.TEACHNAME, attendLists.get(position).getTeachNum());
                    intent.putExtra(SystemConfig.ACTNUM, attendLists.get(position).getActNum());
                    intent.putExtra(SystemConfig.ATTOPEN, attendLists.get(position).getAttOpen());
                    intent.putExtra(SystemConfig.ATTDENCECLASS, attendLists.get(position).getAttdenceClass());
                    intent.putExtra(SystemConfig.ATTDENCEWEEK, attendLists.get(position).getAttdenceWeek());
                    intent.putExtra(SystemConfig.PLACENAME, attendLists.get(position).getPlaceName());
                    intent.putExtra(SystemConfig.REMARK, attendLists.get(position).getRemark());
                    intent.putExtra(SystemConfig.CLASSNAME, attendLists.get(position).getClassName());
                    intent.putExtra(SystemConfig.COURNAME, attendLists.get(position).getCourName());
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                }
            }
        });
        attendLsv.setAdapter(attendAdapter);
    }

    /**
     * 从服务器获取数据更新本地数据库
     * @param flag 区分下拉刷新 1 还是进入更新2
     */
    private void initData(final int flag){
        ArrayList<AttendInfoEntity> arrayList=db.getAttendInfo(1, PreferenceUtils.getUserId(getApplicationContext()));
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams();
        params.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        if (arrayList != null && arrayList.size()>0)
            params.put("AttdenceNum",arrayList.get(0).getAttdenceNum());
        else
            params.put("AttdenceNum",0);

        client.post(SystemConfig.URL_GETATTENDINFO, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                if (flag == 2)
                    dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toAttendInfoLists(response)) {
                            getAttendFromDB();
                        }
                        LogUtil.i("success", getClass().getSimpleName() + "Attendtrue");
                    } else {
                        if (flag == 2) {
                            LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的任务发布!");
                            getAttendFromDB();
                        } else ToastMsg.showToast("已是最新");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "attend连接失败！");
                if (flag == 2) {
                    ToastMsg.showToast("连接失败");
                    getAttendFromDB();
                } else ToastMsg.showToast("刷新失败");
            }


            @Override
            public void onFinish() {
                if (flag == 2)
                    dialog.dismiss();
                else
                    attendLayout.setRefreshing(false);
            }
        });

    }

    /**
     * 列表添加删除按钮
     */
    private void initCreator(){
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                //create 删除 item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(PhoneScreenUtils.getScreenWidth(getApplicationContext())/6);
                // set a icon
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(18);

                // add to menu
                swipeMenu.addMenuItem(deleteItem);
            }
        };
    }

    private void getAttendFromDB(){
        attendLists.clear();
        attendLists.addAll(db.getAttendInfo(showNum, PreferenceUtils.getUserId(getApplicationContext())));
        if(attendLists.size()>=8 && attendLists.size()<db.countData(db.TABLE_ATTENDADMIN)){
            attendLists.add(new AttendInfoEntity("LOADINGMORE"));
        }
        attendAdapter.notifyDataSetChanged();
    }

    /**
     * 去服务器删除该条考勤记录
     * @param num attendNum
     */
    private void delAttend(final String num){
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams("AttdenceNum",num);

        client.post(SystemConfig.URL_DELATTENDINFO, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && db.delAttendInfo(num) > 0) {
                        LogUtil.i("test", getClass().getSimpleName() + "删除考勤成功,id:" + num);
                        getAttendFromDB();
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端出现未知错误 无法删除该条考勤记录!");
                        ToastMsg.showToast("服务器端出现未知错误 无法删除该条考勤记录!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "attend连接失败！");
                ToastMsg.showToast("连接失败");
            }


            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }


    /**
     * 因为adapter中有教学活动班的className这一参数，因此先去获取活动班信息
     * @param flag    刷新1还是 初始化2
     */
    private void initAct(final int flag){
        ArrayList<ActEntity> acts=new ArrayList<>();
        acts=db.getAct(1);
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        if (acts!= null && acts.size()>0)
            requestParams.put("ActNum",acts.get(0).getActNum());
        else
            requestParams.put("ActNum", 0);
        client.post(SystemConfig.URL_GETACT, requestParams, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                if (flag == 2)
                    dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toActLists(response)) {
                            LogUtil.i("success", getClass().getSimpleName() + "本地数据库有新的活动班信息");
                        }
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的活动班信息发布!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "Act连接失败！");
                ToastMsg.showToast("连接失败,无法获取更多的教学班信息");
            }

            @Override
            public void onFinish() {
                initData(flag);
            }
        });
    }

    /**
     * 获取本地数据库  考勤信息时 表需要与课程表相连
     * 因此必须先更新本地课程表，以免没有数据无法获取到
     */
    private void initCourse(final int flag){
        ArrayList<CourseEntity> cous=new ArrayList<>();
        cous=db.getCourse(1, PreferenceUtils.getUserId(getApplicationContext()));
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        if (cous!= null && cous.size()>0)
            requestParams.put("Treeid",cous.get(0).getTreeid());
        else
            requestParams.put("Treeid", 0);
        client.post(SystemConfig.URL_GETCOURSE,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toCourseLists(response)) {
                            LogUtil.i("success", getClass().getSimpleName() + "更新本地Course成功");
                        }

                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的课程信息发布!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "Course连接失败！");
                ToastMsg.showToast("连接失败,无法获取更多的课程信息");
            }

            @Override
            public void onFinish() {
                initAct(flag);
            }
        });
    }
    /**
     * 新建考勤或者更新考勤后返回操作
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        getAttendFromDB();
    }

    /**
     * 新建考勤
     */
    @Override
    public void doLeftClick() {
        startActivity(new Intent(this, NewAttendActivity.class));
    }


    /**
     * 将本地未开始考勤 以及正在考勤中的数据同服务器端数据比较
     */
    private void updateLocalDate(){
        dialog.show();
        final ArrayList<AttendInfoEntity> lists=db.getNeedUpdateAttendInfo(PreferenceUtils.getUserId(getApplicationContext()));
        if (lists!=null && lists.size()>0){
            end=lists.size()+2;
            AsyncHttpClient client = ((BaseApplication) getApplication()).getSharedHttpClient();
            RequestParams params;
            for(int i=0;i<lists.size();i++) {
                params = new RequestParams();
                params.put("AttdenceNum", lists.get(i).getAttdenceNum());
                client.post(SystemConfig.URL_UPDATEANDROIDATTENDINFO, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                int var;
                                var = db.updateAttendInfo(response.getString("AttdenceNum"),response.getInt("AttOpen"));
                                LogUtil.i("test", getClass().getSimpleName() + "成功更新本地列表，更新数量：" + var);
                            } else {
                                LogUtil.i("test", getClass().getSimpleName() + "无法更新");
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
                        countSuccess++;
                        if (countSuccess == end -2)
                            initCourse(2);
                    }
                });
            }
        }
        else{
            end = 2;initCourse(2);
        }
    }
}
