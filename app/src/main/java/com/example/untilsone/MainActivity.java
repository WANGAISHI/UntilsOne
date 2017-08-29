package com.example.untilsone;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private String url="http://v.juhe.cn/toutiao/index?type=&key=22a108244dbb8d1f49967cd74a0c144d";

    private ArrayList<News> list;

    @ViewInject(R.id.lv)
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDate();
        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        list= new ArrayList<News>();
    }


    private void initDate() {
        RequestParams params=new RequestParams(url);
        x.view().inject(this);
        x.http().get(params,new Callback.CommonCallback<String>()
        {

            private Myadapter ma;

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj=new JSONObject(result);
                    JSONObject resultobj = obj.getJSONObject("result");
                    JSONArray dataArray = resultobj.getJSONArray("data");
                    if(dataArray!=null&&dataArray.length()>0)
                    {
                        for (int i = 0; i < dataArray.length() ; i++) {
                            JSONObject data = (JSONObject) dataArray.get(i);
                            News news=new News();
                            news.title=data.getString("title");
                            news.thumbnail_pic_s=data.optString("thumbnail_pic_s");
                            list.add(news);
                        }

                        ma = new Myadapter();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                lv.setAdapter(ma);
            }
        });
    }

    class Myadapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(MainActivity.this, R.layout.item_lv, null);
            TextView tv_title = view.findViewById(R.id.tv_title);
            ImageView tv_image=view.findViewById(R.id.tv_image);
            tv_title.setText(list.get(i).title);
            ImageLoader.getInstance().displayImage(list.get(i).thumbnail_pic_s,tv_image);
            return view;
        }
    }
}
