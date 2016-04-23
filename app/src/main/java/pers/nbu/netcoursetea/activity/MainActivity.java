package pers.nbu.netcoursetea.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcoursetea.BaseApplication;
import pers.nbu.netcoursetea.JsonTransform;
import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.ToastMsg;
import pers.nbu.netcoursetea.adapter.AnnAdapter;
import pers.nbu.netcoursetea.adapter.TaskAdapter;
import pers.nbu.netcoursetea.adapter.TaskInfoAdapter;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.AnnEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.entity.TaskEntity;
import pers.nbu.netcoursetea.entity.TaskInfoEntity;
import pers.nbu.netcoursetea.fragment.BottomFragment;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PhoneScreenUtils;
import pers.nbu.netcoursetea.util.PreferenceUtils;
import pers.nbu.netcoursetea.view.ActionSheet;
import pers.nbu.netcoursetea.view.CircleImageView;
import pers.nbu.netcoursetea.view.ListViewForScrollView;


public class MainActivity extends BaseActivity implements ActionSheet.MenuItemClickListener{

    /*公告任务栏目需要用到的控件及数据定义*/
    private SwipeRefreshLayout srlayout;
    private SwipeMenuListView annLsv;
    private AnnAdapter annAdapter/*,annShowAdapter*/;
    private List<AnnEntity> annLists;

    private SwipeRefreshLayout taskLayout;
    private SwipeMenuListView taskLsv;
    private TaskInfoAdapter taskAdapter;
    private ArrayList<TaskInfoEntity> taskLists ;
    private SwipeMenuCreator creator;

    /*首页*/
//    private ScrollView index;
//    private ListViewForScrollView annShowLsv,taskShowLsv;

    /*更多*/
    private LinearLayout more,me;

    /*我*/
    private CircleImageView ivHead;//头像显示
    private TextView className,regDate,name;
    public static int CROP_REQUEST_CODE = 10001;
    public static int Setting = 10002;
    public static final String PHOTO_TYPE = "PHOTO_TYPE";
    //private String headPath;

    private TextView blankView;

    //底部导航栏
    private BottomFragment bottomFragment;
    private RadioGroup radioGroup;

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private static int showNum=8,taskShowNum=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bottomFragment = (BottomFragment) getFragmentManager().
                findFragmentById(R.id.bottomFragment);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

        initToolBar();

