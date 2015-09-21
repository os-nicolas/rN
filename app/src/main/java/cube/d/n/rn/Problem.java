package cube.d.n.rn;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Colin on 8/20/2015.
 */
public class Problem extends LayoutInfo{


    final String startState;
    final int dim;
    final int size;
    final boolean solved;

    public static Problem make(String startState, int dim,int size,boolean solved){
        Problem res = new Problem(startState,size,dim,solved);
        probs.put(res.myId,res);
        return res;
    }


    public boolean getSolved(){
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(myId + "_solved", solved);
    }


    private Problem(String startState, int size,int dim,boolean solved){
        this.startState = startState;
        this.solved = solved;
        this.dim = dim;
        this.size = size;
    }

    public void updateLastState(String cubeString) {
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(myId + "_state", cubeString);
        editor.commit();
    }

    public String getState(){
        SharedPreferences settings = RN.rn().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(myId + "_state", startState);
    }

    public String getStartState(){
        return startState;
    }
}
