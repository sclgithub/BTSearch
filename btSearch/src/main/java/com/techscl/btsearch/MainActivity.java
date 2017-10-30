package com.techscl.btsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent add = new Intent(MainActivity.this, AddActivity.class);
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
        OkHttpClientManager.getAsyn(AppConfig.SERVER + "api/bt/all/" + key + "/" + page + "/" + count, new OkHttpClientManager.ResultCallback<BT>() {
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

    private void addData() {
        listView.setEnabled(false);
        page++;
        footer.setVisibility(View.VISIBLE);
        OkHttpClientManager.getAsyn(AppConfig.SERVER + "api/bt/all/" + key + "/" + page + "/" + count, new OkHttpClientManager.ResultCallback<BT>() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_settings).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                key = newText;
                getData();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.me) {
//            startActivity(new Intent(MainActivity.this, MeActivity.class));
            Map<String, String> params = new HashMap<>();
            params.put("payWay", "5");
            params.put("ppId", "784a2c314cb24611b9834579b2bb0ec2");
            params.put("pmId", "94421c98410b436d90a466f9363b2a80");
            params.put("business_name", "app支付测试");
            params.put("po_desc", "app支付测试");
            params.put("po_business_num", "1008");
            params.put("po_money", "0.01");
            OkHttpClientManager.postAsyn("http://172.16.150.53:8088/alipay/pay", new OkHttpClientManager.ResultCallback<ResponseInfo>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(ResponseInfo response) {
                    final String orderInfo = response.getData().toString();   // 订单信息

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(MainActivity.this);
                            alipay.payV2(orderInfo, true);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }, params);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent details = new Intent(MainActivity.this, BTDetailsActivity.class);
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
                view = View.inflate(MainActivity.this, R.layout.item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Glide.with(MainActivity.this)
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
