//package cube.d.n.rn;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewTreeObserver;
//
///**
// * Created by Colin on 4/24/2015.
// */
//public class ButtonView extends View implements View.OnTouchListener {
//    //TODO
//    //TODO
//    SuperPrvate<Button> myB;
//
//    //TODO
//    public ButtonView(Context context) {
//        super(context);
//        init();
//    }
//
//    private void init() {
//        final ButtonView that = this;
//
//        myB= new SuperPrvate<Button>() {
//            @Override
//            public void set(Button button) {
//                value = button;
//                value.setOwner(that);
//            }
//            @Override
//            public Button get() {
//                return value;
//            }
//        };
//        // we don't want myB to be null
//        // we put text in it and then when myB is created
//        // we pass the text on
//        myB.set(new Button(0,0,10,10,"",that));
//
//        this.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        that.myB.set( new Button(0,0,getWidth(),getHeight(),that.myB.get().txt.get(),that));
//                        that.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                }
//        );
//        this.setOnTouchListener(this);
//    }
//
//    public ButtonView(Context context, AttributeSet attrs){
//        super(context,attrs);
//        init();
//
//        //This is pulled from here: http://developer.android.com/training/custom-views/create-view.html
//        TypedArray a = context.getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.ButtonView,
//                0, 0);
//
//        try {
//            if ( null != a.getString(R.styleable.ButtonView_mytext)) {
//                myB.get().txt.set(a.getString(R.styleable.ButtonView_mytext));
//            }
//        } finally {
//            // not sure why i need to recycle...
//            a.recycle();
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP){
//            if (myB.get().in(new Vector(event))){
//                myB.get().click();
//            }
//        }else{
//            if (myB.get().in(new Vector(event))){
//                myB.get().hover();
//            }
//        }
//        return true;
//    }
//
//
//    @Override
//    public void draw(Canvas canvas){
//        myB.get().draw(canvas);
//    }
//}
