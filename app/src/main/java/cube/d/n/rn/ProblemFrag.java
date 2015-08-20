package cube.d.n.rn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Colin on 8/19/2015.
 */
public class ProblemFrag extends Fragment{


    public static Fragment make(int dim, int size) {
        ProblemFrag result = new ProblemFrag();
        Bundle args = new Bundle();
        args.putInt("DIM",dim);
        args.putInt("SIZE",size);
        result.setArguments(args);
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Log.i("hey","create view "+this+"");
        // The last two arguments ensure LayoutParams are inflated
        // properly.

        Bundle args = getArguments();

        View rootView = inflater.inflate(
                R.layout.cube_frame, container, false);
        Tess t = (Tess)rootView.findViewById(R.id.cube);
        t.init(args.getInt("DIM"),args.getInt("SIZE"));



        return rootView;
    }
}
