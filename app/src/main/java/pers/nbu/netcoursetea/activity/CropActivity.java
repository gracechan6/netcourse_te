package pers.nbu.netcoursetea.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cropper.CropImageView;
import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.config.SystemConfig;
import pers.nbu.netcoursetea.util.LogUtil;
import pers.nbu.netcoursetea.util.PreferenceUtils;


/*import app.frame.cropper.CropImageView;*/


public class CropActivity extends Activity {
    public static final int PIC_REQUEST_CODE = 0x01;
    private static final int TAKE_PHOTO = 0x02;
    private static int CROP_REQUEST_CODE = 10001;
    private CropImageView mCropImageView;
    private Button btnOk;
    private Button btnCancel;
    private Bitmap mBitMap;
    private OnCropedListener mOnCropedListener;

    public static final String BITMAP_DATA="BITMAP_DATA";

    /**
     * 保存图片
     */
    public static interface OnCropedListener
    {
        public void onCroped(Bitmap bitmap);
    }

    public void setOnCropedListener(OnCropedListener listener)
    {
        Log.i(getClass().getSimpleName(), "enter setOnCropedListener");
        mOnCropedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i(getClass().getSimpleName(), "enter onCreate");
        setContentView(R.layout.activity_crop);
        initView();
    }

    private void initView(){
        btnOk = (Button)findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        btnCancel = (Button)findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        mCropImageView = (CropImageView)findViewById(R.id.CropImageView);
        mCropImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        int type = getIntent().getIntExtra("PHOTO_TYPE", 1);

        if (type == 0)
        {
            takePhoto();
        }
        else
        {
            pic();
        }

    }
    protected final void upload()
    {
        //保存截图，返回保存文件路径
        LogUtil.i(getClass().getSimpleName(), "enter upload " + (SystemConfig.PATH_HEAD + PreferenceUtils.getUserId(getApplication()) + SystemConfig.HEAD_TYPE));
        //保存图片到文件，传递文件名给调用者

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Toast.makeText(getApplication(), getString(R.string.refuse_sdcard), Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(SystemConfig.PATH_HEAD);
        file.mkdirs();// 创建文件夹

        String fileName =SystemConfig.PATH_HEAD + PreferenceUtils.getUserId(getApplication())+ SystemConfig.HEAD_TYPE;//图片名字
        try {
            FileOutputStream b = new FileOutputStream(fileName);
            mCropImageView.getCroppedImage().compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
            b.flush();
            b.close();
        } catch (FileNotFoundException e) {
            Log.e(getClass().getSimpleName(), e.getMessage().toString());
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage().toString());
        }
        Intent data = new Intent();
        data.putExtra(BITMAP_DATA, file.getAbsolutePath());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    protected final void cancel()
    {
        finish();
    }

    private void takePhoto()
    {
        Log.i(getClass().getSimpleName(), "enter takePhoto");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //文件夹tmp======拍照的图片保存在此文件夹下
        String path = SystemConfig.PATH_HEAD+"tmp/";
        File path1 = new File(path);
        if(!path1.exists()){
            path1.mkdirs();
        }
        File file = new File(path1,"face"+".png");
        Uri photUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void pic()
    {
        //清除Bitmap
        if (null != mBitMap)
        {
            mBitMap.recycle();
            System.gc();
        }
        Intent intent = new Intent(Intent.ACTION_PICK);//ACTION_OPEN_DOCUMENT
        intent.setType("image/jpeg");
        startActivityForResult(intent, PIC_REQUEST_CODE);
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    Log.e("CropActivity", "删除图片是否成功：" + success);
                    return success;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    //将图片放大缩小独立出来
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i(getClass().getSimpleName(), "enter onActivityResult");
        //图片选择后返回选择文件名，按文件名称打开图片文件，进行裁剪
        //如果图片太大，图片会以适应屏幕宽度进行重新转换大小，以显示
        if (requestCode == PIC_REQUEST_CODE && resultCode == RESULT_OK && null != data)
        {
            //Uri转换成图片路径
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //转换 end
            Log.i(getClass().getSimpleName(), "cropdata!=null" + picturePath);
            crop(picturePath);
        }
        else if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK)
        {
            String path = SystemConfig.PATH_HEAD+"tmp";
            crop(path + File.separator + "face.png");
            File file=new File(path);
            if(file.exists())
                LogUtil.i(getClass().getSimpleName(), String.valueOf(deleteDir(file)));
        }else if(resultCode== Activity.RESULT_CANCELED){
            finish();
            return;
        }
    }

    private void crop(String picturePath)
    {
        Log.i(getClass().getSimpleName(), "enter crop" + picturePath);
        int inSize = 1;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = inSize;
        options.inScreenDensity = getWindowInfo(this).densityDpi;
        options.inDensity = getWindowInfo(this).densityDpi;
        options.inTargetDensity = getWindowInfo(this).densityDpi;
        options.inScaled = true;

        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(new File(picturePath));
            Log.i(getClass().getSimpleName(), "enter inputStream " + inputStream);
        } catch (FileNotFoundException e)
        {
            //e.printStackTrace();
            LogUtil.e("error"+getClass().getSimpleName(),"创建inputStream失败");
        }

        if (null != inputStream)
        {
            mBitMap = BitmapFactory.decodeStream(inputStream, new Rect(0, 0, 0, 0), options);
            float scale = (float) getScreenWidth(this) / mBitMap.getWidth();
            //图片太大，进行缩放，屏幕大小
            if (scale < 1)
            {
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                mBitMap = Bitmap.createBitmap(mBitMap, 0, 0, mBitMap.getWidth(), mBitMap.getHeight(), matrix, true);
            }

            Log.i(getClass().getSimpleName(), "With[" + mBitMap.getWidth() + "], Height[" + mBitMap.getHeight() + "]");
            mCropImageView.setImageBitmap(mBitMap);

            try {
                inputStream.close();
            } catch (IOException e) {
                //e.printStackTrace();
                LogUtil.e("error"+getClass().getSimpleName(),"关闭inputStream失败");
            }
        }
    }
    /**
     * 获取屏幕宽度
     */
    public static final int getScreenWidth(Context context)
    {
        DisplayMetrics metric = getWindowInfo(context);
        return metric.widthPixels;
    }
    /**
     * 获取屏幕属性
     */
    public static final DisplayMetrics getWindowInfo(Context context)
    {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        return metric;
    }
    /**
     * 获取屏幕高度
     */
    public static final int getScreenHeight(Context context)
    {
        DisplayMetrics metric = getWindowInfo(context);
        return metric.heightPixels;
    }
}

