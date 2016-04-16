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
import pers.nbu.netcoursetea.adapter.TaskManageAdapter;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.TaskManageEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class TaskManageActivity extends BaseActivity {

    private SwipeRefreshLayout taskLayout;
    private SwipeMenuListView taskLsv;
    private TaskManageAdapter taskAdapter;
    private ArrayList<TaskManageEntity> taskLists = new ArrayList<>();

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private int showNum=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manage);
        initToolBar();
        setTitle("任务管理");
        setRightOfToolbar(true);
        setLeftOfToolbar(true);
        initView();
        initData();
    }

    private void initView(){
        taskLayout = (SwipeRefreshLayout) findViewById(R.id.taskLayout);
        taskLayout.setOnRefreshListener(taskShowRefresh);
        taskLsv = (SwipeMenuListView) findViewById(R.id.taskLsv);
        taskAdapter = new TaskManageAdapter(taskLists,getApplicationContext());
        taskLsv.setAdapter(taskAdapter);
        taskLsv.setOnItemClickListener(taskClickListener);
    }

    /**
     * 左上角图标刷新操作
     * 更新本地未读的作业，去服务器端查看当前的作业是否已经上交 如果已经上交 则本地更新为 已交状态
     */
    @Override
    public void doRefreshClick() {
        ArrayList<TaskManageEntity> lists=db.getTaskManageShowOver(false);
        if (lists!=null && lists.size()>0){
            for(int i=0;i<lists.size();i++) {
                AsyncHttpClient client = ((BaseApplication) getApplication()).getSharedHttpClient();
                final RequestParams params = new RequestParams();
                params.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
                params.put("TaskNum", lists.get(i).getTaskNum());
                client.post(SystemConfig.URL_UPDATETASKM, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getBoolean("success") && response.getInt("OptNum") != 0) {
                                int var = db.updateTaskManageShowOver(response.getInt("TaskNum"), response.getInt("OptNum"));
                                if (var > 0)
                                    getTaskFromDB(showNum);
                                LogUtil.i("test", getClass().getSimpleName() + "成功更新本地列表，更新数量：" + var);
                            } else {
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

    /**
     * 初始化数据
     */
    private void initData(){

        ArrayList<TaskManageEntity> arrayList = db.getTaskManageShow(1);
        if (arrayList != null && arrayList.size()>0) {
            LogUtil.d("test", String.valueOf(arrayList.get(0).getTaskNum()));
            getTask(1, PreferenceUtils.getUserId(getApplicationContext()),showNum,arrayList.get(0).getTaskNum());
        }
        else
            getTask(1, PreferenceUtils.getUserId(getApplicationContext()),showNum,0);
    }

    /**
     * 任务下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener taskShowRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //获取当前任务的最后一条消息的AnnNum，与服务器端比较看最新消息是否为这个id
            //是，则不操作,否，获取此id后的所有消息，然后保存到本地并更新进度
            ArrayList<TaskManageEntity> arrayList = db.getTaskManageShow(1);
            if (arrayList != null && arrayList.size()>0) {
                LogUtil.d("test", String.valueOf(arrayList.get(0).getTaskNum()));
                getTask(2, PreferenceUtils.getUserId(getApplicationContext()),showNum,arrayList.get(0).getTaskNum());
            }
        }
    };

    protected AdapterView.OnItemClickListener taskClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position+1==taskLists.size()){
                showNum +=7;
                getTaskFromDB(showNum);
            }
            else{
                Intent intent = new Intent(getApplicationContext(),TaskShowActivity.class);
                intent.putExtra(SystemConfig.TASKTITLE, taskLists.get(position).getTaskTitle());
                intent.putExtra(SystemConfig.OPUSNUM,""+taskLists.get(position).getOpusNum());
                intent.putExtra(SystemConfig.TASKTIME,taskLists.get(position).getTaskTime());
                intent.putExtra(SystemConfig.ENDTIME,taskLists.get(position).getEndTime());
                intent.putExtra(SystemConfig.TEACHNAME,taskLists.get(position).getTeachName());
                intent.putExtra(SystemConfig.COURNAME,taskLists.get(position).getCourName());
                intent.putExtra(SystemConfig.TASKNUM,taskLists.get(position).getTaskNum());
                intent.putExtra("flag","2");//代表此activity传入
                startActivity(intent);
            }
        }
    };

    /**
     * 更新本地任务显示数据
     * @param show   从本地数据库获取条数
     */
    protected void getTaskFromDB(int show){
        taskLists.clear();
        taskLists.addAll(db.getTaskManageShow(show));
        if (taskLists.size()>= 8 && taskLists.size()<db.countData(db.TABLE_TASKMANAGESHOW)) {
            taskLists.add(new TaskManageEntity("LOADINGMORE"));
        }
        taskAdapter.notifyDataSetChanged();
    }

    /**
     * 从服务器端获取数据
     * @param flag   区分下拉刷新2还是初始化获取1
     * @param userNum    用户名
     * @param showTask   显示数量
     * @param taskNum   任务号
     */
    protected void getTask(final int flag,String userNum, final int showTask,int taskNum){
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams();
        params.put("UserNum", userNum);
        params.put("TaskNum",taskNum);
        client.post(SystemConfig.URL_GETTASKM,params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                super.onStart();
                if (flag==1){dialog.show();}
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.turnToTaskMLists(response)) {
                            getTaskFromDB(showTask);
                        }
                        LogUtil.i("test", getClass().getSimpleName()+"true,获取数据成功");
                    } else {
                        if (flag==1) {
                            //Toast.makeText(getApplicationContext(), "服务器端未有任务记录!", Toast.LENGTH_SHORT).show();
                            LogUtil.i("test",getClass().getSimpleName()+"服务器端未有新的任务记录!");
                            getTaskFromDB(showTask);
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
                else taskLayout.setRefreshing(false);
            }
        });
    }

}
