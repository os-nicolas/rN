package cube.d.n.rn.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cube.d.n.rn.R;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class HelpFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(
                R.layout.help_frag, container, false);
        return rootView;

    }

}
