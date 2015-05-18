package cube.d.n.rn.filter;

import cube.d.n.rn.Brick;
import cube.d.n.rn.Face;
import cube.d.n.rn.GS;
import cube.d.n.rn.Index;
import cube.d.n.rn.SpinTo;
import cube.d.n.rn.Tess;

/**
 * Created by Colin_000 on 5/16/2015.
 */
public class ColorFilter extends Filter {


    public final GS<Integer> color = new GS<>(0xffffffff);

    public ColorFilter(Tess owner) {
        super(owner);
        color.set((Integer) Face.colors.values().toArray()[0]);
    }

    @Override
    public boolean filter(Brick brick, Face f) {
        return !( f.getColor()+0 ==  color.get()+0);
    }

    @Override
    public boolean drakLine(Index index, Index indexTo) {
        return true;
    }

    @Override
    public boolean filter(SpinTo st){
        return false;
    }
}
