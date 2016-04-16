package pers.nbu.netcoursetea.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.config.SystemConfig;


public class AnnActivity extends BaseActivity {

    private TextView title,time,content,attach,tcName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann);
        initToolBar();
        setTitle("通知公告");
        setRightOfToolbar(true);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);
        attach = (TextView) findViewById(R.id.attach);
        tcName = (TextView) findViewById(R.id.tcName);

        title.setText(getIntent().getStringExtra(SystemConfig.ANNTITLE));
        time.append(getIntent().getStringExtra(SystemConfig.ANNTIME));
        content.setText(Html.fromHtml(getIntent().getStringExtra(SystemConfig.ANNCON)));
        tcName.setText("发布人：" + getIntent().getStringExtra(SystemConfig.TEACHNAME) + " 课程：" + getIntent().getStringExtra(SystemConfig.COURNAME));
        if (getIntent().getStringExtra(SystemConfig.ANNURL)==null){
            attach.setVisibility(View.GONE);
        }

    }

}
