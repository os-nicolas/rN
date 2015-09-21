package cube.d.n.rn;

import android.content.SharedPreferences;

/**
 * Created by Colin on 9/14/2015.
 */
public class TextPageInfo extends SolvedLayoutInfo {

    String text;

    public TextPageInfo(String text) {
        this.text = text;
    }
    public static TextPageInfo make(String src){
        TextPageInfo res = new TextPageInfo(src);
        probs.put(res.myId,res);
        return res;
    }

}
