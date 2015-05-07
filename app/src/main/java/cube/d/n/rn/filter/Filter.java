package cube.d.n.rn.filter;

import cube.d.n.rn.Brick;
import cube.d.n.rn.Face;
import cube.d.n.rn.Index;
import cube.d.n.rn.Tess;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public abstract class Filter {
    final public Tess owner;

    public Filter(Tess owner){
        this.owner = owner;
    }

    /**
     *
     * @param brick
     * @param f
     * @return true if the brick face combo should not be drawn
     */
    public abstract boolean filter(Brick brick, Face f);

    public boolean drakLine(Index index, Index indexTo) {
        return false;
    }
}
