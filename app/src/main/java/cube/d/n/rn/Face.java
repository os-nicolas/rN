package cube.d.n.rn;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Colin_000 on 4/18/2015.
 */
public class Face {

    final Brick owner;
    private final int color;

    public Face(Brick owner, Index faceIndex, Index ownerIndex){
        this.owner = owner;
        this.color = getColor(faceIndex, ownerIndex);
    }

    private static HashMap<ColorKey,Integer> colors = new HashMap<>();
    private static int getColor(Index faceIndex, Index ownerIndex) {

        // now we make out ColorKey
        ColorKey myKey = new ColorKey(faceIndex,ownerIndex);
        // if it's in there we are good to go
        if (colors.containsKey(myKey)){
            return colors.get(myKey);
        }
        // otherwise we need to make up a new color
        Random r = new Random();
        int myColor = 0xff000000 + r.nextInt(0x00ffffff);
        Log.i("adding",myKey+"");
        colors.put(myKey,myColor);
        return myColor;
    }

    public Index getIndex() {
        return owner.indexOf(this);
    }

    public int getColor() {
        return color;
    }

    public boolean sharesSide(Brick brick) {
        // if the owner index and bricks index are the same on everything except
        // the values where we have values
        Index myIndex = getIndex();
        Index ownersIndex = owner.getIndex();
        Index brickIndex = brick.getIndex();
        for (int i=0;i<myIndex.size();i++){
            if (myIndex.get(i)==0){
                if (!ownersIndex.get(i).equals(brickIndex.get(i))){
                    return false;
                }
            }
        }
        return true;
    }
}
