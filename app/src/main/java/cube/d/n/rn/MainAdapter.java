package cube.d.n.rn;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Colin on 8/19/2015.
 */
public class MainAdapter extends FragmentPagerAdapter {

    public ArrayList<Fragment> frags = new ArrayList<>();

    public MainAdapter(FragmentManager fm, final Activity activity) {
        super(fm);



        Problem last = null;
        final MainAdapter that = this;

        for (Problem problem: RN.rn().problems){
            if (problem.unlocked()) {
                frags.add(ProblemFrag.make(problem));
            }else if (last != null){
                final Problem rProblem = problem;
                last.onSolved(new Runnable(){
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                frags.add(ProblemFrag.make(rProblem));
                                that.notifyDataSetChanged();
                            }
                        });
                    }
                });

            }
            last = problem;
        }


//
//        frags.add(ProblemFrag.make(2, 2));
//        frags.add(ProblemFrag.make(3, 2));
//        frags.add(ProblemFrag.make(3,3));
//        frags.add(ProblemFrag.make(4, 2));
//        frags.add(ProblemFrag.make(3, 4));
//        frags.add(ProblemFrag.make(4, 3));
    }

    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return frags.size();
    }
}
