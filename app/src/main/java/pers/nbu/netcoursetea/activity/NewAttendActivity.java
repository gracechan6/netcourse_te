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
import pers.nbu.netcoursetea.entity.AttendInfoEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.entity.TreeEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class NewAttendActivity extends BaseActivity {

    private LinearLayout spCourse,spAct,spWeek;
    private Spinner course,act,week;
    private Button submit;
    private EditText content;
    private RadioGroup attOpen,attClass;

    private TextView actShow,courseShow;

    private ArrayList<CourseEntity> courseLists;
    private ArrayList<ActEntity> actLists;
    private ArrayAdapter<String> course_adapter,act_adapter,week_adapter;
    private List<String> course_list,act_list;
    private final String arr[] = new String[] { "第一周", "第二周", "第三周", "第四周", "第五周", "第六周","第七周","第八周","第九周","第十周",
            "第十一周", "第十二周", "第十三周", "第十四周", "第十五周", "第十六周","第十七周","第十八周","第十九周","第二十周"};

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private String courseNum;
    private Integer flag=1;

    private String attn,time="",actn,aclass,aweek,pn,remark,className="",courseName="";
    private int open=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_attend);

        initToolBar();
        setRightOfToolbar(true);
        initView();
    }

    private void initView(){
        content = (EditText) findViewById(R.id.content);
        spCourse = (LinearLayout) findViewById(R.id.spCourse);
        spAct = (LinearLayout) findViewById(R.id.spAct);
        spWeek = (LinearLayout) findViewById(R.id.spWeek);
        course = (Spinner) findViewById(R.id.course);
        act = (Spinner) findViewById(R.id.act);
        week = (Spinner) findViewById(R.id.week);

        actShow = (TextView) findViewById(R.id.actShow);
        courseShow = (TextView) findViewById(R.id.courseShow);

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
                    courseName = courseLists.get(position).getCourName();
                    dialog.show();
                    initAct();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (courseLists != null && courseLists.size() > 0) {
                    courseName = courseLists.get(0).getCourName();
                    dialog.show();
                    initAct();
                }
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
                    actn = actLists.get(position).getActNum();
                    className = actLists.get(position).getClassName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (actLists != null && actLists.size() > 0) {
                    actn = actLists.get(0).getActNum();
                    className = actLists.get(0).getClassName();
                }
            }
        });


        week_adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        week_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        week.setAdapter(week_adapter);
        week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                aweek = arr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                aweek = arr[0];
            }
        });

        attOpen = (RadioGroup) findViewById(R.id.attOpen);
        attOpen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rd = (RadioButton) NewAttendActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                if (rd == ((RadioButton) attOpen.getChildAt(1))) {
                    open = 0;
                } else if (rd == ((RadioButton) attOpen.getChildAt(2)))
                    open = 1;
                else
                    open = 2;
            }
        });
        ((RadioButton)attOpen.getChildAt(1)).setChecked(true);

        attClass = (RadioGroup) findViewById(R.id.attClass);
        attClass.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rd = (RadioButton) NewAttendActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
                aclass = rd.getText().toString();
                if (aclass.equals("机房考勤"))
                    pn = "默认机房";
                else
                    pn = "null";
            }
        });
        ((RadioButton)attClass.getChildAt(1)).setChecked(true);

        if (getIntent().getIntExtra("flag",0)!=0){
            attn = getIntent().getStringExtra(SystemConfig.ATTDENCENUM);
            time = getIntent().getStringExtra(SystemConfig.STATUSTIME);
            actn = getIntent().getStringExtra(SystemConfig.ACTNUM);
            open = getIntent().getIntExtra(SystemConfig.ATTOPEN, 0);
            aclass = getIntent().getStringExtra(SystemConfig.ATTDENCECLASS);
            aweek = getIntent().getStringExtra(SystemConfig.ATTDENCEWEEK);
            pn = getIntent().getStringExtra(SystemConfig.PLACENAME);
            remark = getIntent().getStringExtra(SystemConfig.REMARK);
            int i=0;
            for (;i<arr.length;i++)
                if (aweek.equals(arr[i]))
                    break;
            week.setSelection(i, true);
            ((RadioButton)attOpen.getChildAt(open+1)).setChecked(true);
            if (aclass.equals("机房考勤"))  ((RadioButton)attClass.getChildAt(1)).setChecked(true);
            else    ((RadioButton)attClass.getChildAt(2)).setChecked(true);
            if (!remark.equals("null"))
                content.setText(remark);
            //spWeek.setVisibility(View.GONE);
            spAct.setVisibility(View.GONE);
            spCourse.setVisibility(View.GONE);

            className = getIntent().getStringExtra(SystemConfig.CLASSNAME);
            courseName = getIntent().getStringExtra(SystemConfig.COURNAME);

            setTitle("考勤详情");
            submit.setText("更新");

            actShow.setText("教学班：" + className);
            courseShow.setText("课程：" + courseName);
            actShow.setVisibility(View.VISIBLE);
            courseShow.setVisibility(View.VISIBLE);
        }
        else {
            setTitle("新建考勤");
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
     * @param view 更新教学班信息
     */
    public void doRefreshAct(View view){
        warnAgain(db.TABLE_ACT, "教学班");
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
                setTitle("查看考勤信息");
                submit.setText("更新");

                actShow.setText("教学班："+className);
                courseShow.setText("课程："+ courseName);
                actShow.setVisibility(View.VISIBLE);
                courseShow.setVisibility(View.VISIBLE);

                spAct.setVisibility(View.GONE);
                spCourse.setVisibility(View.GONE);
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
     * @param view 按钮点击提交
     */
    public void doSubmit(View view){

        if(!content.getText().toString().trim().equals("")){
            remark = content.getText().toString().trim();
        }
        else
            remark="null";

        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();

        if (time.equals(""))
            time = DateTransform.getNowDate();

        requestParams.put("StatusTime",time);
        requestParams.put("AttOpen",open);
        requestParams.put("AttdenceClass",aclass);
        requestParams.put("AttdenceWeek",aweek);
        requestParams.put("PlaceName",pn);
        requestParams.put("Remark",remark);
        if (submit.getText().toString().equals("提交")){
            requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
            requestParams.put("ActNum",actn);
            client.post(SystemConfig.URL_ADDATTENDINFO,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
                @Override
                public void onStart() {
                    dialog.show();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success") && response.getInt("AttdenceNum") != 0) {
                            attn = response.getString("AttdenceNum");
                            db.insertAttendInfo(new AttendInfoEntity(attn,time, PreferenceUtils.getUserId(getApplicationContext()),
                                    actn,open,aclass,aweek,pn,remark));
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
            requestParams.put("AttdenceNum",attn);
            client.post(SystemConfig.URL_UPDATEATTENDINFO, requestParams, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            db.updateAttendInfo(new AttendInfoEntity(attn, time,PreferenceUtils.getUserId(getApplicationContext()),
                                    actn,open,aclass,aweek,pn,remark));
                            LogUtil.i("success", getClass().getSimpleName() + "Updatetrue");
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

    /**
     * 返回上层
     */
    private void close(){
        this.finish();
    }

    private void getCourseFromDB(){
        courseLists.clear();
        course_list.clear();
        courseLists.addAll(db.getCourse(9999, PreferenceUtils.getUserId(getApplicationContext())));
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
        ArrayList<ActEntity> acts=new ArrayList<>();
        acts=db.getAct(1, courseNum);
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        if (acts!= null && acts.size()>0)
            requestParams.put("ActNum",acts.get(0).getActNum());
        else
            requestParams.put("ActNum", 0);
        client.post(SystemConfig.URL_GETACT, requestParams, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
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
