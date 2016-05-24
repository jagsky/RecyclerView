package com.example.recyclerviewdome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.IllegalFormatException;

/**
 * Created by Administrator on 2016/5/24.
 * ClassName ：com.example.recyclerviewdome
 * 作用：自定义在布局管理者的线性（Linelayout）管理者下RecyclerView的分割线
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    //获取Android下list分割线的属性  attr 属性
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    //定义两个常量，分别用来表示线性布局的两个方向 LinearLayoutManager.HORIZONTAL;LinearLayoutManager.VERTICA
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    /*
    * Drawable类是一个抽象的类，用来描述“可绘制的东西”。经常你处理的Drawable，
    * 是作为一个种资源类型，用来表示绘制在屏幕上的事物。
    * Drawable类提供了通用的API用来处理一些基础的可见的资源，这样方便进行加工。
    * 跟View不同的是，Drawable没有处理事件及交互的能力。
    *除了易于绘制外，Drawable提供了许多方法用作客户端与绘制内容间的交互。*/
    private Drawable mDivider;
    //定义一个变量来判断布局的方向。
    private int mOrientation;

    //重写构造方法，获取需要的资源，比如要配置给对方的 上下文，orientation（方向）
    public DividerItemDecoration(Context context, int orientation) {
        /*定义：索检
        public TypedArray obtainStyledAttributes (AttributeSet set, int[] attrs,
                                                int defStyleAttr, int defStyleRes)
        public TypedArray obtainAttributes (AttributeSet set, int[] attrs)（说明此函数）
        说明：返回一个由AttributeSet获得的一系列的基本的属性值，不需要用用一个主题或者/和样式资源执行样式。
        参数：
        set：现在检索的属性值；
        attrs：制定的检索的属性值
        public void recycle()
        返回先前检索的数组，稍后再用。
        * */
        //因为这句话的意思是： TypedArray a = context.obtainStyledAttributes(ATTRS);
        //在我设置给那个那个类时（context表示的类），同时获取其布局的属性，并为这个类设置一些布局的方向等属性
        //同时可以给那个Activity类设置必要的属性
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.mOrientation = orientation;
    }

    //接下来在判断了布局的属性后，就是画分割线 state 表示对RecyclerView的声明
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //判断在线性布局下的方向
        if (mOrientation == HORIZONTAL_LIST) {
            //在水平布局下执行此方法
            drawHorizontal(c, parent);
        } else {
            //在垂直布局下执行此方法
            drawVertical(c, parent);
        }

    }

    //在垂直布局下执行此方法，本质就是RecyclerView中获取各个控件的位置，并进行绘制。
    private void drawVertical(Canvas c, RecyclerView parent) {
        //如何是垂直布局的时候，我们就需要获得RecyclerView的item中的左右值是多少。
        //因为他的宽度可能不是mact_parent.
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        //同时还要获取RecyclerView的儿子的个数 Child儿子的意思
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //循环获取parent儿子的View属性。
            final View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            /*
            * LayoutParams相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息。
            * 假设在屏幕上一块区域是由一个Layout占领的，
            * 如果将一个View添加到一个Layout中，最好告诉Layout用户期望的布局方式，
            * 也就是将一个认可的layoutParams传递进去。
            * 可以这样去形容LayoutParams，在象棋的棋盘上，每个棋子都占据一个位置，
            * 也就是每个棋子都有一个位置的信息，如这个棋子在4行4列，这里的“4行4列”就是棋子的LayoutParams。
            * 但LayoutParams类也只是简单的描述了宽高，宽和高都可以设置成三种值：
            *1，一个确定的值；
            *2，FILL_PARENT，即填满（和父容器一样大小）；
            *3，WRAP_CONTENT，即包裹住组件就好。
            * */
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //在水平布局下执行此方法,与垂直布局一样，把方向反过来想就可以了
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //getItemOffsets中，outRect去设置了绘制的范围。onDraw中实现真正的绘制

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }

    }
}
