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
import pers.nbu.netcoursetea.adapter.AnnInfoAdapter;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.db.DB;
import pers.nbu.netcoursetea.entity.AnnInfoEntity;
import pers.nbu.netcoursetea.entity.CourseEntity;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PhoneScreenUtils;
import pers.nbu.netcoursetea.util.PreferenceUtils;

public class AnnManageActivity extends BaseActivity {

    private SwipeRefreshLayout annLayout;
    private SwipeMenuListView annLsv;
    private AnnInfoAdapter annInfoAdapter;
    private ArrayList<AnnInfoEntity> annLists;
    private SwipeMenuCreator creator;

    private int showNum = 8;
    private DB db=DB.getInstance();
    private JsonTransform jsonTransform = JsonTransform.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann_manage);

        initToolBar();
        setLeft("新公告");
        setRightOfToolbar(true);
        setTitle("公告管理");

        initView();
        //initData(2);
        initCourse(2);

    }

    /**
     * 初始化View
     */
    private void initView(){
        annLayout = (SwipeRefreshLayout) findViewById(R.id.annLayout);
        annLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initCourse(1);
            }
        });
        annLsv = (SwipeMenuListView) findViewById(R.id.annLsv);
//        annLsv.setScrollbarFadingEnabled(true);//滚动条隐藏
        annLists = new ArrayList<>();

        annInfoAdapter = new AnnInfoAdapter(annLists,getApplicationContext());
        initCreator();
        if (creator != null)
            annLsv.setMenuCreator(creator);
        annLsv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        //去服务器端删除该公告，返回true，本地数据库也删除该公告
                        LogUtil.i("test", getClass().getSimpleName() + "删除当前公告,id:" + annLists.get(position).getAnnNum());
                        delAnn(annLists.get(position).getAnnNum());
                        break;
                    case 1:
                        // 未设置
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        annLsv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        annLsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //点击公告进入公告详情页查看
                if (position + 1 == annLists.size() && annLists.get(position).getAnnTitle().equals("LOADINGMORE")) {
                    showNum += 7;
                    getAnnFromDB();
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewAnnActivity.class);
                    intent.putExtra(SystemConfig.ANNTITLE, annLists.get(position).getAnnTitle());
                    intent.putExtra(SystemConfig.ANNCON, annLists.get(position).getAnnCon());
                    intent.putExtra(SystemConfig.ANNTIME, annLists.get(position).getAnnTime());
                    intent.putExtra(SystemConfig.ANNURL, annLists.get(position).getAnnUrl());
                    intent.putExtra(SystemConfig.TEACHNUM, annLists.get(position).getTeachNum());
                    intent.putExtra(SystemConfig.ANNTYPE, annLists.get(position).getAnnType());
                    intent.putExtra(SystemConfig.ANNNUM, annLists.get(position).getAnnNum());
                    intent.putExtra(SystemConfig.TREEID, annLists.get(position).getTreeid());
                    intent.putExtra(SystemConfig.COURNAME, annLists.get(position).getCourName());
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                }
            }
        });
        annLsv.setAdapter(annInfoAdapter);
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
     * 新建公告或者更新公告后返回操作
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        getAnnFromDB();
    }

    /**
     * 新建公告
     */
    @Override
    public void doLeftClick() {
        //ToastMsg.showToast("新建公告");
        startActivity(new Intent(this, NewAnnActivity.class));
    }

    private void getAnnFromDB(){
        annLists.clear();
        annLists.addAll(db.getAnn(showNum,PreferenceUtils.getUserId(getApplicationContext())));
        if (annLists.size()>= 8 && annLists.size()<db.countData(db.TABLE_ANN)) {
            annLists.add(new AnnInfoEntity("LOADINGMORE"));
        }
        annInfoAdapter.notifyDataSetChanged();
    }

    /**
     * 从服务器获取数据更新本地数据库
     * @param flag 区分下拉刷新 1 还是进入更新2
     */
    private void initData(final int flag){
        ArrayList<AnnInfoEntity> arrayList=db.getAnn(1,PreferenceUtils.getUserId(getApplicationContext()));
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams();
        params.put("UserNum", PreferenceUtils.getUserId(getApplicationContext()));
        if (arrayList != null && arrayList.size()>0)
            params.put("AnnNum",arrayList.get(0).getAnnNum());
        else
            params.put("AnnNum",0);

        client.post(SystemConfig.URL_GETANN, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                if (flag == 2)
                    dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.toAnnInfoLists(response)) {
                            getAnnFromDB();
                        }
                        LogUtil.i("success", getClass().getSimpleName() + "Anntrue");
                    } else {
                        if (flag == 2) {
                            LogUtil.i("test", getClass().getSimpleName() + "服务器端未有更多的公告发布!");
                            getAnnFromDB();
                        } else ToastMsg.showToast("已是最新");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "ann连接失败！");
                if (flag == 2) ToastMsg.showToast("连接失败");
                else ToastMsg.showToast("刷新失败");
            }


            @Override
            public void onFinish() {
                if (flag == 2)
                    dialog.dismiss();
                else
                    annLayout.setRefreshing(false);
            }
        });

    }

    /**
     * 去服务器删除该条公告
     * @param num AnnNum
     */
    private void delAnn(final int num){

        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams("AnnNum",num);

        client.post(SystemConfig.URL_DELANN, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                dialog.show();
            }

//            @Override
//            public void onProgress(long bytesWritten, long totalSize) {
//                //super.onProgress(bytesWritten, totalSize);
//                int progress= (totalSize > 0) ? (int) ((bytesWritten * 1.0 / totalSize) * 100) : -1;
//                //Log.i("test", String.valueOf(progress));
//                tvProgress.setText(getString(R.string.text_progress) + String.valueOf(progress)+"%");
//                progressBar_upload.setProgress(progress);
//            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && db.delAnn(num)>0) {
                        LogUtil.i("test", getClass().getSimpleName() + "删除公告成功,id:"+num);
                        getAnnFromDB();
                    }else {
                        LogUtil.i("test", getClass().getSimpleName() + "服务器端出现未知错误 无法删除该公告!");
                        ToastMsg.showToast("服务器端出现未知错误 无法删除该公告!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", getClass().getSimpleName() + "ann连接失败！");
                ToastMsg.showToast("连接失败");
            }


            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }

    /**
     * 需要先更新本地课程表
     */
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
                initData(flag);
            }
        });
    }
}
