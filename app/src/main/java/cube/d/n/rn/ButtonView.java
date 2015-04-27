package cube.d.n.rn;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Colin on 4/24/2015.
 */
public class ButtonView extends View implements View.OnTouchListener {
    //TODO
    //TODO
    Button myB;

    //TODO
    public ButtonView(Context context) {
        super(context);
        init();
    }

    private void init() {
        final ButtonView that = this;
        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        that.myB= new Button(0,0,getWidth(),getHeight(),"");
                        that.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );
        this.setOnTouchListener(this);
    }

    public ButtonView(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            if (myB.in(new Vector(event))){
                myB.click();
            }
        }else{
            if (myB.in(new Vector(event))){
                myB.hover();
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        myB.draw(canvas);
    }
}
