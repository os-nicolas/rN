package cube.d.n.rn.filter;

import android.util.Log;

import cube.d.n.rn.Brick;
import cube.d.n.rn.Face;
import cube.d.n.rn.Index;
import cube.d.n.rn.Tess;
import cube.d.n.rn.Util;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public class cornorFilter extends Filter {

    Index[] goods = new Index[3];
    Index[] bads = new Index[5];
    Index[] all = new Index[8];

    public cornorFilter(Tess owner,Index startIndex) {
        super(owner);
        int end=owner.size.get()-1;
        all[0]= new Index(new int[]{end, 0, 0, end});
        all[1]= new Index(new int[]{end, 0, end, end});
        all[2]= new Index(new int[]{end, end, 0, end});
        all[3]= new Index(new int[]{0, 0, end, 0});
        all[4]= new Index(new int[]{0, end, 0, 0});
        all[5]= new Index(new int[]{0, end, end, 0});
        all[6]= new Index(new int[]{0, end, 0, end});
        all[7]= new Index(new int[]{end, 0, end, 0});

        int dim1= Integer.MAX_VALUE;
        int dim1value= 0;
        int dim2 =Integer.MAX_VALUE;
        int dim2value= 0;
        for (int at=0;at<startIndex.size();at++){
            Index tempIndex = new Index(startIndex);
            // modify tempIndex
            if (tempIndex.get(at) ==0){
                tempIndex.set(at,end);
            }else if (tempIndex.get(at) ==end){
                tempIndex.set(at,0);
            }
            if (Util.in(all, tempIndex)){
                if (dim1 ==Integer.MAX_VALUE){
                    dim1 = at;
                    dim1value = tempIndex.get(at);
                }else if (dim2 ==Integer.MAX_VALUE){
                    dim2 = at;
                    dim2value = tempIndex.get(at);
                    break;
                }
            }
        }
        // now we add our three good
        Index g1 = new Index(startIndex);
        g1.set(dim1,dim1value);
        goods[0]= g1;
        Index g2 = new Index(startIndex);
        g2.set(dim2,dim2value);
        goods[1]= g2;
        Index g3 = new Index(startIndex);
        g3.set(dim1,dim1value);
        g3.set(dim2,dim2value);
        goods[2]= g3;

        // and our bads are ones not in goods
        int at =0;
        Log.d("conrnorFilter", "looking at: "+ startIndex);
        for (int i=0;i<all.length;i++){
            if (!Util.in(goods, all[i])){
                bads[at] = all[i];
                at++;
            }
        }


    }


    @Override
    public boolean filter(Brick brick, Face f) {


        if ((owner.bricks.get(goods[0]).sharesSpecificFace(brick, f) ||
                owner.bricks.get(goods[1]).sharesSpecificFace(brick, f) ||
                owner.bricks.get(goods[2]).sharesSpecificFace(brick, f)) && (
                !owner.bricks.get(bads[0]).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(bads[1]).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(bads[2]).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(bads[3]).sharesSpecificFace(brick, f) &&
                        !owner.bricks.get(bads[4]).sharesSpecificFace(brick, f)
        )) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean drakLine(Index index, Index indexTo) {
        return !(Util.in(bads, index) || Util.in(bads, indexTo));
    }
}