        showView(2);setTitle("通知公告");initAnn(2, showNum);
    }

    /**
     * 初始化控件
     */
    protected void initView(){

        blankView = (TextView) findViewById(R.id.blankView);

        annLists = new ArrayList<>();
        taskLists = new ArrayList<>();

        //index
//        index = (ScrollView) findViewById(R.id.index);
//        annShowLsv = (ListViewForScrollView) findViewById(R.id.annShowLsv);
//        taskShowLsv = (ListViewForScrollView) findViewById(R.id.taskShowLsv);
//        annShowAdapter = new AnnAdapter(annLists,getApplicationContext());
//        taskShowAdapter = new TaskAdapter(taskLists,getApplicationContext());
//        annShowLsv.setAdapter(annShowAdapter);
//        taskShowLsv.setAdapter(taskShowAdapter);
//        annShowLsv.setOnItemClickListener(annClickListener);
//        taskShowLsv.setOnItemClickListener(annClickListener);

        //AnnShow
        srlayout = (SwipeRefreshLayout) findViewById(R.id.srlayout);
        srlayout.setOnRefreshListener(refreshListener);
        annLsv = (SwipeMenuListView) findViewById(R.id.announInfoLsv);
        annAdapter = new AnnAdapter(annLists,getApplicationContext());
        annLsv.setAdapter(annAdapter);
        annLsv.setOnItemClickListener(annClickListener);

        //TaskShow
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

        //more
        more = (LinearLayout) findViewById(R.id.more);
        //me
        me = (LinearLayout) findViewById(R.id.me);
        ivHead= (CircleImageView) findViewById(R.id.civ_head);
        //headPath=SystemConfig.PATH_HEAD+ PreferenceUtils.getUserId(getApplication())+SystemConfig.HEAD_TYPE;
        className = (TextView) findViewById(R.id.className);
        regDate = (TextView) findViewById(R.id.regDate);
        name = (TextView) findViewById(R.id.name);
    }

    //点击
    protected AdapterView.OnItemClickListener annClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            if (position+1==annLists.size() &&annLists.get(position).getAnnTitle().equals("LOADINGMORE")){
                showNum +=7;
                getAnnFromDB(2,showNum);
            }else {
                Intent intent = new Intent(getApplicationContext(), AnnActivity.class);
                intent.putExtra(SystemConfig.ANNTITLE, annLists.get(position).getAnnTitle());
                intent.putExtra(SystemConfig.ANNCON, annLists.get(position).getAnnCon());
                intent.putExtra(SystemConfig.ANNTIME, annLists.get(position).getAnnTime());
                intent.putExtra(SystemConfig.ANNURL, annLists.get(position).getAnnUrl());
                intent.putExtra(SystemConfig.TEACHNAME, annLists.get(position).getTeachName());
                intent.putExtra(SystemConfig.COURNAME, annLists.get(position).getCourName());
                intent.putExtra(SystemConfig.ANNNUM, annLists.get(position).getAnnNum());
                startActivity(intent);
            }
        }
    };

    /**
     * 底部标签栏操作
     */
    RadioGroup.OnCheckedChangeListener checkedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int rbId = group.getCheckedRadioButtonId();
            switch (rbId) {
//                case R.id.index:
//                    showView(1);setTitle("高校云课堂");init(1,3);
//                    break;
                case R.id.message:
                    showView(2);setTitle("通知公告");initAnn(2,showNum);
                    break;
                case R.id.task:
                    setTitle("任务");
                    if (PreferenceUtils.getLOGINVAL()) {showView(3);initCourse(2);}
                    else{showView(9999); showLogin(3);}
                    break;
                case R.id.more:
                    setTitle("更多");
                    if (PreferenceUtils.getLOGINVAL()) {showView(4);}
                    else{showView(9999); showLogin(4);}
                    break;
                case R.id.me:
                    setTitle("我");
                    if (PreferenceUtils.getLOGINVAL()) {showView(5);initMe();}
                    else{showView(9999); showLogin(5);}
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 底部控件选择控制显隐
     * @param select 底部选择了第几个
     */
    protected void showView(int select){
        switch (select){
//            case 1: index.setVisibility(View.VISIBLE);
//                    srlayout.setVisibility(View.GONE);
//                    taskLayout.setVisibility(View.GONE);
//                    more.setVisibility(View.GONE);
//                    me.setVisibility(View.GONE);
//                    break;
            case 2: //index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.VISIBLE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    me.setVisibility(View.GONE);
                    blankView.setVisibility(View.GONE);
                    break;
            case 3: //index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.VISIBLE);
                    more.setVisibility(View.GONE);
                    me.setVisibility(View.GONE);
                    blankView.setVisibility(View.GONE);
                    break;
            case 4: //index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                    me.setVisibility(View.GONE);
                    blankView.setVisibility(View.GONE);
                    break;
            case 5: //index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    me.setVisibility(View.VISIBLE);
                    blankView.setVisibility(View.GONE);
                    break;
            default:srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    me.setVisibility(View.GONE);
                    blankView.setVisibility(View.VISIBLE);;
        }
    }

    /**
     * @param flag 代表首页1
     * @param num 显示条数
     */
//首页========start
    protected void init(int flag,int num){
        //initAnn(flag, num);
        //initTask(flag, num);
    }
//首页========end


//公告========start
    /**
     * 公告下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //获取当前公告的最后一条消息的AnnNum，与服务器端比较看最新消息是否为这个id
            //是，则不操作,否，获取此id后的所有消息，然后保存到本地并更新进度
            ArrayList<AnnEntity> arrayList=db.getAnnInfo(1);
            if (arrayList!=null  && arrayList.size()>0) {
                LogUtil.d("test", "公告下拉刷新，获取到的最后一条公告id"+String.valueOf(arrayList.get(0).getAnnNum()));
                getAnn(2, arrayList.get(0).getAnnNum(), showNum, 2);
            }
        }
    };

    /**
     * 更新本地公告显示数据
     * @param flag   区分 是首页 1 显示还是公告栏 2显示
     * @param show 显示数量
     */
    protected void getAnnFromDB(int flag,int show){
        annLists.clear();
        annLists.addAll(db.getAnnInfo(show));
        if (annLists.size()>= 8 && annLists.size()<db.countData(db.TABLE_ANNSHOW)) {
            annLists.add(new AnnEntity("LOADINGMORE","..."));
        }
        annAdapter.notifyDataSetChanged();
    }
    /**
     * 初始化公告信息
     * @param flag 区分首页1显示还是公告栏显示
     * @param num 显示条数
     */
    protected void initAnn(int flag,int num){
        ArrayList<AnnEntity> arrayList = db.getAnnInfo(1);
        if (arrayList != null && arrayList.size()>0) {
            LogUtil.d("test", "公告，获取到的最后一条公告id"+String.valueOf(arrayList.get(0).getAnnNum()));
            getAnn(1, arrayList.get(0).getAnnNum(), num, flag);
        }
        else
            getAnn(1, 0, num, flag);
    }

    /**
     * 从服务器端获取公告信息并更新公告
     * @param flag   区分下拉刷新还是进去刷新1
     * @param annNum 获取服务器 annNum 大于 提供的值
     * @param showAnn 从数据库获取显示的数量
     * @param flags 区分 是首页1显示还是公告栏2显示
     */
    protected void getAnn(final int flag,int annNum, final int showAnn, final int flags){
        AsyncHttpClient client =((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams("annNum",annNum);
        client.post(SystemConfig.URL_UPDATEANN,params,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onStart() {
                if (flag==1) dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.turnToAnnLists(response)) {
                            getAnnFromDB(flags, showAnn);
                        }
                        LogUtil.i("success", getClass().getSimpleName()+"Anntrue");
                    } else {
                        if (flag==1) {
                            LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的公告发布!");//Toast.makeText(getApplicationContext(), "服务器端未有任务发布!", Toast.LENGTH_SHORT).show();
                            getAnnFromDB(flags, showAnn);
                        }
                        else Toast.makeText(getApplicationContext(), "已是最新", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "ann连接失败！");
                if (flag==1)
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                if (flag==1) dialog.dismiss();
                else srlayout.setRefreshing(false);
            }
        });
    }

//公告========end

//任务========start

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


    private void getTaskFromDB(){
        taskLists.clear();
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
    private void initTaskData(final int flag){
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
        client.post(SystemConfig.URL_GETTREE, requestParams, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
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
                initTaskData(flag);
            }
        });
    }


