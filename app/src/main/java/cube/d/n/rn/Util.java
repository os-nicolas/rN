package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Shader;
import android.graphics.drawable.PictureDrawable;
import android.view.View;

/**
 * Created by Colin_000 on 4/18/2015.
 */
public class Util {

    public static void drawLine(Canvas canvas, Vector startAt, Vector vector, Paint p) {


        canvas.drawLine(startAt.x, startAt.y, vector.x,vector.y,p);
    }



    public static void drawLine(Canvas canvas, Vector startAt, Vector endAt, int color, int startWidth, int endWidth) {

        Paint p = new Paint();
        p.setColor(color);

        Path path = getPath(startAt,endAt,startWidth,endWidth);

        canvas.drawPath(path, p);
    }

    private static Path getPath(Vector startAt, Vector endAt, int startWidth, int endWidth) {
        Path path = new Path();

        float dx = endAt.x - startAt.x;
        float dy = endAt.y - startAt.y;
        float d = (float)Math.sqrt((dx*dx) + (dy*dy));

        path.moveTo(startAt.x-((dy/d)*startWidth/2f) , startAt.y+((dx/d)*startWidth/2f));
        path.lineTo(startAt.x+((dy/d)*startWidth/2f), startAt.y-((dx/d)*startWidth/2f));
        path.lineTo(endAt.x+((dy/d)*endWidth/2f), endAt.y-((dx/d)*endWidth/2f));
        path.lineTo(endAt.x - ((dy / d) * endWidth / 2f), endAt.y + ((dx / d) * endWidth / 2f));
        path.lineTo(startAt.x - ((dy / d) * startWidth / 2f), startAt.y + ((dx / d) * startWidth / 2f));
        return  path;
    }


    //Convert Picture to Bitmap
    public static Bitmap pictureDrawable2Bitmap(Picture picture) {
        PictureDrawable pd = new PictureDrawable(picture);
        Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pd.getPicture());
        return bitmap;
    }

    public static void drawShadedLine(Canvas canvas, Vector startAt, Vector endAt, int startColor,int endColor, int width) {
        drawShadedLine(canvas, startAt, endAt, startColor, endColor, width, width);
    }

    public static void drawShadedLine(Canvas canvas, Vector startAt, Vector endAt, int startColor,int endColor, int startWidth, int endWidth) {
        Shader shader = new LinearGradient(startAt.x, startAt.y, endAt.x, endAt.y, startColor,  endColor, Shader.TileMode.MIRROR);

        Paint p = new Paint();
        p.setShader(shader);

        Path path = getPath(startAt,endAt,startWidth,endWidth);

        canvas.drawPath(path, p);
    }

    public static int getColor(View owner, Vector myLoc, int alpha) {
        float width = owner.getWidth();
        float hieght = owner.getHeight();

        return  (0x01000000*alpha) +
                ((int)(0xff*(myLoc.x/width))*0x10000) +
                ((int)(0xff*(myLoc.y/hieght))*0x100);
    }

    public static int getColor(View owner, Vector vector) {
        return getColor(owner,vector,0xff);
    }

    public static Vector moveTowards(Vector start, Vector end, float radius) {

        float dx = end.x - start.x;
        float dy = end.y - start.y;
        float d = (float)Math.sqrt((dx*dx) + (dy*dy));

        Vector toAdd = new Vector((dx*radius)/d, (dy*radius)/d);
        Vector res = start.add(toAdd,true);
        return res;
    }

    public static void drawCircle(Canvas canvas, Vector startAt, int i, Paint p) {
        canvas.drawCircle(startAt.x,startAt.y,i,p);
    }

    public static boolean hasAtleastOneEdge(Tess t,Index at) {
        for (int i=0;i<at.size();i++){
            if (at.get(i)==0 || at.get(i)== t.size.get()-1){
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEdgeExcept(Tess tess, int at, Index index) {
        for (int i=0;i<index.size();i++){
            if (i != at) {
                if (index.get(i) == 0 || index.get(i) == tess.size.get() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean pointsAtAnyThing(Tess tess, Index index, int at) {
        return Util.isNotEdgeExcept(tess,at,index);// && (index.get(at) == 0 || index.get(at)== tess.size.get()-1);
    }

    public static boolean isEdge(Tess tess,Brick b) {

        Index index = b.getIndex();
        int count = 0;
        for (int i=0;i<index.size();i++){
                if (index.get(i) == 0 || index.get(i) == tess.size.get() - 1) {
                    count++;
                    if (count== 2) {
                        return true;
                    }
                }
        }
        return false;
    }
}
