package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.Log;

import cube.d.n.rn.filter.cornorFilter;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public class CornoreFilterButton extends OnFilterButton {
    public CornoreFilterButton(Tess tess, Index current) {
        super(tess,new cornorFilter(tess,current), getVector(tess,current));
    }
    public static Vector getVector(Tess tess, Index current) {
        Index cpy = new Index(current);

        Vector at = new Vector(tess.getStartAt());
        int end = tess.size.get()-1;
        for (int i=0;i< cpy.size();i++){
            Vector vec=tess.vectorForDimension(i);

            if (cpy.get(i) == 0){
                vec.scale(-.25f,false);
            }else if(cpy.get(i) == end){
                vec.scale(end+.25f,false);
            }else{
                Log.e("getVector", "bad");
            }
            at.add(vec,false);
        }
        return at;
    }
    @Override
    protected Bitmap updateBitmap(Bitmap old) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int)(2*radius), (int)(2*radius));

        drawBkg(canvas);

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture,old);
    }
}
