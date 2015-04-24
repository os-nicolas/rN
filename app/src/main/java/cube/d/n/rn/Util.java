package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;

/**
 * Created by Colin_000 on 4/18/2015.
 */
public class Util {

    public static void drawLine(Canvas canvas, Vector startAt, Vector vector, Paint p) {
        canvas.drawLine(startAt.x, startAt.y, vector.x,vector.y,p);
    }

    //Convert Picture to Bitmap
    public static Bitmap pictureDrawable2Bitmap(Picture picture) {
        PictureDrawable pd = new PictureDrawable(picture);
        Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pd.getPicture());
        return bitmap;
    }
}
