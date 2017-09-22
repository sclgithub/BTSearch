package com.techscl.btsearch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Request;

public class MeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<BT.DataBean> data;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private Adapter adapter;
    private int page, count;
    private int visibleLastIndex = 0;  //最后的可视项索引
    private int visibleItemCount;// 当前窗口可见项总数
    private String key;
    private SimpleDateFormat simpleDateFormat;
    private View footer;
    private String imei, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getString(R.string.me));
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        try {

            phoneNumber = telephonyManager.getLine1Number() != null ? telephonyManager.getLine1Number() : "无USIM卡";
            imei = telephonyManager.getDeviceId() != null ? telephonyManager.getDeviceId() : "模拟器";//weixin://profile/gh_c28e28a2510d
        } catch (Exception e) {
            phoneNumber = "无USIM卡";
            imei = "模拟器";
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent add = new Intent(MeActivity.this, AddActivity.class);
                startActivityForResult(add, 1001);
            }
        });
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.MAGENTA);
        swipeRefreshLayout.setRefreshing(true);
        footer = findViewById(R.id.footer);
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(this);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        key = "";
        count = 10;
        getData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                int itemsLastIndex = adapter.getCount() - 1; //数据集最后一项的索引
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && visibleLastIndex == itemsLastIndex) {
                    // 如果是自动加载,可以在这里放置异步加载数据的代码
                    if (data.size() >= count) {
                        addData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                visibleItemCount = i1;
                visibleLastIndex = i + i1 - 1;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 || resultCode == 1002) {
            swipeRefreshLayout.setRefreshing(true);
            getData();
        }
    }

    private void getData() {
        page = 0;
        if (phoneNumber.equals("无USIM卡")) {
            OkHttpClientManager.getAsyn(AppConfig.SERVER + "api/bt/allMyPhone/" + imei + "/" + page + "/" + count, new OkHttpClientManager.ResultCallback<BT>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onResponse(BT response) {
                    swipeRefreshLayout.setRefreshing(false);
                    data = response.getData();
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            OkHttpClientManager.getAsyn(AppConfig.SERVER + "api/bt/allMyNumber/" + phoneNumber + "/" + page + "/" + count, new OkHttpClientManager.ResultCallback<BT>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onResponse(BT response) {
                    swipeRefreshLayout.setRefreshing(false);
                    data = response.getData();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void addData() {
        listView.setEnabled(false);
        page++;
        footer.setVisibility(View.VISIBLE);
        if (phoneNumber.equals("无USIM卡")) {
            OkHttpClientManager.getAsyn(AppConfig.SERVER + "api/bt/allMyPhone/" + imei + "/" + page + "/" + count, new OkHttpClientManager.ResultCallback<BT>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                    listView.setEnabled(true);
                    footer.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(BT response) {
                    swipeRefreshLayout.setRefreshing(false);
                    listView.setEnabled(true);
                    data.addAll(response.getData());
                    adapter.notifyDataSetChanged();
                    footer.setVisibility(View.GONE);
                }
            });
        } else {
            OkHttpClientManager.getAsyn(AppConfig.SERVER + "api/bt/allMyNumber/" + phoneNumber + "/" + page + "/" + count, new OkHttpClientManager.ResultCallback<BT>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                    listView.setEnabled(true);
                    footer.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(BT response) {
                    swipeRefreshLayout.setRefreshing(false);
                    listView.setEnabled(true);
                    data.addAll(response.getData());
                    adapter.notifyDataSetChanged();
                    footer.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent details = new Intent(MeActivity.this, BTDetailsActivity.class);
        details.putExtra("data", data.get(i));
        startActivityForResult(details, 1002);
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data != null ? data.size() : 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = View.inflate(MeActivity.this, R.layout.item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Glide.with(MeActivity.this)
                    .load(data.get(i).getImgUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.img);
            holder.id.setText(String.valueOf(data.get(i).getId()));
            holder.name.setText(data.get(i).getName());
            holder.content.setText(data.get(i).getContent());
            holder.time.setText("发布时间：" + simpleDateFormat.format(new Date(data.get(i).getUpdateTime())));
            holder.count.setText("关注度：" + data.get(i).getDownload());
            return view;
        }

        class ViewHolder {
            View rootView;
            ImageView img;
            TextView id;
            TextView name;
            TextView content;
            TextView time;
            TextView count;

            ViewHolder(View rootView) {
                this.rootView = rootView;
                this.img = rootView.findViewById(R.id.img);
                this.id = rootView.findViewById(R.id.id);
                this.name = rootView.findViewById(R.id.name);
                this.content = rootView.findViewById(R.id.content);
                this.time = rootView.findViewById(R.id.time);
                this.count = rootView.findViewById(R.id.count);
            }

        }
    }
}
