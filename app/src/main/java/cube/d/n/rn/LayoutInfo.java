package cube.d.n.rn;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Colin on 8/24/2015.
 */
public abstract class LayoutInfo {
    final protected static HashMap<Integer,LayoutInfo> probs = new HashMap<>();
    private static  int count=0;
    final int myId = count++;

    protected static final String PREFS_NAME = "PAGE";

    ArrayList<Runnable> runOnSolved = new ArrayList<>();
    public void onSolved(Runnable runnable) {
        runOnSolved.add(runnable);
    }

    public boolean getSolved(){
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(myId + "_solved", false);
    }

    public void setSolved(boolean solved){
        if (!getSolved() && solved){
            for(Runnable r: runOnSolved){
                r.run();
            }
        }
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(myId+"_solved", solved);
        editor.commit();
    }

    public boolean unlocked() {
        if (myId == 0){
            return true;
        }else{
            LayoutInfo before = getPage(myId-1);
            return before.getSolved() || getSolved();
        }
    }

    public static LayoutInfo getPage(int id){
        return probs.get(id);
    }
}
