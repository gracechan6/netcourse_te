package pers.nbu.netcoursetea.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcoursetea.BaseApplication;
import pers.nbu.netcoursetea.DateTransform;
import pers.nbu.netcoursetea.JsonTransform;
import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.ToastMsg;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.ActEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.entity.TaskInfoEntity;
import pers.nbu.netcoursetea.entity.TreeEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class NewTaskActivity extends BaseActivity {

    private LinearLayout spCourse,spTree,spAct;
    private RadioGroup yorNSub,yorNVis,isShowResult,isStuDown;
    private Spinner course,tree,act,endTime;
    private CheckBox annex,fileOn,video;
    private EditText title,content;
    private Button submit;

    private TextView courseShow,treeShow,actShow;
    private String courseName="",className="",treeName="";

    private String t,r,ys="True",yv="True",fi="False",vi="False",an="False",start="",end,isdo="False",isdr="False",ac,courseNum,url="null";
    private Integer taskNum,treeId;

    private ArrayList<CourseEntity> courseLists;
    private ArrayList<TreeEntity> treeLists;
    private ArrayList<ActEntity> actLists;
    private ArrayAdapter<String> course_adapter,tree_adapter,act_adapter,time_adapter;
    private List<String> course_list,tree_list,act_list,time_list;

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private int flag=0,afterDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        initToolBar();
        setRightOfToolbar(true);
        initView();
    }

    private void initView(){
        courseShow = (TextView) findViewById(R.id.courseShow);
        treeShow = (TextView) findViewById(R.id.treeShow);
        actShow = (TextView) findViewById(R.id.actShow);

        spCourse = (LinearLayout) findViewById(R.id.spCourse);
        spTree = (LinearLayout) findViewById(R.id.spTree);
        spAct = (LinearLayout) findViewById(R.id.spAct);
        course = (Spinner) findViewById(R.id.course);
        tree = (Spinner) findViewById(R.id.tree);
        act = (Spinner) findViewById(R.id.act);
        endTime = (Spinner) findViewById(R.id.endTime);
        annex = (CheckBox) findViewById(R.id.annex);
        annex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    an="True";
                else
                    an="False";
            }
        });
        fileOn = (CheckBox) findViewById(R.id.fileOn);
        fileOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    fi="True";
                else
                    fi="False";
            }
        });
        video = (CheckBox) findViewById(R.id.video);
        video.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    vi="True";
                else
                    vi="False";
            }
        });
        yorNSub = (RadioGroup) findViewById(R.id.yorNSub);
        yorNSub.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rd = (RadioButton) NewTaskActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                if (rd == ((RadioButton) yorNSub.getChildAt(1))) {
                    ys = "True";
                    fileOn.setClickable(true);
                    video.setClickable(true);
                    annex.setClickable(true);
                } else {
                    ys = "False";
                    fileOn.setChecked(false);
                    fileOn.setClickable(false);
                    video.setChecked(false);
                    video.setClickable(false);
                    annex.setChecked(false);
                    annex.setClickable(false);

                }
            }
        });
        ((RadioButton)yorNSub.getChildAt(1)).setChecked(true);
        yorNVis = (RadioGroup) findViewById(R.id.yorNVis);
        yorNVis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rd = (RadioButton) NewTaskActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                if (rd == ((RadioButton) yorNVis.getChildAt(1))) {
                    yv = "True";
                }
                else
                    yv = "False";
            }
        });
        ((RadioButton)yorNVis.getChildAt(1)).setChecked(true);
        isShowResult = (RadioGroup) findViewById(R.id.isShowResult);
        isShowResult.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rd = (RadioButton) NewTaskActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                if (rd == ((RadioButton) isShowResult.getChildAt(1))) {
                    isdr = "False";
                }
                else
                    isdr = "True";
            }
        });
        ((RadioButton)isShowResult.getChildAt(1)).setChecked(true);
        isStuDown = (RadioGroup) findViewById(R.id.isStuDown);
        isStuDown.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rd = (RadioButton) NewTaskActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                if (rd == ((RadioButton) isStuDown.getChildAt(1))) {
                    isdo = "False";
                }
                else
                    isdo = "True";
            }
        });
        ((RadioButton)isStuDown.getChildAt(1)).setChecked(true);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        submit = (Button) findViewById(R.id.submit);

        courseLists=new ArrayList<>();
        course_list = new ArrayList<String>();
        course_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, course_list);
        course_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course.setAdapter(course_adapter);
        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (courseLists != null && courseLists.size() > 0) {
                    courseNum = courseLists.get(position).getCourNum();
                    dialog.show();
                    initTree();
                    initAct();
                    LogUtil.i("test", getClass().getSimpleName() + "course：" + courseNum);
                    courseName = courseLists.get(position).getCourName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (courseLists != null && courseLists.size() > 0) {
                    dialog.show();
                    initTree();
                    initAct();
                    courseName = courseLists.get(0).getCourName();
                }
            }
        });


        treeLists=new ArrayList<>();
        tree_list = new ArrayList<String>();
        tree_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tree_list);
        tree_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tree.setAdapter(tree_adapter);
        tree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (treeLists != null && treeLists.size() > 0) {
                    treeId = treeLists.get(position).getTreeid();
                    LogUtil.i("test", getClass().getSimpleName() + "treeid：" + treeId);
                    treeName = treeLists.get(position).getTreeName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (treeLists != null && treeLists.size() > 0)
                    treeId = treeLists.get(0).getTreeid();
                    treeName = treeLists.get(0).getTreeName();
            }
        });

        actLists=new ArrayList<>();
        act_list = new ArrayList<String>();
        act_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, act_list);
        act_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        act.setAdapter(act_adapter);
        act.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (actLists != null && actLists.size() > 0) {
                    ac = actLists.get(position).getActNum();
                    className = actLists.get(position).getClassName();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (actLists != null && actLists.size() > 0)
                    ac = actLists.get(0).getActNum();
                    className = actLists.get(0).getClassName();
            }
        });

        time_list = new ArrayList<String>();
        for(int i=0;i<=14;i++){
            time_list.add(i+"天后");
        }
        time_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_list);
        time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endTime.setAdapter(time_adapter);
        endTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                afterDay = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                afterDay = 0;
            }
        });

        if (getIntent().getIntExtra("flag",0)!=0){
            setTitle("查看任务");
            taskNum = getIntent().getIntExtra(SystemConfig.TASKNUM, 0);
            t=getIntent().getStringExtra(SystemConfig.TASKTITLE);
            r=getIntent().getStringExtra(SystemConfig.TASKREQUIRE);
            ys=getIntent().getStringExtra(SystemConfig.YORNSUB);
            yv=getIntent().getStringExtra(SystemConfig.YORNVIS);
            fi=getIntent().getStringExtra(SystemConfig.FILEON);
            vi=getIntent().getStringExtra(SystemConfig.VIDEO);
            an=getIntent().getStringExtra(SystemConfig.ANNEX);
            start=getIntent().getStringExtra(SystemConfig.ENDTIME);
            treeId=getIntent().getIntExtra(SystemConfig.TREEID, 0);
            isdo=getIntent().getStringExtra(SystemConfig.ISSTUDOWN);
            isdr=getIntent().getStringExtra(SystemConfig.ISSHOWRESULT);
            ac=getIntent().getStringExtra(SystemConfig.ACTNUM);
            treeName=getIntent().getStringExtra(SystemConfig.TREENAME);
            courseName=getIntent().getStringExtra(SystemConfig.COURNAME);
            className=getIntent().getStringExtra(SystemConfig.CLASSNAME);

            title.setText(t);
            content.setText(r);
            if (ys.equals("True")) {
                ((RadioButton) yorNSub.getChildAt(1)).setChecked(true);
                if (an.equals("True"))
                    annex.setChecked(true);
                else
                    annex.setChecked(false);

                if (fi.equals("True"))
                    fileOn.setChecked(true);
                else
                    fileOn.setChecked(false);

                if (vi.equals("True"))
                    video.setChecked(true);
                else
                    video.setChecked(false);
            }
            else
                ((RadioButton)yorNSub.getChildAt(2)).setChecked(true);
            if (yv.equals("True"))
                ((RadioButton)yorNVis.getChildAt(1)).setChecked(true);
            else
                ((RadioButton)yorNVis.getChildAt(2)).setChecked(true);

            if (isdo.equals("True"))
                ((RadioButton)isStuDown.getChildAt(2)).setChecked(true);
            else
                ((RadioButton)isStuDown.getChildAt(1)).setChecked(true);

            if (isdr.equals("True"))
                ((RadioButton)isShowResult.getChildAt(2)).setChecked(true);
            else
                ((RadioButton)isShowResult.getChildAt(1)).setChecked(true);


            submit.setText("更新");
            treeShow.setText("章节："+treeName);treeShow.setVisibility(View.VISIBLE);
            courseShow.setText("课程："+courseName);courseShow.setVisibility(View.VISIBLE);
            actShow.setText("教学班："+className);actShow.setVisibility(View.VISIBLE);

            treeId=getIntent().getIntExtra(SystemConfig.TREEID,0);
            taskNum=getIntent().getIntExtra(SystemConfig.TASKNUM,0);
            spCourse.setVisibility(View.GONE);
            spTree.setVisibility(View.GONE);
            spAct.setVisibility(View.GONE);
        }else{
            setTitle("新建任务");
            initCourse();
        }
    }


    /**
     * 弹框再次提醒
     */
    protected void warnAgain(final String table,final String name){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("警告") ;
        builder.setMessage("此操作会删除本地所有已保存的" + name + "信息再去服务器端获取，您确认这样操作吗？") ;
        builder.setPositiveButton("取消", null);
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int re = db.delData(table);
                LogUtil.i("test", "删除了" + name + "数据总条数：" + re);
                switch (name){
                    case "课程":initCourse();break;
                    case "章节":initTree();break;
                    case "教学班":initAct();break;
                    default:break;
                }

            }
        });
        builder.show();
    }

    /**
     * @param view 更新课程
     */
    public void doRefreshCourse(View view){
        warnAgain(db.TABLE_COURSE, "课程");
    }

    /**
     * @param view 更新章节
     */
    public void doRefreshTree(View view){
        warnAgain(db.TABLE_TREE,"章节");
    }

    /**
     * @param view 更新课程
     */
    public void doRefreshAct(View view){
        warnAgain(db.TABLE_ACT,"教学班");
    }

    /**
     * 发布成功
     */
    private void doAddSuccess(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("成功") ;
        builder.setMessage("已经成功发表，请问是返回上层还是停留在此页面？") ;
        builder.setPositiveButton("停留", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setTitle("查看任务");
                submit.setText("更新");

                treeShow.setText("章节："+treeName);treeShow.setVisibility(View.VISIBLE);
                courseShow.setText("课程："+courseName);courseShow.setVisibility(View.VISIBLE);
                actShow.setText("教学班：" + className);actShow.setVisibility(View.VISIBLE);

                start = end;
                endTime.setSelection(0,true);
                spCourse.setVisibility(View.GONE);
                spTree.setVisibility(View.GONE);
                spAct.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("返回上层", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                close();
            }
        });
        builder.show();
    }

    /**
     * 返回上层
     */
    private void close(){
        this.finish();
    }

    /**
     * @param view 按钮点击提交
     */
    public void doSubmit(View view){
        t=title.getText().toString().trim();
        r=content.getText().toString().trim();
        if (t.equals("")){
            ToastMsg.showToast("任务标题不能为空");
            title.requestFocus();
            return;
        }
        if (r.equals("")){
            ToastMsg.showToast("任务详情不能为空");
            content.requestFocus();
            return;
        }
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();

        if (start.equals(""))
            start = DateTransform.getNowDate();
        end = DateTransform.getDateLater(DateTransform.stringToDate(start),afterDay);

        requestParams.put("TaskTitle",t);
        requestParams.put("TaskRequire",r);
        requestParams.put("YorNSub",ys);
        requestParams.put("YorNVis",yv);
        requestParams.put("FileOn",fi);
        requestParams.put("Video",vi);
        requestParams.put("Annex",an);
        requestParams.put("EndTime",end);
        requestParams.put("IsStuDown",isdo);
        requestParams.put("IsShowResult",isdr);
        if (submit.getText().toString().equals("提交")){
            requestParams.put("TeachNum", PreferenceUtils.getUserId(getApplicationContext()));
            requestParams.put("TaskTime",start);
            requestParams.put("Treeid",treeId);
            requestParams.put("ActNum",ac);
            client.post(SystemConfig.URL_ADDTASKINFO,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
                @Override
                public void onStart() {
                    dialog.show();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success") && response.getInt("TaskNum") != 0) {
                            taskNum = response.getInt("TaskNum");
                            db.insertTaskInfo(new TaskInfoEntity(taskNum, PreferenceUtils.getUserId(getApplicationContext()),
                                    t,r,ys,yv,url,fi,vi,an,start,end,treeId,isdo,isdr,ac));
                            LogUtil.i("success", getClass().getSimpleName() + "Addtrue");
                            doAddSuccess();
                        } else {
                            LogUtil.i("test", getClass().getSimpleName() + "发布失败");
                            ToastMsg.showToast("服务器方面问题导致发布失败！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    LogUtil.e("test", getClass().getSimpleName() + "连接失败！");
                    ToastMsg.showToast("连接失败");
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                }
            });
        }
        else {
            requestParams.put("TaskNum",taskNum);
            client.post(SystemConfig.URL_UPDATETASKINFO, requestParams, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            db.updateTaskInfo(new TaskInfoEntity(taskNum, PreferenceUtils.getUserId(getApplicationContext()),
                                    t, r, ys, yv, url, fi, vi, an, start, end, treeId, isdo, isdr, ac));
                            LogUtil.i("success", getClass().getSimpleName() + "Updatetrue");
                            start = end;
                            endTime.setSelection(0,true);
                            ToastMsg.showToast("更新成功");
                        } else {
                            LogUtil.i("test", getClass().getSimpleName() + "更新失败!");
                            ToastMsg.showToast("服务器方面问题导致更新失败！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    LogUtil.e("test", getClass().getSimpleName() + "连接失败！");
                    ToastMsg.showToast("连接失败");
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                }
            });
        }
    }

    private void getCourseFromDB(){
        courseLists.clear();
        course_list.clear();
        courseLists.addAll(db.getCourse(9999,PreferenceUtils.getUserId(getApplicationContext())));
        for(int i=0;i<courseLists.size();i++)
            course_list.add(courseLists.get(i).getCourName());
        course_adapter.notifyDataSetChanged();
    }

    private void initCourse(){
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
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toCourseLists(response)) {
                            getCourseFromDB();
                        }
                        LogUtil.i("success", getClass().getSimpleName() + "Coursetrue");
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的课程信息发布!");
                        getCourseFromDB();
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
                dialog.dismiss();
            }
        });
    }


    private void getTreeFromDB(){
        treeLists.clear();
        tree_list.clear();
        treeLists.addAll(db.getTree(9999, courseNum));
        for(int i=0;i<treeLists.size();i++)
            tree_list.add(treeLists.get(i).getTreeName());
        tree_adapter.notifyDataSetChanged();
        if (flag>1)
            dialog.dismiss();
    }

    private void initTree(){
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        requestParams.put("Treeid", db.getLatestTreeId());
        client.post(SystemConfig.URL_GETTREE,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                flag++;
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toTreeLists(response)) {
                            getTreeFromDB();
                        }
                        LogUtil.i("success", getClass().getSimpleName() + "Treetrue");
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的章节信息发布!");
                        getTreeFromDB();
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

        });
    }

    private void getActFromDB(){
        actLists.clear();
        act_list.clear();
        actLists.addAll(db.getAct(9999, courseNum));
        for(int i=0;i<actLists.size();i++)
            act_list.add(actLists.get(i).getClassName());
        act_adapter.notifyDataSetChanged();
        if (flag>1)
            dialog.dismiss();
    }

    private void initAct(){
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        requestParams.put("ActNum", db.getLatestAct());
        client.post(SystemConfig.URL_GETACT,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                flag++;
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toActLists(response)) {
                            getActFromDB();
                        }
                        LogUtil.i("success", getClass().getSimpleName() + "Acttrue");
                    } else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的活动班信息发布!");
                        getActFromDB();
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

        });
    }

}