//任务========end

    /**
     * 显示登录界面
     * @param flag 区分点击更多4还是点击我5 点击任务3跳转的
     */
    protected void showLogin(final int flag){
        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("未登录") ;
        builder.setMessage("是否登录？") ;
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LogUtil.d("test", "不登陆" + String.valueOf(i));

            }
        });
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LogUtil.d("test", "登陆" + String.valueOf(i));
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), flag);
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==4){
            if(resultCode == Activity.RESULT_OK) {
                LogUtil.i("test", "更多返回");
                showView(4);
            }
        }else if (requestCode==5){
            if(resultCode == Activity.RESULT_OK) {
                LogUtil.i("test", "我  返回");
                showView(5);
                initMe();
            }
        }else if (requestCode==3){
            if(resultCode == Activity.RESULT_OK) {
                LogUtil.i("test", "任务  返回");
                showView(3);
                initCourse(2);
            }
        }

        if ((requestCode==CROP_REQUEST_CODE ) && resultCode== Activity.RESULT_OK){
            /*
            * 头像成功了
            * */
            //Bitmap bitmap=data.getExtras().getParcelable(CropActivity.BITMAP_DATA);
            setHead();
        }

    }

//更多========start

    /**
     * 公告管理
     * @param view
     */
    public void doNoticeManage(View view){
        startActivity(new Intent(getApplicationContext(),AnnManageActivity.class));
    }

    /**
     * 任务管理
     * @param view
     */
    public void doTaskManage(View view){
        startActivity(new Intent(getApplicationContext(),TaskManaActivity.class));
    }

    /**
     * 出勤管理
     * @param view
     */
    public void doAttendManage(View view){
        startActivity(new Intent(getApplicationContext(), AttendManaActivity.class));
    }

