package pers.nbu.netcoursetea.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.config.SystemConfig;

public class TaskShowActivity extends BaseActivity {

    private TextView title,time,require,endTime,tcName,turnOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_show);
        initToolBar();
        setTitle("任务");
        setRightOfToolbar(true);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        require = (TextView) findViewById(R.id.require);
        endTime = (TextView) findViewById(R.id.endTime);
        tcName = (TextView) findViewById(R.id.tcName);
        turnOver = (TextView) findViewById(R.id.turnOver);

        title.setText(getIntent().getStringExtra(SystemConfig.TASKTITLE));
        time.append(getIntent().getStringExtra(SystemConfig.TASKTIME));

        tcName.setText("发布人：" + getIntent().getStringExtra(SystemConfig.TEACHNAME) + " 课程：" + getIntent().getStringExtra(SystemConfig.COURNAME));
        endTime.append(getIntent().getStringExtra(SystemConfig.ENDTIME));
        if(getIntent().getStringExtra("flag").equals("1")) {
            require.append(getIntent().getStringExtra(SystemConfig.TASKREQUIRE));
        }else{
            require.setVisibility(View.GONE);
            turnOver.setVisibility(View.VISIBLE);
            if(getIntent().getStringExtra(SystemConfig.OPUSNUM).equals("0"))
                turnOver.append(" 未交");
            else
                turnOver.append(" 已交");
        }

    }
}
