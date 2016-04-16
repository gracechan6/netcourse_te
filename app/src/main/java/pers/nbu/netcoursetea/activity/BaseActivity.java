package pers.nbu.netcoursetea.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pers.nbu.netcoursetea.ActivityCollector;
import pers.nbu.netcoursetea.R;


public class BaseActivity extends Activity {

	protected Toolbar mToolBar;
	protected TextView mTitle;
	protected TextView mRight,mLeft;
	protected Drawable drawable;

	protected Toolbar.LayoutParams lp;

	/**
	 * 条理进度对话框
	 */
	protected Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		LayoutInflater inflater=LayoutInflater.from(this);
		View modifyView=inflater.inflate(R.layout.layout_progressbar_load, null);
		dialog=new AlertDialog.Builder(this).
				setView(modifyView).
				create();
		dialog.setCanceledOnTouchOutside(false);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 设置标题
	 * @param title
	 */
	protected void setTitle(String title)
	{
		this.mTitle.setText(title);
	}

	/**
	 * 初始化导航栏
	 */
	protected void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbar);
		mToolBar.setBackgroundColor(getResources().getColor(R.color.toolbar_bg));
		//setSupportActionBar(mToolBar);

		//设置标题
		lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		mTitle = new TextView(this);
		mTitle.setTextColor(getResources().getColor(R.color.black));
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		mToolBar.addView(mTitle, lp);
	}

	public void doRefreshClick(){

	}

	public void doLeftClick(){

	}


	/**
	 * 设置导航栏左侧刷新
	 * @param b 是否显示图标
	 */
	protected void setLeftOfToolbar(Boolean b){
		if (mLeft == null){
			lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.LEFT;
			mLeft = new TextView(this);
			mLeft.setText("");
			mLeft.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			mLeft.setTextColor(getResources().getColor(R.color.bottom_text_selected));
			mLeft.setPadding(0, 0, 20, 0);
			mLeft.setGravity(Gravity.CENTER);
			mToolBar.addView(mLeft, lp);
		}
		if (b){
			Drawable img_back=getResources().getDrawable(R.drawable.refresh_icon_select);
			img_back.setBounds(0, 0, img_back.getMinimumWidth(), img_back.getMinimumHeight());
			mLeft.setCompoundDrawables(img_back, null, null, null);
			mLeft.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					doRefreshClick();
				}
			});
		}
		else
			mLeft.setCompoundDrawables(null, null, null, null);
	}

	/**
	 * 设置导航栏左侧新建
	 * @param name   新公告  新任务 新建考勤
	 */
	protected void setLeft(String name){
		if (mLeft == null){
			lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.LEFT;
			mLeft = new TextView(this);
			mLeft.setText(name);
			mLeft.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			mLeft.setTextColor(getResources().getColor(R.color.bottom_text_selected));
			mLeft.setPadding(0, 0, 20, 0);
			mLeft.setGravity(Gravity.CENTER);
			mToolBar.addView(mLeft, lp);
			mLeft.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					doLeftClick();
				}
			});
		}
	}

	/**
	 * 设置导航栏右侧返回
	 */
	protected void setRightOfToolbar(Boolean b){
		if (mRight == null){
			lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.RIGHT;
			mRight = new TextView(this);
			mRight.setText("");
			mRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			mRight.setTextColor(getResources().getColor(R.color.bottom_text_selected));
			mRight.setPadding(0, 0, 20, 0);
			mRight.setGravity(Gravity.CENTER);
			mToolBar.addView(mRight, lp);
		}
		if (b){
			Drawable img_back=getResources().getDrawable(R.drawable.back_icon_select);
			img_back.setBounds(0, 0, img_back.getMinimumWidth(), img_back.getMinimumHeight());
			mRight.setCompoundDrawables(img_back, null, null, null);
			mRight.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					finish();
				}
			});
		}
		else
			mRight.setCompoundDrawables(null, null, null, null);
	}

	/**
	 * 是否隐藏导航栏，默认开启
	 * 注意，有个操作隐藏后之后操作要记得开回来
	 * @param b
	 */
	protected void hideToolbar(Boolean b){
		if (b) mToolBar.setVisibility(View.GONE);
		else mToolBar.setVisibility(View.VISIBLE);
	}
	
}
