package cube.d.n.rn.filter;

import cube.d.n.rn.Brick;
import cube.d.n.rn.Face;
import cube.d.n.rn.Index;
import cube.d.n.rn.Tess;
import cube.d.n.rn.Util;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class Filter1  extends Filter{

    public Filter1(Tess owner){
        super(owner);
    }

    @Override
    public boolean filter(Brick brick, Face f) {
        Index i1 = new Index(new int[]{1, 0, 0, 1});
        Index i2 = new Index(new int[]{1, 0, 1, 1});
        Index i3 = new Index(new int[]{1, 1, 0, 1});

        Index b1 = new Index(new int[]{0, 0, 1, 0});
        Index b2 = new Index(new int[]{0, 1, 0, 0});
        Index b3 = new Index(new int[]{0, 1, 1, 0});
        Index b4 = new Index(new int[]{0, 1, 0, 1});
        Index b5 = new Index(new int[]{1, 0, 1, 0});

        if ((owner.bricks.get(i1).sharesSpecificFace(brick, f) ||
                owner.bricks.get(i2).sharesSpecificFace(brick, f) ||
                owner.bricks.get(i3).sharesSpecificFace(brick, f)) && (
                !owner.bricks.get(b1).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(b2).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(b3).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(b4).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(b5).sharesSpecificFace(brick, f)
        )) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean drakLine(Index index, Index indexTo) {
        Index[] bads = new Index[]{new Index(new int[]{0, 0, 1, 0}),
                new Index(new int[]{0, 1, 0, 0}),
                new Index(new int[]{0, 1, 1, 0}),
                new Index(new int[]{0, 1, 0, 1}),
                new Index(new int[]{1, 0, 1, 0})};


        return !(Util.in(bads, index) || Util.in(bads, indexTo));
    }
}
