package cn.com.nd.tabindicator.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.nd.tabindicator.R;
import cn.com.nd.tabindicator.library.TabIndicator;

public class SampleActivity extends ActionBarActivity implements
        TabIndicator.OnPageChangeListener {

    private TabIndicator mTabIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mTabIndicator = (TabIndicator) findViewById(R.id.tabIndicator);
        mTabIndicator.setUp(new String[] { "TEST", "TEST2", "TEST3" }, this);
    }

    @Override
    public void onPageChange(int oldPage, int newPage) {
        int[] color = getResources().getIntArray(R.array.color_array);
        Fragment fragment = PlaceHolderFragment.newInstance(color[newPage]);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commit();
    }

    public static class PlaceHolderFragment extends Fragment {

        private static final String PARAM_COLOR = "color";
        private int mBackGroundColor;

        public static PlaceHolderFragment newInstance(int color) {
            PlaceHolderFragment fragment = new PlaceHolderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(PARAM_COLOR, color);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            mBackGroundColor = args.getInt(PARAM_COLOR);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_place_holder, null);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.setBackgroundColor(mBackGroundColor);
        }
    }
}
