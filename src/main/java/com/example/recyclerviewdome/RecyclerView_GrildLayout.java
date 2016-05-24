package com.example.recyclerviewdome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Administrator on 2016/5/24.
 * ClassName ：com.example.recyclerviewdome
 * 作用：
 */
public class RecyclerView_GrildLayout extends RecyclerView.ItemDecoration {
    //获取Android下list分割线的属性  attr 属性
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    //获取画板
    private Drawable mDivider;

    //重写构成方法，获取对应的Context
    public RecyclerView_GrildLayout(Context context) {
        //制定的检索的属性值
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        //获取对应Activity的画布（XML）。重0开始
        mDivider = a.getDrawable(0);
        a.recycle();

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //因为格子布局是双向的，所以要同时绘制两个方向
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    //获取格子布局中的范围个数
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        //获取父容器的布局管理者
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //我个人理解的一个应用场合就是，当你拿到一个对象的引用时（例如参数），
        // 你可能需要判断这个引用真正指向的类。
        // 所以你需要从该类继承树的最底层开始，
        // 使用instanceof操作符判断，第一个结果为true的类即为引用真正指向的类。
        //
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }

        return spanCount;
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

    }

    //判断要绘制的边界是否是最后一列
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //如果这个布局管理者是GridLayoutManager
        if (layoutManager instanceof GridLayoutManager) {
            // 如果是最后一列，则不需要绘制右边
            if ((pos + 1) % spanCount == 0) {
                return true;
                //瀑布流布局 StaggeredGridlayout
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //判断瀑布流布局的方向
                int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                    {
                        return true;

                    } else {
                        childCount = childCount - childCount % spanCount;
                        if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                            return true;
                    }
                }
            }
        }
        return false;
    }

    //判断是否是最后一行 pos表示下标 spanCount表示总个数
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                                    int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //首先要获得最后一行的个数
            childCount = pos - childCount % spanCount;
            if (pos >= childCount) {
                // 如果是最后一行，则不需要绘制底部
                return true;
            }

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    //getItemOffsets中，outRect去设置了绘制的范围。onDraw中实现真正的绘制
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent)
    {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
        {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
        {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else
        {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(),
                    mDivider.getIntrinsicHeight());
        }
    }
}
