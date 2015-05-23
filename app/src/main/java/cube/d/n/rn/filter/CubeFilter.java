package cube.d.n.rn.filter;

import cube.d.n.rn.Brick;
import cube.d.n.rn.Face;
import cube.d.n.rn.FaceIndex;
import cube.d.n.rn.Index;
import cube.d.n.rn.Tess;
import cube.d.n.rn.Util;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public class CubeFilter extends Filter {

    private final int dim;
    private final int value;

    public CubeFilter(Tess owner,int dim,int value) {
        super(owner);
        this.dim = dim;
        this.value = value;
    }
    @Override
    public boolean filter(Brick brick, Face f) {
        if (brick.getIndex().get(dim)==value){
            if (f.getIndex().get(dim)== FaceIndex.FaceValue.NONE){
                return false;
            }
        }

        return true;

    }

    @Override
    public boolean drakLine(Index index, Index indexTo) {
        return index.get(dim)==value && indexTo.get(dim)==value ;
    }
}
