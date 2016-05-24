package com.example.recyclerviewdome;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends ActionBarActivity {


    //创建一个List集合，存放显示的文字数据
    List<String> mDatas;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        //设置布局管理者用什么样的的管理方式，LayoutManager这个我们也可以自己定义
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new HomeAdapter());
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    //必须手动设置RecyclerView的适配器，重写ViewHolder这个类。ViewHolder在此方法的使用中类似于View管理器。
    //因此可以ViewHolder获取控件
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        //创建一个ViewHolder，并返回一个ViewHolder，这样在加载布局时，就不会重新在加载item。
        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建适配器，获得自定义的ViewHolder
            //LayoutInflater这个类还是非常有用的，它的作用类似于findViewById()。
            // 不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；
            // 而findViewById()是找xml布局文件下的具体widget控件(如Button、TextView等)。具体作用：
            // 1、对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
            //2、对于一个已经载入的界面，就可以使用Activiyt.findViewById()方法来获得其中的界面元素。
            /*
            * 通过 sdk 的 api 文档，可以知道该方法有以下几种过载形式，返回值均是 View 对象，如下：
            public View inflate (int resource, ViewGroup root)
            public View inflate (XmlPullParser parser, ViewGroup root)
            public View inflate (XmlPullParser parser, ViewGroup root, boolean attachToRoot)
            public View inflate (int resource, ViewGroup root, boolean attachToRoot)
            示意代码：
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom, (ViewGroup)findViewById(R.id.test));
            //EditText editText = (EditText)findViewById(R.id.content);// error
            EditText editText = (EditText)view.findViewById(R.id.content);
            对于上面代码，指定了第二个参数 ViewGroup root，当然你也可以设置为 null 值。
            注意：
            ·inflate 方法与 findViewById 方法不同；
            ·inflater 是用来找 res/layout 下的 xml 布局文件，并且实例化；
            ·findViewById() 是找具体 xml 布局文件中的具体 widget 控件(如:Button、TextView 等)。*/
            MyViewHolder holder = new MyViewHolder(LayoutInflater.
                    from(MainActivity.this).inflate(R.layout.item, parent, false));
            return holder;
        }

        //
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(mDatas.get(position));

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
