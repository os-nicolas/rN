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
    public TextFrag textFrag;

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        if (textFrag != null) {
            textFrag.start();
        }
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (getItem(position) instanceof TextFrag) {
            textFrag = (TextFrag) getItem(position);
        } else {
            textFrag = null;
        }
    }

    public MainAdapter(FragmentManager fm, final Activity activity) {
        super(fm);


        LayoutInfo last = null;
        final MainAdapter that = this;
        boolean solvedZone = true;


        for (LayoutInfo problem : RN.rn().problems) {
            if (solvedZone && problem.unlocked()) {

                if (problem instanceof Problem) {
                    frags.add(ProblemFrag.make((Problem) problem));
                } else if (problem instanceof ImagePageInfo) {
                    frags.add(ImageFrag.make((ImagePageInfo) problem));
                } else {
                    frags.add(TextFrag.make((TextPageInfo) problem));
                }
            } else {
                if (solvedZone) {
                    solvedZone = false;
                }

                if (last != null) {
                    final LayoutInfo rProblem = problem;
                    last.onSolved(new Runnable() {
                        @Override
                        public void run() {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (rProblem instanceof Problem) {
                                        frags.add(ProblemFrag.make((Problem) rProblem));
                                    } else if (rProblem instanceof ImagePageInfo) {
                                        frags.add(ImageFrag.make((ImagePageInfo) rProblem));
                                    } else {
                                        frags.add(TextFrag.make((TextPageInfo) rProblem));
                                    }
                                    that.notifyDataSetChanged();
                                }
                            });
                        }
                    });

                }

            }
            last = problem;
        }
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
