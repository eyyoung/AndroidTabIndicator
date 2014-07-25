AndroidTabIndicator
===================

用于非ViewPager情况下Tab切换标题自定义控件

ViewPager情况使用ViewPagerIndicator

效果图：

![Renderings](https://github.com/eyyoung/AndroidTabIndicator/blob/master/sample.gif)  

使用方法：

```xml
 <cn.com.nd.tabindicator.library.TabIndicator xmlns:indicator="http://schemas.android.com/apk/res-auto"
        android:id="@+id/tabIndicator"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        indicator:indicatorHeight="4dp"
        indicator:dividerWidth="1dp"
        indicator:dividerMargin="5dp"
        indicator:defaultTextColor="@color/color_3"
        indicator:dividerColor="@android:color/holo_red_light" />
