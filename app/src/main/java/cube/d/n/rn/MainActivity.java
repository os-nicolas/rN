package cube.d.n.rn;


import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Tess myView = new Tess(this,3,3);

        setContentView(R.layout.activity_main);//R.layout.activity_main

       ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);

        Log.d("margin",mViewPager.getPageMargin()+"");
        mViewPager.setPageMargin(0);


        //this looks like it will soon be depricated and replaced with Add
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                RN.rn().setLast(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

        }
        });


        if (mViewPager.getAdapter() == null) {
            MainAdapter adapter =
                    new MainAdapter(
                            getSupportFragmentManager(),this);
            mViewPager.setAdapter(adapter);

        }
        mViewPager.setCurrentItem(RN.rn().getLast(), true);


    }

    @Override
    public void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        //ActionBar();
        if (actionBar!= null) {
            actionBar.hide();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