//更多========end

    /**
     * @param view 更改头像  actionSheet显示
     */
//我========start
    public void showActionSheet(View view){
        LogUtil.i("test","showAtionSheet");
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(this);
        menuView.setCancelButtonTitle(getResources().getString(R.string.text_cancel));// before add items
        menuView.addItems(getResources().getString(R.string.text_take_photo),
                getResources().getString(R.string.text_from_gallery));
        menuView.setItemClickListener(this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }

    /**
     * 弹框再次提醒
     * @param name 表名
     */
    protected void warnAgain(final String name){

        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("删除") ;
        builder.setMessage("您确认删除吗？") ;
        builder.setPositiveButton("取消", null);
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int re = db.delData(name);
                LogUtil.i("test", name + "删除了数据总条数：" + re);
                ToastMsg.showToast("本地共有" + re + "条数据，已清除完毕！");
            }
        });
        builder.show();
    }

    public void doClearAnnData(View view){
        LogUtil.i("test", "doClearAnnData");
        warnAgain(db.TABLE_ANNSHOW);
    }


    public void doClearAnnMData(View view){
        LogUtil.i("test", "doClearAnnMData");
        warnAgain(db.TABLE_ANN);
    }

    public void doClearTaskData(View view){
        LogUtil.i("test", "doClearTaskData");
        warnAgain(db.TABLE_TASKINFO);
    }

    public void doClearAttendData(View view){
        LogUtil.i("test","doClearAttendData");
        warnAgain(db.TABLE_ATTENDADMIN);
    }

    /**
     * @param itemPosition 三个选项选择其一操作
     */
    @Override
    public void onItemClick(int itemPosition) {
        switch (itemPosition){
            case 0://调用相机拍照
            case 1://从相册里面取照片
                changePhoto(itemPosition);
                break;
            default:break;
        }
    }

    /**
     * 修改头像
     * @param index 0 - 拍照， 1 - 相册
     */
    public void changePhoto(int index) {
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra(PHOTO_TYPE, index);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 头像显示
     */
    public  void setHead(){
        String headPath=SystemConfig.PATH_HEAD+ PreferenceUtils.getUserId(getApplication())+SystemConfig.HEAD_TYPE;
        File file=new File(headPath);
        if(file.exists()){
            Bitmap bm= BitmapFactory.decodeFile(headPath);
            ivHead.setImageDrawable(new BitmapDrawable(bm));
        }else{
            LogUtil.i("test", getClass().getSimpleName() + "  头像不存在 路径：" + headPath);
        }
    }

    protected void initMe(){
        setHead();
        className.setText("职位：" + PreferenceUtils.getPost(getApplicationContext()));
        regDate.setText("专业方向：" + PreferenceUtils.getMod(getApplicationContext()));
        name.setText(PreferenceUtils.getUserName(getApplicationContext()));
    }

//我========end


    @Override
    protected void onRestart() {
        super.onRestart();
        if (taskLayout.getVisibility() == View.VISIBLE)
        {
            getTaskFromDB();
        }
    }
}
