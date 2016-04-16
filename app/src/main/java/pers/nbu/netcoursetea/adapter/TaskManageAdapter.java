package pers.nbu.netcoursetea.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.entity.TaskManageEntity;

/**
 * Created by gracechan on 2015/11/9.
 */
public class TaskManageAdapter extends BaseAdapter{

    class ViewHolder{
        public TextView title,turnOver;
    }
    private List<TaskManageEntity> infoEntityList;
    private TaskManageEntity entity;
    private ViewHolder viewHolder;
    private Context context;
    private LayoutInflater inflater;

    public TaskManageAdapter(List<TaskManageEntity> infoEntityList, Context context) {
        this.infoEntityList = infoEntityList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infoEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_taskmanage, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.turnOver = (TextView) convertView.findViewById(R.id.turnOver);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        entity=infoEntityList.get(position);
        if (entity.getTaskTitle().equals("LOADINGMORE")){
            viewHolder.title.setText("加载更多");
            Drawable img_back=context.getResources().getDrawable(R.drawable.icon_load);
            img_back.setBounds(0, 0, img_back.getMinimumWidth(), img_back.getMinimumHeight());
            viewHolder.title.setCompoundDrawables(img_back, null, null, null);
            viewHolder.title.setGravity(Gravity.CENTER);
            viewHolder.turnOver.setVisibility(View.GONE);
        }else {
            viewHolder.title.setText("[标题]" + entity.getTaskTitle());
            if (entity.getOpusNum() != 0) {
                viewHolder.turnOver.setText("上交情况： " + "已交");
                viewHolder.turnOver.setTextColor(context.getResources().getColor(R.color.announ_content));
            }
            else {
                viewHolder.turnOver.setText("上交情况： " + "未交");
                viewHolder.turnOver.setTextColor(context.getResources().getColor(R.color.red_select));
            }

            viewHolder.title.setCompoundDrawables(null, null, null, null);
            viewHolder.title.setGravity(Gravity.LEFT);
            viewHolder.turnOver.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
