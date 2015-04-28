package cube.d.n.rn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Colin on 4/24/2015.
 */
public class ButtonView extends View implements View.OnTouchListener {
    //TODO
    //TODO
    SuperPrvate<Button> myB;

    //TODO
    public ButtonView(Context context) {
        super(context);
        init();
    }

    private void init() {
        final ButtonView that = this;

        myB= new SuperPrvate<Button>() {
            @Override
            public void set(Button button) {
                value = button;
                value.setOwner(that);
            }
            @Override
            public Button get() {
                return value;
            }
        };

        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        that.myB.set( new Button(0,0,getWidth(),getHeight(),"",that));
                        that.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );
        this.setOnTouchListener(this);
    }

    public ButtonView(Context context, AttributeSet attrs){
        super(context,attrs);
        init();

        TypedArray a=getContext().obtainStyledAttributes(
                attrs,
                R.styleable.ButtonView);


        Log.i("test", a.getString(
                R.styleable.ButtonView_android_text));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            if (myB.get().in(new Vector(event))){
                myB.get().click();
            }
        }else{
            if (myB.get().in(new Vector(event))){
                myB.get().hover();
            }
        }
        return true;
    }


    @Override
    public void draw(Canvas canvas){
        myB.get().draw(canvas);
    }
}
