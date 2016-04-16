package pers.nbu.netcoursetea.activity;

import android.os.Bundle;

import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.ToastMsg;

public class AnnManageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann_manage);

        initToolBar();
        setLeft("新公告");
        setRightOfToolbar(true);
        setTitle("公告管理");


    }

    /**
     * 新建公告
     */
    @Override
    public void doLeftClick() {
        super.doLeftClick();
        ToastMsg.showToast("新建公告");
    }
}
