package cube.d.n.rn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Colin on 8/19/2015.
 */
public class ProblemFrag extends Fragment{


//    public static Fragment make(int dim, int size) {
//        ProblemFrag result = new ProblemFrag();
//        Bundle args = new Bundle();
//        args.putInt("DIM",dim);
//        args.putInt("SIZE",size);
//        result.setArguments(args);
//        return result;
//    }

    public static Fragment make(Problem problem) {
        ProblemFrag result = new ProblemFrag();
        Bundle args = new Bundle();
        args.putInt("ID",problem.myId);
        result.setArguments(args);
        return result;
    }

    public Problem getProblem(){
        Bundle args = getArguments();
        int id = args.getInt("ID");
        return Problem.getProblem(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Log.i("hey","create view "+this+"");
        // The last two arguments ensure LayoutParams are inflated
        // properly.

        View rootView = inflater.inflate(
                R.layout.cube_frame, container, false);
        final Tess t = (Tess)rootView.findViewById(R.id.cube);
        final Problem problem = getProblem();

        t.init(problem.dim, problem.size,problem.startState);
        final String resetTo = t.getCubeString();

        final MyViewPager mvp = ((MyViewPager)((MainActivity)getActivity()).findViewById(R.id.pager));


        ((Button) rootView.findViewById(R.id.left)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mvp.getCurrentItem() != 0) {
                    mvp.setCurrentItem(mvp.getCurrentItem() - 1, true);
                }
            }
        });

        ((Button)rootView.findViewById(R.id.right)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mvp.getCurrentItem() < mvp.getChildCount()) {
                    mvp.setCurrentItem(mvp.getCurrentItem() + 1, true);
                }
            }
        });

        rootView.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("resetting from", t.getCubeString());
                t.resetTo(resetTo);
            }
        });

        return rootView;
    }


}
