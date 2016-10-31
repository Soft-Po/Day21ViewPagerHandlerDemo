package com.softpo.day21viewpagerhandlerdemo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.softpo.day21viewpagerhandlerdemo.adapters.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerHandlerFragment extends Fragment {

    private int currentPosition = Integer.MAX_VALUE/2;

    private int lastPosition = Integer.MAX_VALUE/2%7;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(this.hasMessages(1)){
                //移出多个Message，保证只有一个
                this.removeMessages(1);
            }
            switch (msg.what){
                case 1://进行ViewPager切换
                    currentPosition++;
                    mViewPager.setCurrentItem(currentPosition);
                    this.sendEmptyMessageDelayed(1,3000);
                    mFlag = true;
                    break;
                case 2:
                    if(this.hasMessages(1)){
                        //移出了Message，自动的切换就会停止
                        this.removeMessages(1);
                    }
                    break;
                case 3:
                    //手滑动的时候，页码变，需要对页码重新赋值
                    currentPosition = msg.arg1;
                    this.sendEmptyMessageDelayed(1,3000);
                    break;
            }


        }
    };
    private ViewPager mViewPager;
    private boolean mFlag;
    private LinearLayout mIndicator;

    public ViewPagerHandlerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_view_pager_handler, container, false);

        //ListView，可以多布局展示数据
        initListView(ret);



        return ret;
    }

    private void initIndicator(View headView) {

        mIndicator = (LinearLayout) headView.findViewById(R.id.indicator);

        for (int i = 0; i < 7; i++) {

            View view = new View(getContext());

            view.setBackgroundResource(R.drawable.bg_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);

            params.leftMargin = 10;
            params.rightMargin = 10;

            view.setLayoutParams(params);

            mIndicator.addView(view);

            view.setEnabled(false);
        }
        //设置默认值0，选中状态
        mIndicator.getChildAt(currentPosition%7).setEnabled(true);
    }

    private void initListView(View ret) {
        //1、ListView
        ListView listView = (ListView) ret.findViewById(R.id.listView);

        //2、数据源
        List<Map<String,Object>> data = new ArrayList<>();
        String[] icons = getContext().getResources().getStringArray(R.array.appIcon);
        String[] names = getContext().getResources().getStringArray(R.array.appName);
        String[] counts = getContext().getResources().getStringArray(R.array.appCount);
        for (int i = 0; i < icons.length; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("icon",icons[i]);
            map.put("name",names[i]);
            map.put("count",counts[i]);
            //!!!
            data.add(map);
        }
        //3、添加头布局
        View headView = LayoutInflater.from(getContext())
                .inflate(R.layout.header_item,listView,false);

        //初始化ViewPager
        initViewPager(headView);

        initIndicator(headView);

        listView.addHeaderView(headView);

        //4、设置适配器
        BaseAdapter adapter = new MyAdapter(getContext(),data);

        listView.setAdapter(adapter);


    }

    private void initViewPager(View headView) {
        //TODO 对ViewPager进行设置
        mViewPager = (ViewPager) headView.findViewById(R.id.viewPager);

        final List<ImageView> data = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            ImageView imageView = new ImageView(getContext());

            imageView.setImageResource(getContext()
                    .getResources().getIdentifier(
                            "p"+(i+1),
                            "mipmap",
                            getContext().getPackageName()));

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            data.add(imageView);
        }

        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
//                return data!=null?data.size():0;
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View ret = data.get(position%7);

                container.addView(ret);

                return ret;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                container.removeView(data.get(position%7));
            }
        };

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2);

        //调用Handler发送消息
        mHandler.sendEmptyMessageDelayed(1,3000);

        //对ViewPager设置滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //当前的选中位置position
            @Override
            public void onPageSelected(int position) {
                mHandler.sendMessage(Message.obtain(mHandler,3,position,0));

                mIndicator.getChildAt(position%7).setEnabled(true);

                mIndicator.getChildAt(lastPosition).setEnabled(false);

                lastPosition = position%7;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_DRAGGING://手正在拖拽
                        mHandler.sendEmptyMessage(2);
                    break;
                }

            }
        });

    }

}
