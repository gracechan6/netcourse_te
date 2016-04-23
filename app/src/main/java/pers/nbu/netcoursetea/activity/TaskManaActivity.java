package pers.nbu.netcoursetea.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

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
import pers.nbu.netcoursetea.adapter.TaskInfoAdapter;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.ActEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.entity.TaskInfoEntity;
import pers.nbu.netcoursetea.entity.TaskManageEntity;
import pers.nbu.netcoursetea.entity.TreeEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PhoneScreenUtils;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class TaskManaActivity extends BaseActivity {


    private SwipeRefreshLayout taskLayout;
    private SwipeMenuListView taskLsv;
    private TaskInfoAdapter taskAdapter;
    private ArrayList<TaskInfoEntity> taskLists ;
    private SwipeMenuCreator creator;

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private int showNum=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_mana);

        initToolBar();
        setTitle("任务管理");
        setRightOfToolbar(true);
        setLeft("新任务");

        initView();
        initCourse(2);
    }
    /**
     * 初始化View
     */
    private void initView(){
        taskLayout = (SwipeRefreshLayout) findViewById(R.id.taskLayout);
        taskLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initCourse(1);
            }
        });
        taskLsv = (SwipeMenuListView) findViewById(R.id.taskLsv);
        taskLists = new ArrayList<>();

        taskAdapter = new TaskInfoAdapter(taskLists,getApplicationContext());
        initCreator();
        if (creator != null)
            taskLsv.setMenuCreator(creator);
        taskLsv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        //去服务器端删除该公告，返回true，本地数据库也删除该公告
                        LogUtil.i("test", getClass().getSimpleName() + "删除当前任务,id:" + taskLists.get(position).getTaskNum());
                        delTask(taskLists.get(position).getTaskNum());
                        break;
                    case 1:
                        // 未设置
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        taskLsv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        taskLsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //点击公告进入任务详情页查看
                if (position + 1 == taskLists.size() && taskLists.get(position).getTaskTitle().equals("LOADINGMORE")) {
                    showNum += 7;
                    getTaskFromDB();
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                    intent.putExtra(SystemConfig.TASKNUM, taskLists.get(position).getTaskNum());
                    intent.putExtra(SystemConfig.TASKTITLE, taskLists.get(position).getTaskTitle());
                    intent.putExtra(SystemConfig.TASKREQUIRE, taskLists.get(position).getTaskRequire());
                    intent.putExtra(SystemConfig.YORNSUB, taskLists.get(position).getYorNSub());
                    intent.putExtra(SystemConfig.YORNVIS, taskLists.get(position).getYorNVis());
                    intent.putExtra(SystemConfig.TASKURL, taskLists.get(position).getTaskUrl());
                    intent.putExtra(SystemConfig.FILEON, taskLists.get(position).getFileOn());
                    intent.putExtra(SystemConfig.VIDEO, taskLists.get(position).getVideo());
                    intent.putExtra(SystemConfig.ANNEX, taskLists.get(position).getAnnex());
                    intent.putExtra(SystemConfig.TASKTIME, taskLists.get(position).getTaskTime());
                    intent.putExtra(SystemConfig.ENDTIME, taskLists.get(position).getEndTime());
                    intent.putExtra(SystemConfig.ISSTUDOWN, taskLists.get(position).getIsStuDown());
                    intent.putExtra(SystemConfig.ISSHOWRESULT, taskLists.get(position).getIsShowResult());
                    intent.putExtra(SystemConfig.TREEID, taskLists.get(position).getTreeid());
                    intent.putExtra(SystemConfig.ACTNUM, taskLists.get(position).getActNum());
                    intent.putExtra(SystemConfig.TREENAME, taskLists.get(position).getTreeName());
                    intent.putExtra(SystemConfig.COURNAME, taskLists.get(position).getCourseName());
                    intent.putExtra(SystemConfig.CLASSNAME, taskLists.get(position).getClassName());

                    intent.putExtra("flag", 1);
                    startActivity(intent);
                }
            }
        });
        taskLsv.setAdapter(taskAdapter);
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


    /**
     * 新建任务或者更新任务后返回操作
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        getTaskFromDB();
    }

    /**
     * 新建任务
     */
    @Override
    public void doLeftClick() {
        startActivity(new Intent(this, NewTaskActivity.class));
    }

    private void getTaskFromDB(){
        taskLists.clear();
        LogUtil.i("test" , "更新本地数据："+taskLists.size());
        taskLists.addAll(db.getTaskInfo(showNum,PreferenceUtils.getUserId(getApplicationContext())));
        if(taskLists.size()>=8 && taskLists.size()<db.countData(db.TABLE_TASKINFO)){
            taskLists.add(new TaskInfoEntity("LOADINGMORE"));
        }
        taskAdapter.notifyDataSetChanged();
    }

    /**
     * 从服务器获取数据更新本地数据库
     * @param flag 区分下拉刷新 1 还是进入更新2
     */
    private void initData(final int flag){
        ArrayList<TaskInfoEntity> arrayList=db.getTaskInfo(1, PreferenceUtils.getUserId(getApplicationContext()));
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams();
        params.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        if (arrayList != null && arrayList.size()>0)
            params.put("TaskNum",arrayList.get(0).getTaskNum());
        else
            params.put("TaskNum",0);

        client.post(SystemConfig.URL_GETTASKINFO, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toTaskInfoLists(response)) {
                            getTaskFromDB();
                        }
                        LogUtil.i("success", getClass().getSimpleName() + "Tasktrue");
                    } else {
                        if (flag == 2) {
                            LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的任务发布!");
                            getTaskFromDB();
                        } else ToastMsg.showToast("已是最新");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "task连接失败！");
                if (flag == 2) ToastMsg.showToast("连接失败");
                else ToastMsg.showToast("刷新失败");
            }


            @Override
            public void onFinish() {
                if (flag == 2)
                    dialog.dismiss();
                else
                    taskLayout.setRefreshing(false);
            }
        });

    }

    /**
     * 去服务器删除该条任务
     * @param num TaskNum
     */
    private void delTask(final int num){

        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams("TaskNum",num);

        client.post(SystemConfig.URL_DELTASKINFO, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && db.delTaskInfo(num) > 0) {
                        LogUtil.i("test", getClass().getSimpleName() + "删除任务成功,id:" + num);
                        getTaskFromDB();
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端出现未知错误 无法删除该任务!");
                        ToastMsg.showToast("服务器端出现未知错误 无法删除该任务!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "task连接失败！");
                ToastMsg.showToast("连接失败");
            }


            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }

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
            public void onStart() {
                if (flag == 2)
                    dialog.show();
            }

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
                initTree(flag);
            }
        });
    }

    private void initTree(final int flag){
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        requestParams.put("Treeid", db.getLatestTreeId());
        client.post(SystemConfig.URL_GETTREE,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toTreeLists(response)) {
                            LogUtil.i("success", getClass().getSimpleName() + "章节信息有更新");
                        }
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的章节信息发布!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "Tree连接失败！");
                ToastMsg.showToast("连接失败,无法获取更多的章节信息");
            }

            @Override
            public void onFinish() {
                initAct(flag);
            }

        });
    }

    private void initAct(final int flag){
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        requestParams.put("ActNum", db.getLatestAct());
        client.post(SystemConfig.URL_GETACT,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toActLists(response)) {
                            LogUtil.i("success", getClass().getSimpleName() + "本地教学班信息有更新");
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
}
