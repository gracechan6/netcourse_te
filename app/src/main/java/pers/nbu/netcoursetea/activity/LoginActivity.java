package pers.nbu.netcoursetea.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcoursetea.BaseApplication;
import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class LoginActivity extends BaseActivity {

    private EditText name,pwd;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        setTitle("登录");
        initView();
    }

    @Override
    public void finish() {
        super.finish();
    }

    protected void initView(){
        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.pwd);
        submit = (Button) findViewById(R.id.submit);

        SharedPreferences sp=getSharedPreferences(PreferenceUtils.PREFERENCE, Context.MODE_PRIVATE);
        name.setText(sp.getString(PreferenceUtils.PREFERENCE_USERID, ""));
        pwd.setText(sp.getString(PreferenceUtils.PREFERENCE_PASSWORD, ""));
    }

    public void doSubmit(View view){
        final String names=name.getText().toString().trim();
        final String pwds=pwd.getText().toString().trim();

        if(names.length()==0){
            Toast.makeText(getApplication(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }
        if(pwds.length()==0){
            Toast.makeText(getApplication(),"密码不能为空",Toast.LENGTH_SHORT).show();
            pwd.requestFocus();
            return;
        }
        submit.setClickable(false);

//        if (!PreferenceUtils.getEqId(getApplicationContext()).equals("") &&
//                !PreferenceUtils.getEqId(getApplicationContext()).equals(names)) {
//            Toast.makeText(getApplicationContext(), "此设备已经登陆过相应学号，请使用第一次登陆的学号作为帐号登陆", Toast.LENGTH_SHORT).show();
//            return;
//        }

        AsyncHttpClient client=((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put(SystemConfig.LOGINNAME,names);
        requestParams.put(SystemConfig.LOGINPWD,pwds);
        client.post(SystemConfig.URL_LOGINVAILD, requestParams, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        LogUtil.i("test", "登陆成功");
                        PreferenceUtils.saveLoginInfo(getApplicationContext(), names, pwds);
                        PreferenceUtils.saveClassInfo(getApplicationContext(), response.getString("TeachName"),
                                response.getString("TeachPost"), response.getString("TeachMod"));
                        PreferenceUtils.setLOGINVAL(true);
                        setResult(Activity.RESULT_OK);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "用户不存在或者密码错误!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName()+"连接失败！");
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                submit.setClickable(true);
            }
        });

    }

}
