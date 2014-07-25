package cn.com.nd.tabindicator.library;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabIndicator extends LinearLayout {

    public void setOnPageChangeListener(
            OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public int getCurPage() {
        return mCurPage;
    }

    public void setCurPage(int curPage) {
        mCurPage = curPage;
        mTitleViews[curPage].performClick();
    }

    /**
     * 页面变更回调
     */
    public static interface OnPageChangeListener {
        /**
         * 页面变更
         * 
         * @param oldPage
         *            旧页码
         * @param newPage
         *            新页码
         */
        void onPageChange(int oldPage, int newPage);
    }

    public OnPageChangeListener mOnPageChangeListener;

    private final LinearLayout mTabLayout;// 标题布局
    private final int mIndicatorColor;
    private final int mIndicatorBackColor; // 指针背景颜色
    private final int mDefaultTextColor;
    private TextViewStyle mStyle;// 标题样式
    private String[] mTitles;
    private final int mIndicatorHeight;// 指针高度
    private int mTabWidth; // 每个TAB的宽度
    private TabIndicatorView mIndicatorView;
    private TitleTextView[] mTitleViews;
    private final int mDividerWidth; // 分割线宽度
    private final int mDividerMargin; // 分割线上下margin
    private final int mDividerColor; // 分割线颜色

    private int mCurPage = -1; // 新页码
    private int mOldPage; // 旧页码

    public TabIndicator(Context context) {
        this(context, null);
    }

    /**
     * 构造器
     * 
     * @param context
     *            Context
     * @param attrs
     *            AttributeSet
     */
    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 垂直分布
        setOrientation(VERTICAL);

        mStyle = new TextViewStyle();

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TabIndicator, 0, 0);
        mStyle.paddingLeft = a.getDimensionPixelSize(
                R.styleable.TabIndicator_paddingLeft, 0);
        mStyle.paddingRight = a.getDimensionPixelSize(
                R.styleable.TabIndicator_paddingRight, 0);
        mStyle.paddingTop = a.getDimensionPixelSize(
                R.styleable.TabIndicator_paddingTop, 0);
        mStyle.paddingBottom = a.getDimensionPixelSize(
                R.styleable.TabIndicator_paddingBottom, 0);
        mStyle.bg = a.getResourceId(R.styleable.TabIndicator_ti_background, 0);
        mStyle.textAppereance = a.getResourceId(
                R.styleable.TabIndicator_textAppearance, 0);
        // 指针颜色，默认黑色
        mIndicatorBackColor = a.getColor(
                R.styleable.TabIndicator_indicatorBackColor, Color.GRAY);
        mIndicatorColor = a.getColor(R.styleable.TabIndicator_indicatorColor,
                Color.parseColor("#499399"));
        mDefaultTextColor = a.getColor(
                R.styleable.TabIndicator_defaultTextColor, Color.BLACK);
        // 指针高度
        mIndicatorHeight = a.getDimensionPixelSize(
                R.styleable.TabIndicator_indicatorHeight, 5);
        // 分割线宽度，默认无分割线
        mDividerWidth = a.getDimensionPixelSize(
                R.styleable.TabIndicator_dividerWidth, 0);
        mDividerMargin = a.getDimensionPixelSize(
                R.styleable.TabIndicator_dividerMargin, 10);
        mDividerColor = a.getColor(R.styleable.TabIndicator_dividerColor,
                Color.GRAY);
        a.recycle();

        mTabLayout = new LinearLayout(context);
        addView(mTabLayout, new LinearLayout.LayoutParams(WRAP_CONTENT,
                MATCH_PARENT, 1));
    }

    /**
     * 初始化
     * 
     * @param titles
     *            标题组
     */
    public void setUp(String[] titles, OnPageChangeListener onPageChangeListener) {
        setUp(titles, 0, onPageChangeListener);
    }

    /**
     * 初始化
     * 
     * @param titles
     *            标题组
     * @param initialPage
     *            起始页
     * @param onPageChangeListener
     *            监听
     */
    public void setUp(String[] titles, final int initialPage,
            OnPageChangeListener onPageChangeListener) {
        setOnPageChangeListener(onPageChangeListener);
        mTitles = titles;
        mTabLayout.setWeightSum(titles.length);
        // 先添加指针，下面响应事件要用
        mIndicatorView = new TabIndicatorView(getContext(), mIndicatorColor);
        mIndicatorView.setBackgroundColor(mIndicatorBackColor);
        mIndicatorView.setPageNum(titles.length);
        addView(mIndicatorView, new ViewGroup.LayoutParams(MATCH_PARENT,
                mIndicatorHeight));

        // 添加文字
        mTitleViews = new TitleTextView[mTitles.length];
        for (int i = 0; i < titles.length; i++) {
            addTab(i, titles[i]);
            // 添加分割线
            if (mDividerWidth > 0) {
                // 不是最后一个才添加
                if (i < titles.length - 1) {
                    addDivider();
                }
            }
        }
        requestLayout();
        post(new Runnable() {
            @Override
            public void run() {
                // 点击初始项
                mTitleViews[initialPage].performClick();
            }
        });
    }

    /**
     * 新增分割线
     */
    private void addDivider() {
        View view = new View(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(mDividerWidth,
                MATCH_PARENT);
        lp.topMargin = lp.bottomMargin = mDividerMargin;
        view.setBackgroundColor(mDividerColor);
        mTabLayout.addView(view, lp);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dividerWidth = (mTitles.length - 1) * mDividerWidth;// 分割线总宽度
        int width = MeasureSpec.getSize(widthMeasureSpec) - dividerWidth;// 控件宽度
        mTabWidth = width / mTitles.length;// 平分
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void addTab(int index, CharSequence text) {
        final TitleTextView tabView = new TitleTextView(getContext(), mStyle);
        tabView.mIndex = index;
        tabView.setOnClickListener(mTabClickListener);
        tabView.setFocusable(true);
        tabView.setText(text);
        tabView.setGravity(Gravity.CENTER);

        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0,
                MATCH_PARENT, 1));
        mTitleViews[index] = tabView;
    }

    /**
     * 标题样式
     */
    private class TextViewStyle {
        public int paddingLeft, paddingRight, paddingTop, paddingBottom,
                textAppereance;
        public int bg;
    }

    /**
     * 标题控件
     */
    private class TitleTextView extends TextView {
        private int mIndex;

        /**
         * 创建标题
         * 
         * @param context
         *            Context
         * @param style
         *            Style
         */
        public TitleTextView(Context context, TextViewStyle style) {
            super(context);
            setBackgroundResource(style.bg);
            setPadding(style.paddingLeft, style.paddingTop, style.paddingRight,
                    style.paddingBottom);
            setTextAppearance(getContext(), style.textAppereance);
        }

        public int getIndex() {
            return mIndex;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(mTabWidth, MeasureSpec.EXACTLY),
                    heightMeasureSpec);
        }
    }

    /**
     * TAB标题点击事件
     */
    private OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TitleTextView titleTextView = (TitleTextView) v;
            int index = titleTextView.getIndex();
            if (mCurPage == index && mCurPage > 0) {
                return;
            }
            mIndicatorView.scrollToPage(index);
            mOldPage = mCurPage;
            mCurPage = index;
            for (int i = 0; i < mTitleViews.length; i++) {
                mTitleViews[i].setTextColor(i == index ? mIndicatorColor
                        : mDefaultTextColor);
            }
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageChange(mOldPage, mCurPage);
            }
        }
    };

}
