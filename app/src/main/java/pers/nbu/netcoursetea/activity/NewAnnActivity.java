package pers.nbu.netcoursetea.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import pers.nbu.netcoursetea.entity.AnnInfoEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class NewAnnActivity extends BaseActivity {

    private Spinner course;
    private EditText title,content;
    private Button submit;

    private LinearLayout sp;
    private TextView courseShow;

    private String t,c;
    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private ArrayList<CourseEntity> lists;
    private ArrayAdapter<String> arr_adapter;
    private List<String> data_list;

    private int pos=0;
    private String time,courseName="";
    private int AnnNum,treeid;
    private String url,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ann);

        initToolBar();
        setRightOfToolbar(true);
        initView();
    }

    private void initView(){
        courseShow = (TextView) findViewById(R.id.courseShow);
        sp = (LinearLayout) findViewById(R.id.sp);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
//        content.setMovementMethod(ScrollingMovementMethod.getInstance());
//        content.setSelection(content.getText().length(), content.getText().length());
        submit = (Button) findViewById(R.id.submit);
        course = (Spinner) findViewById(R.id.course);
        lists=new ArrayList<>();
        data_list = new ArrayList<String>();
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        course.setAdapter(arr_adapter);
        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                pos = position;
                LogUtil.i("test", getClass().getSimpleName() + "点击了pos：" + pos);
                courseName = lists.get(position).getCourName();
                //ToastMsg.showToast((String.valueOf(pos)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pos = 0;
            }
        });

        if (getIntent().getIntExtra("flag",0)!=0){
            setTitle("查看公告");
            t=getIntent().getStringExtra(SystemConfig.ANNTITLE);
            c=getIntent().getStringExtra(SystemConfig.ANNCON);
            AnnNum=getIntent().getIntExtra("AnnNum", 0);
            title.setText(t);
            content.setText(c);
            submit.setText("更新");
            url=getIntent().getStringExtra(SystemConfig.ANNURL);
            type=getIntent().getStringExtra(SystemConfig.ANNURL);
            courseName = getIntent().getStringExtra(SystemConfig.COURNAME);
            courseShow.setText("课程："+courseName);
            courseShow.setVisibility(View.VISIBLE);
            treeid=getIntent().getIntExtra(SystemConfig.TREEID,0);
            sp.setVisibility(View.GONE);
        }else{
            setTitle("新建公告");
            initCourse();
        }
    }

    /**
     * 弹框再次提醒
     */
    protected void warnAgain(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("警告") ;
        builder.setMessage("此操作会删除本地所有已保存的课程信息再去服务器端获取，您确认这样操作吗？") ;
        builder.setPositiveButton("取消", null);
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int re = db.delData(db.TABLE_COURSE);
                LogUtil.i("test", "删除了课程数据总条数：" + re);
                initCourse();
            }
        });
        builder.show();
    }
    /**
     * @param view 更新课程
     */
    public void doRefreshCourse(View view){
        warnAgain();
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
                setTitle("查看公告");
                submit.setText("更新");

                courseName = getIntent().getStringExtra(SystemConfig.COURNAME);
                courseShow.setText("课程：" + courseName);
                courseShow.setVisibility(View.VISIBLE);

                sp.setVisibility(View.GONE);
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
        c=content.getText().toString().trim();
        if (t.equals("")){
            ToastMsg.showToast("标题不能为空");
            title.requestFocus();
            return;
        }
        if (c.equals("")){
            ToastMsg.showToast("内容不能为空");
            content.requestFocus();
            return;
        }
        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams=new RequestParams();

        time = DateTransform.getNowDate();
        requestParams.put("AnnTime",time);
        requestParams.put("AnnTitle",t);
        requestParams.put("AnnCon",c);
        if (submit.getText().toString().equals("提交")){
            requestParams.put("TeachNum", PreferenceUtils.getUserId(getApplicationContext()));
            requestParams.put("AnnType","公告");
            requestParams.put("Treeid",lists.get(pos).getTreeid());
            client.post(SystemConfig.URL_ADDANN,requestParams,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
                @Override
                public void onStart() {
                    dialog.show();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success") && response.getInt("AnnNum") != 0) {
                            AnnNum = response.getInt("AnnNum");
                            type="公告";
                            treeid = lists.get(pos).getTreeid();
                            url = "null";
                            db.insertAnn(new AnnInfoEntity(AnnNum,t, c, time, url, PreferenceUtils.getUserId(getApplicationContext()), type, treeid));
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
            requestParams.put("AnnNum",AnnNum);
            client.post(SystemConfig.URL_ALTERANN, requestParams, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            db.updateAnn(new AnnInfoEntity(AnnNum,t, c, time,url,PreferenceUtils.getUserId(getApplicationContext()),type,treeid));
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

    private void getCourseFromDB(){
        lists.clear();
        data_list.clear();
        lists.addAll(db.getCourse(9999,PreferenceUtils.getUserId(getApplicationContext())));
        for(int i=0;i<lists.size();i++)
            data_list.add(lists.get(i).getCourName());
        arr_adapter.notifyDataSetChanged();
    }

    private void initCourse(){
        ArrayList<CourseEntity> cous=new ArrayList<>();
        cous=db.getCourse(1,PreferenceUtils.getUserId(getApplicationContext()));
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

}
