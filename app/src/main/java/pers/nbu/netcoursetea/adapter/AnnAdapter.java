package pers.nbu.netcoursetea.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pers.nbu.netcoursetea.R;
import pers.nbu.netcoursetea.entity.AnnEntity;

/**
 * Created by gracechan on 2015/11/9.
 */
public class AnnAdapter extends BaseAdapter{

    class ViewHolder{
        /*public View unread;*/
        public TextView title,content;
    }
    private List<AnnEntity> infoEntityList;
    private AnnEntity entity;
    private ViewHolder viewHolder;
    private Context context;
    private LayoutInflater inflater;

    public AnnAdapter(List<AnnEntity> infoEntityList, Context context) {
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
            convertView = inflater.inflate(R.layout.layout_announshow, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        entity=infoEntityList.get(position);
        if (entity.getAnnTitle().equals("LOADINGMORE")){
            viewHolder.title.setText("加载更多");
            Drawable img_back=context.getResources().getDrawable(R.drawable.icon_load);
            img_back.setBounds(0, 0, img_back.getMinimumWidth(), img_back.getMinimumHeight());
            viewHolder.title.setCompoundDrawables(img_back, null, null, null);
            //viewHolder.title.setCompoundDrawablePadding(10);
            viewHolder.title.setGravity(Gravity.CENTER);
            viewHolder.content.setVisibility(View.GONE);
        }
        else {
            viewHolder.title.setText(entity.getAnnTitle());
            viewHolder.content.setText(Html.fromHtml(entity.getAnnCon()).toString());
            viewHolder.content.setVisibility(View.VISIBLE);
            viewHolder.title.setCompoundDrawables(null, null, null, null);
            viewHolder.title.setGravity(Gravity.LEFT);
        }
        return convertView;
    }
}
