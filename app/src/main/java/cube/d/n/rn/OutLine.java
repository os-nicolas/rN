package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Shader;

/**
 * Created by Colin on 8/14/2015.
 */
public class OutLine extends BitmapBacked {

    final private Tess tess;
    final private  float radius;

    public OutLine(Tess tess,float radius){
        super(tess);
        this.tess = tess;
        this.radius = radius;
    }

    @Override
    protected Bitmap updateBitmap() {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int) (2 * radius), (int) (2 * radius));

        Vector startAt = new Vector(radius, radius);

        Paint grey = new Paint();
        grey.setColor(0x88ffffff);
        grey.setStrokeWidth(2);


        for (Brick b : tess.bricks.values()) {
            Index index = b.getIndex();
            for (int at = 0; at < index.size(); at++) {
                if (Util.pointsAtAnyThing(tess,index,at) && index.get(at) < tess.size.get()-1) {

                    Index indexTo = new Index(index);
                    indexTo.set(at, indexTo.get(at)+1);

                    Vector start = index.getVector(tess);

                    Vector end = indexTo.getVector(tess);

                    Util.drawShadedLine(
                            canvas,
                            Util.moveTowards(start, end, ((Tess) owner).radius),
                            Util.moveTowards(end, start, ((Tess) owner).radius),
                            Util.getColor(owner, index.getVector((Tess) owner)),
                            Util.getColor(owner, indexTo.getVector((Tess) owner)),
                            10);

                    Util.drawShadedLine(
                            canvas,
                            start,
                            end,
                            Util.getColor(owner, index.getVector((Tess) owner),0x60),
                            Util.getColor(owner, indexTo.getVector((Tess) owner),0x60),
                            10);
                }
            }
        }


        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture);
    }
}
