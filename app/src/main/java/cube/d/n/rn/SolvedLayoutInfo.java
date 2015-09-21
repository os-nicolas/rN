package cube.d.n.rn;

import android.content.SharedPreferences;

/**
 * Created by Colin on 9/16/2015.
 */
public class SolvedLayoutInfo extends LayoutInfo {


    boolean firstTime = true;

    @Override
    public boolean getSolved(){
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        if (firstTime){
            firstTime = false;
            for(Runnable r: runOnSolved){
                r.run();
            }
        }
        return settings.getBoolean(myId + "_solved", true);
    }
}
