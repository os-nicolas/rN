package cube.d.n.rn.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import cube.d.n.rn.Activities.MyAdapter;
import cube.d.n.rn.R;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class Holder extends FragmentActivity {

    ViewPager mViewPager;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder);

//        View decorView = getWindow().getDecorView();
//// Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//// Remember that you should never show the action bar if the
//// status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.

        mViewPager = (ViewPager) findViewById(R.id.pager);

        if (mViewPager.getAdapter()==null) {
            MyAdapter adapter =
                    new MyAdapter(
                            getSupportFragmentManager());
            mViewPager.setAdapter(adapter);

            final Context that = this;
        }
    }
}
