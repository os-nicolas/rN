package cube.d.n.rn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

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

    public LayoutInfo getProblem(){
        Bundle args = getArguments();
        int id = args.getInt("ID");
        return LayoutInfo.getPage(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Log.i("hey","create view "+this+"");
        // The last two arguments ensure LayoutParams are inflated
        // properly.

        final View rootView = inflater.inflate(
                R.layout.cube_frame, container, false);
        final Tess t = (Tess)rootView.findViewById(R.id.cube);
        final Problem problem = (Problem)getProblem();
        t.setProblem(problem);

        Log.d("loading problem","saved state: " + problem.getState());

        t.init(problem.dim, problem.size,problem.getState());
        final String resetTo = problem.getStartState();

        final MyViewPager mvp = ((MyViewPager)((MainActivity)getActivity()).findViewById(R.id.pager));

        rootView.findViewById(R.id.left).setVisibility(problem.myId != 0 ? View.VISIBLE : View.INVISIBLE);
        ((Button) rootView.findViewById(R.id.left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvp.getCurrentItem() != 0) {
                    mvp.setCurrentItem(mvp.getCurrentItem() - 1, true);
                }
            }
        });

        rootView.findViewById(R.id.right).setVisibility(problem.getSolved() ? View.VISIBLE : View.INVISIBLE);
        ((Button)rootView.findViewById(R.id.right)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mvp.getCurrentItem() < mvp.getAdapter().getCount()) {
                    mvp.setCurrentItem(mvp.getCurrentItem() + 1, true);
                }
            }
        });

        final WeakReference<Tess> tessWeakReference = new WeakReference<Tess>(t);
        rootView.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {

            boolean canAct=true;
            @Override
            public void onClick(View v) {
                if (canAct) {
                    canAct = false;
                    Tess myt = tessWeakReference.get();
                    if (myt != null) {
                        Log.i("resetting from", t.getCubeString());
                        Log.i("resetting to", resetTo);
                        t.animate().withLayer().alpha(0).scaleX(1.2f).scaleY(1.2f).setDuration(400).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                t.resetTo(resetTo);
                                t.animate().withLayer().setStartDelay(200).setDuration(400).alpha(1).scaleX(1f).scaleY(1f).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        canAct = true;
                                    }
                                }).start();
                            }
                        }).start();
                    }
                }
            }
        });

        ((TextView)rootView.findViewById(R.id.problem_number)).setText("" + (problem.myId));

        if (problem.getSolved()) {
            if (problem.solved){
                ((TextView) rootView.findViewById(R.id.problem_status)).setText("");

            }else{
            ((TextView) rootView.findViewById(R.id.problem_status)).setText("Solved");
            }
        }else{
            problem.onSolved(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) rootView.findViewById(R.id.problem_status)).animate().withLayer().alpha(0).scaleX(1.5f).scaleY(1.5f).setDuration(100).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) rootView.findViewById(R.id.problem_status)).setText("Solved");
                                    ((TextView) rootView.findViewById(R.id.problem_status)).animate().withLayer().alpha(1).scaleX(1).scaleY(1).setDuration(600).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((Button) rootView.findViewById(R.id.right)).setVisibility(mvp.getCurrentItem() < mvp.getAdapter().getCount() ? View.VISIBLE : View.INVISIBLE);
                                            mvp.setCurrentItem(mvp.getCurrentItem() + 1, true);
                                        }
                                    }).start();
                                }
                            }).start();
                        }
                    });
                }
            });
            ((TextView) rootView.findViewById(R.id.problem_status)).setText("");
        }
        //if (!problem.getSolved()){

//
//
//                    Thread th = new Thread(new Runnable(){
//                        @Override
//                        public void run() {
//                            String solved ="SOLVED";
//                            String current = "";
//                            for (int i=0;i<solved.length();i++){
//                                try {
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                current+=solved.charAt(i);
//                                final String c = current;
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ((TextView) rootView.findViewById(R.id.problem_status)).setText(c);
//                                    }
//                                });
//                            }
//                            try {
//                                Thread.sleep(400);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mvp.setCurrentItem(mvp.getCurrentItem() + 1, true);
//                                    ((Button)rootView.findViewById(R.id.right)).setVisibility(mvp.getCurrentItem() < mvp.getAdapter().getCount()? View.VISIBLE : View.INVISIBLE);
//                                }
//                            });
//
//                        }
//                    });
//                    th.start();


//                }
//            });
//        }

        return rootView;
    }
}
