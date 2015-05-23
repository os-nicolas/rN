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
import cube.d.n.rn.Index;
import cube.d.n.rn.R;
import cube.d.n.rn.Tess;
import cube.d.n.rn.filter.ColorFilter;
import cube.d.n.rn.filter.Filter;
import cube.d.n.rn.filter.Filter1;
import cube.d.n.rn.filter.Filter2;
import cube.d.n.rn.filter.Filter3;
import cube.d.n.rn.filter.Filter4;
import cube.d.n.rn.filter.cornorFilter;


public class CubeFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(
                R.layout.cube_frag, container, false);


        final Tess tess= (Tess)rootView.findViewById(R.id.tess);
        ButtonView filterClear= (ButtonView)rootView.findViewById(R.id.filter_clear);
        filterClear.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set((Filter) null);

            }
        });
        final int end=tess.size.get()-1;
        ButtonView filter1= (ButtonView)rootView.findViewById(R.id.filter_1);
        filter1.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new cornorFilter(tess,new Index(new int[]{0, 0, 0, 0})));
            }
        });
        ButtonView filter2= (ButtonView)rootView.findViewById(R.id.filter_2);
        filter2.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new cornorFilter(tess,new Index(new int[]{end, 0, 0, 0})));
            }
        });
        ButtonView filter3= (ButtonView)rootView.findViewById(R.id.filter_3);
        filter3.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new cornorFilter(tess,new Index(new int[]{end, end, 0, 0})));
            }
        });
        ButtonView filter4= (ButtonView)rootView.findViewById(R.id.filter_4);
        filter4.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new cornorFilter(tess,new Index(new int[]{end, end, end, 0})));
            }
        });
        ButtonView filterColor= (ButtonView)rootView.findViewById(R.id.filter_color);
        filterColor.myB.get().action.set(new Action() {
            @Override
            public void act() {
                tess.filter.set(new ColorFilter(tess));
            }
        });





        return rootView;

    }
}
