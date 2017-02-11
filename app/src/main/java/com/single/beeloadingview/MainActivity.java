package com.single.beeloadingview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.library.beeloadingview.BeeLoadingView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
        Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
        Bitmap b4 = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
        Bitmap b5 = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
        Bitmap b6 = BitmapFactory.decodeResource(getResources(), R.drawable.img6);
        Bitmap b7 = BitmapFactory.decodeResource(getResources(), R.drawable.img7);
        ((BeeLoadingView) findViewById(R.id.bl11)).setBitmaps(new Bitmap[]{b1, b2, b3, b4, b5, b6, b7});
        ((BeeLoadingView) findViewById(R.id.bl22)).setBitmaps(new Bitmap[]{b1, b2, b3, b4, b5, b6, b7});
        ((BeeLoadingView) findViewById(R.id.bl33)).setBitmaps(new Bitmap[]{b1, b2, b3, b4, b5, b6, b7});
    }
}
