package cube.d.n.rn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Colin on 9/14/2015.
 */
public class TextFrag  extends Fragment {
    private boolean textGone = false;
    TextView tv;

    public static Fragment make(TextPageInfo imagePageInfo) {
        TextFrag result = new TextFrag();
        Bundle args = new Bundle();
        args.putInt("ID",imagePageInfo.myId);
        result.setArguments(args);
        return result;
    }

    public TextPageInfo getProblem(){
        Bundle args = getArguments();
        int id = args.getInt("ID");
        return (TextPageInfo)LayoutInfo.getPage(id);
    }

    public void onResume(){
        super.onResume();
        Log.i("resuming","textFrag "+ getProblem().text);
        if (!getProblem().getSolved()) {
            getProblem().setSolved(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Log.i("hey", "create view " + this + "");
        // The last two arguments ensure LayoutParams are inflated
        // properly.

        final View rootView = inflater.inflate(
                R.layout.text_frame, container, false);
        tv = (TextView)rootView.findViewById(R.id.text);
        tv.setText(((TextPageInfo)getProblem()).text);
        tv.setAlpha(0);
        tv.setScaleX(1.5f);
        tv.setScaleY(1.5f);
        textGone = true;
        final TextPageInfo imagePageInfo = (TextPageInfo)getProblem();
        //t.setProblem(problem);

        //t.init(problem.dim, problem.size,problem.getState());
        //final String resetTo = t.getCubeString();

        final MyViewPager mvp = ((MyViewPager)((MainActivity)getActivity()).findViewById(R.id.pager));

        rootView.findViewById(R.id.left).setVisibility(imagePageInfo.myId != 0 ? View.VISIBLE : View.INVISIBLE);
        ((Button) rootView.findViewById(R.id.left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvp.getCurrentItem() != 0) {
                    mvp.setCurrentItem(mvp.getCurrentItem() - 1, true);
                }
            }
        });

        rootView.findViewById(R.id.right).setVisibility(imagePageInfo.getSolved() ? View.VISIBLE : View.INVISIBLE);
        ((Button)rootView.findViewById(R.id.right)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mvp.getCurrentItem() < mvp.getAdapter().getCount()) {
                    mvp.setCurrentItem(mvp.getCurrentItem() + 1, true);
                }
            }
        });

        rootView.findViewById(R.id.reset).setVisibility(View.INVISIBLE);

        ((TextView)rootView.findViewById(R.id.problem_number)).setText("" + (imagePageInfo.myId));

        ((TextView)rootView.findViewById(R.id.problem_status)).setText("");

        if (!imagePageInfo.getSolved()){
            imagePageInfo.onSolved(new Runnable(){
                @Override
                public void run() {
                    ((Button)rootView.findViewById(R.id.right)).setVisibility(mvp.getCurrentItem() < mvp.getAdapter().getCount()? View.VISIBLE : View.INVISIBLE);
                }
            });
        }
        return rootView;
    }

    public void start() {
        if (textGone) {
            tv.animate().withLayer().alpha(1).scaleX(1).scaleX(1).scaleY(1).setStartDelay(0).setDuration(400).withEndAction(new Runnable() {
                @Override
                public void run() {
                    textGone = false;
                }
            }).start();
        }
    }
}
