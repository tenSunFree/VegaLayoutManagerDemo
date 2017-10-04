package com.tensun.vegalayoutmanagerdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<StockEntity> dataList = new ArrayList<>();
    private int redColor, greenColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** 1. 沉浸式状态栏 + dark模式 */
        View positionView = findViewById(R.id.main_position_view);

        boolean immerse = Utils.immerseStatusBar(this);
        boolean darkMode = Utils.setDarkMode(this);

        /**
         * immerse為true 表示Android4.4以上
         * immerse為false 表示未滿Android4.4
         *
         * 補充: 一個View實際上同時有兩種width和height值
         *           第一種 getMeasuredWidth(), 跟xml裡面設定的數值有關係, 主要是預先設想的概念, 但是不見得和最後在手機上呈現的是一樣的數值
         *           第二種 getWidth(), 最後呈現在手機上的數值, 也就是最終的結果數值
         */
        if (immerse) {                                                                              // 判斷是 Android4.4以上的, 還是未滿Android4.4
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();                             // 每個view都需要一個LayoutParams, 這裡是 取得positionView的參數控制權
            lp.height = Utils.getStatusBarHeight(this);
            positionView.setLayoutParams(lp);

            /** 如果是小米或者魅族 或者Android6.0以上的, 不用另外設置顏色 */
            if (!darkMode) {
                positionView.setBackgroundColor(Color.BLACK);                                      // 將positionView 的背景色設成黑色
            }
        } else {
            positionView.setVisibility(View.GONE);                                                 // 隱藏自定義的View
        }

        /** 2. toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);                                                               // 如果要實現setDisplayHomeAsUpEnabled(), 必須要先對toolbar 實體化
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);                                      // 设置是否添加显示NavigationIcon, 如果沒有另外透過setNavigationIcon() 設置圖片來源的話, 預設是顯示官方的圖案

        /** 3. recyclerView数据填充 */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new VegaLayoutManager());
        RecyclerView.Adapter adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        redColor = getResources().getColor(R.color.red);
        greenColor = getResources().getColor(R.color.green);

        prepareDataList();
        adapter.notifyDataSetChanged();
    }

    private void prepareDataList() {
        for (int i = 0; i < 2; i++) {
            dataList.add(new StockEntity("麻省理工學院", "https://upload.wikimedia.org/wikipedia/zh/thumb/4/44/MIT_Seal.svg/1200px-MIT_Seal.svg.png"));
            dataList.add(new StockEntity("斯坦福大學", "https://upload.wikimedia.org/wikipedia/en/thumb/b/b7/Stanford_University_seal_2003.svg/1200px-Stanford_University_seal_2003.svg.png"));
            dataList.add(new StockEntity("哈佛大學", "https://upload.wikimedia.org/wikipedia/zh/thumb/6/6e/Harvard_Wreath.svg/1053px-Harvard_Wreath.svg.png"));
            dataList.add(new StockEntity("加州理工學院", "https://www.ceu.edu/sites/default/files/main_image/article/17995/caltechlogoseal.png"));
            dataList.add(new StockEntity("劍橋大學", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/University_of_Cambridge_coat_of_arms.svg/1000px-University_of_Cambridge_coat_of_arms.svg.png"));
            dataList.add(new StockEntity("牛津大學", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Oxford-University-Circlet.svg/1200px-Oxford-University-Circlet.svg.png"));
            dataList.add(new StockEntity("倫敦大學學院", "http://www.huntlogo.com/wp-content/uploads/2011/09/imperial-college-london-logo.png"));
            dataList.add(new StockEntity("帝國理工學院", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Imperial_College_London_crest.svg/960px-Imperial_College_London_crest.svg.png"));
            dataList.add(new StockEntity("芝加哥大學", "https://upload.wikimedia.org/wikipedia/zh/thumb/f/f2/University_of_Chicago_Modern_Etched_Seal.svg/822px-University_of_Chicago_Modern_Etched_Seal.svg.png"));
            dataList.add(new StockEntity("蘇黎世聯邦理工學院", "http://dl.keywin.org/a/b/ab855ec78ae24ad6ba6cbd7e2a66c884.png"));
            dataList.add(new StockEntity("洛桑聯邦理工學院", "http://4vector.com/i/free-vector-epfl_070472_epfl.png"));
            dataList.add(new StockEntity("普林斯頓大學", "https://upload.wikimedia.org/wikipedia/zh/thumb/7/71/Princeton_shield.svg/804px-Princeton_shield.svg.png"));
            dataList.add(new StockEntity("康奈爾大學", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Cornell_University_seal.svg/500px-Cornell_University_seal.svg.png"));
            dataList.add(new StockEntity("新加坡國立大學", "http://www.epigami.sg/images/school/thumb27.jpg"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private RecyclerView.Adapter getAdapter() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.recycler_item, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                MyViewHolder myHolder = (MyViewHolder) holder;
                myHolder.bindData(dataList.get(position));
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }
        };
        return adapter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
            this.image = (ImageView) itemView.findViewById(R.id.image);
        }

        public void bindData(StockEntity stockEntity) {
            text.setText(stockEntity.getText());
            Glide.with(itemView.getContext()).load(stockEntity.getImage()).into(image);
        }
    }
}
