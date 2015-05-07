package cube.d.n.rn.Activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cube.d.n.rn.Action;
import cube.d.n.rn.ButtonView;
import cube.d.n.rn.R;
import cube.d.n.rn.Tess;
import cube.d.n.rn.filter.Filter;
import cube.d.n.rn.filter.Filter1;
import cube.d.n.rn.filter.Filter2;
import cube.d.n.rn.filter.Filter3;
import cube.d.n.rn.filter.Filter4;


public class CubeFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(
                R.layout.cube_frag, container, false);


        final Tess tess= (Tess)rootView.findViewById(R.id.tess);
        ButtonView filterClear= (ButtonView)rootView.findViewById(R.id.filter_clear);
        Log.i("set action for",filterClear.myB.get()+"");
        filterClear.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set((Filter) null);

            }
        });
        Log.i("set action for",filterClear.myB.get()+"");
        ButtonView filter1= (ButtonView)rootView.findViewById(R.id.filter_1);
        filter1.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new Filter1(tess));
            }
        });
        Log.i("set action for",filterClear.myB.get()+"");
        ButtonView filter2= (ButtonView)rootView.findViewById(R.id.filter_2);
        filter2.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new Filter2(tess));
            }
        });
        Log.i("set action for",filterClear.myB.get()+"");
        ButtonView filter3= (ButtonView)rootView.findViewById(R.id.filter_3);
        filter3.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new Filter3(tess));
            }
        });
        Log.i("set action for",filterClear.myB.get()+"");
        ButtonView filter4= (ButtonView)rootView.findViewById(R.id.filter_4);
        filter4.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new Filter4(tess));
            }
        });




        return rootView;

    }
}
