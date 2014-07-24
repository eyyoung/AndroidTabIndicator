package cn.com.nd.tabindicator.library;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;


public class TabIndicatorView extends LinearLayout {

    public static final int ANIMATION_DURATION_MILLIS = 300;
    private int mPageNum = 3;// 页数，设置控件权重
    private View mIndicator;

    private int mCurrentPage;// 当前页

    /**
     * @函数名称 :setAnimListener
     * @brief 动画监听
     * @see
     * @param mAnimListener
     *            监听
     * @作者 : 杨扬
     * @创建时间 : 2014-07-08 12:24
     */
    public void setAnimListener(Animation.AnimationListener mAnimListener) {
        this.mAnimListener = mAnimListener;
    }

    private Animation.AnimationListener mAnimListener;

    public TabIndicatorView(Context context, int color) {
        super(context);
        mIndicator = new View(getContext());
        LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        addView(mIndicator, lp);
        mIndicator.setBackgroundColor(color);
        mPageNum = (int) getWeightSum();
    }

    /**
     * @函数名称 :scrollToPage
     * @brief
     * @see
     * @param page
     *            滑动到某页
     * @作者 : 杨扬
     * @创建时间 : 2014-07-08 10:33
     */
    public void scrollToPage(int page) {
        int width = mIndicator.getWidth();
        Animation animation = new TranslateAnimation(width * mCurrentPage,
                width * page, 0, 0);
        mCurrentPage = page;
        animation.setFillAfter(true);
        animation.setDuration(ANIMATION_DURATION_MILLIS);
        mIndicator.startAnimation(animation);
        animation.setAnimationListener(mAnimListener);
    }

    /**
     * @函数名称 :initCursorPosition
     * @brief 初始化指针位置
     * @see
     * @param page
     *            页码
     * @作者 : 杨扬
     * @创建时间 : 2014-07-08 10:52
     */
    public void initCursorPosition(final int page) {
        post(new Runnable() {
            @Override
            public void run() {
                scrollToPage(page);
            }
        });
    }

    /**
     * @函数名称 :setPageNum
     * @brief 设置页数
     * @see
     * @param pageNum
     *            页数
     * @作者 : 杨扬
     * @创建时间 : 2014-07-08 10:42
     */
    public void setPageNum(int pageNum) {
        mPageNum = pageNum;
        setWeightSum(mPageNum);
        requestLayout();
    }

}
