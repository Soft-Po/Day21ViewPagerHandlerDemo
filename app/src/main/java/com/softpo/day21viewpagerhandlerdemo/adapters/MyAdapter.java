package com.softpo.day21viewpagerhandlerdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.softpo.day21viewpagerhandlerdemo.R;

import java.util.List;
import java.util.Map;

/**
 * Created by softpo on 2016/10/31.
 */
//多布局展示数据
public class MyAdapter extends BaseAdapter {
    public static final int TYPE_TWO = 1;
    public static final int TYPE_ONE = 0;
    private Context mContext;
    private List<Map<String,Object>> mData;
    public MyAdapter(Context context, List<Map<String, Object>> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData!=null?mData.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //总共两种布局
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        //两种布局，需要两种ViewHolder
        ViewHolderOne holderOne = null;
        ViewHolderTwo holderTwo = null;

        //区分，调用getItemViewType();
        int type = getItemViewType(position);
        switch (type){
            case TYPE_ONE:
                if (convertView == null) {
                    ret = LayoutInflater.from(mContext).inflate(R.layout.item_one,parent,false);
                    holderOne = new ViewHolderOne();
                    holderOne.mImageView = (ImageView) ret.findViewById(R.id.imageView);
                    holderOne.count = (TextView) ret.findViewById(R.id.count);
                    holderOne.name = (TextView) ret.findViewById(R.id.name);

                    ret.setTag(holderOne);
                }else {
                    ret = convertView;
                    holderOne = (ViewHolderOne) ret.getTag();
                }

                //赋值
                Map<String, Object> map = mData.get(position);
                //设置图标
                holderOne.mImageView
                        .setImageResource(mContext
                                .getResources()
                                .getIdentifier(
                                        ((String) map.get("icon")),
                                        "mipmap",
                                        mContext.getPackageName()));
                //设置名称
                holderOne.name.setText(((String) map.get("name")));
                //设置下载量
                holderOne.count.setText("下载量是："+ ((String) map.get("count"))+"万");
                break;
            case TYPE_TWO:
                if (convertView != null) {
                    ret = convertView;
                    holderTwo = (ViewHolderTwo) ret.getTag();
                }else {
                    //三张图片
                    ret = LayoutInflater.from(mContext).inflate(R.layout.item_two,parent,false);

                    holderTwo = new ViewHolderTwo();

                    holderTwo.imgOne = (ImageView) ret.findViewById(R.id.imgOne);
                    holderTwo.imgTwo = (ImageView) ret.findViewById(R.id.imgTwo);
                    holderTwo.imgThree = (ImageView) ret.findViewById(R.id.imgThree);
                    ret.setTag(holderTwo);
                }

                //进行赋值
                if(position == 2){
                    //使用布局种默认图片
                }else if(position == 5){
                    holderTwo.imgOne.setImageResource(R.mipmap.a);
                    holderTwo.imgTwo.setImageResource(R.mipmap.b);
                    holderTwo.imgThree.setImageResource(R.mipmap.f);
                }
                break;
        }
        return ret;
    }
    //一张图片，两个文本，Button
    private static class ViewHolderOne{
        private ImageView mImageView;
        private TextView name,count;
        private Button download;
    }
    //三张图片
    private static class ViewHolderTwo{
        private ImageView imgOne,imgTwo,imgThree;
    }

    //多布局时，还需要重写其他的两个方法：getViewTypeCount()；getItemViewType(int position)
    //告诉BaseAdapter，有几种布局
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    //返回某一个条目Item位置的类型
    @Override
    public int getItemViewType(int position) {
        if(position == 2||position == 5){
            return TYPE_TWO;
        }else {//0,1,3,4,6
            return TYPE_ONE;
        }
    }
}
