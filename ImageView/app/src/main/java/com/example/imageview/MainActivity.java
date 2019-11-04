package com.example.imageview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.Arrays;

import static android.graphics.Color.RGBToHSV;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button buttonChoose;
    Button buttonApply;
    Uri imageURI;
    SeekBar seekBar;
    Bitmap bmp_Copy;
    Bitmap bmp_Original;
    int[] histogram;
    boolean choiceDone = false;
    private static final int PICK_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //main image
        imageView = (ImageView)findViewById((R.id.imageView2));

        //button to choose an image
        buttonChoose = (Button)findViewById(R.id.buttonChoose);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //button to set the image in grey
        buttonApply = (Button)findViewById(R.id.buttonApply);

        //button to return to the original image
        Button buttonToOriginal = (Button)findViewById(R.id.buttonToOriginal);
        buttonToOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choiceDone) {
                    bmp_Copy = bmp_Original.copy(Bitmap.Config.ARGB_8888, true);
                    imageView.setImageBitmap(bmp_Copy);
                    seekBar.setProgress(0);
                }
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar );
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // When Progress value changed.
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                if(choiceDone) {
                    int i = seekBar.getProgress();
                    bmp_Copy = bmp_Original.copy(Bitmap.Config.ARGB_8888, true);
                    increaseLum(bmp_Copy, i);
                    imageView.setImageBitmap(bmp_Copy);
                }
            }

            // Notification that the user has started a touch gesture.
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final Spinner spinnerRegion = (Spinner) findViewById(R.id.spinner);
        String[] lRegion={"to grey","red filter", "blue filter", "green filter","red & green", "red & blue", "blue & green", "weird", "grey keep red", "grey keep green", "grey keep blue", "Dynamic linear Extension "};
        ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,lRegion);
        dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(dataAdapterR);
        //-- gestion du Click sur la liste Région
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if( id == 0){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                toGrey(bmp_Copy);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 1){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                simpleFilter(bmp_Copy, -1,0,0);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 2){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                simpleFilter(bmp_Copy, 0,0,-1);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 3){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                simpleFilter(bmp_Copy, 0,-1,0);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 4){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                simpleFilter(bmp_Copy, -1,-1,0);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 5){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                simpleFilter(bmp_Copy, -1,0,-1);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 6){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                simpleFilter(bmp_Copy, 0,-1,-1);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 7){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                weirdFiler1(bmp_Copy);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }
                else if(id == 8){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                greyKeepColor(bmp_Copy,0);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });

                }
                else if(id == 9){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                greyKeepColor(bmp_Copy,120);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });

                }
                else if(id == 10){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                greyKeepColor(bmp_Copy,240);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }

                else if(id == 11){
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(choiceDone){
                                DynamicLinearExtension(bmp_Copy);
                                imageView.setImageBitmap(bmp_Copy);
                            }
                        }
                    });
                }

                ; }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });
    }
    public double getHue(int r, int g, int b){
        double r_ = r/255.0;
        double b_ = b/255.0;
        double g_ = g/255.0;
        double cMax = Math.max(r_, Math.max(b_, g_));
        double cMin = Math.min(r_, Math.min(b_, g_));
        double delta = cMax - cMin;
        //Log.i("gigantesque", ("getHue: " + r/255.0 +" " + g_ +" " + b_ + " : " + cMax));
        if(delta <= 0.0001)
            return 0;
        else if(cMax == r_){
            return 60 * (((g_ - b_)/delta)%6);
        }
        else if(cMax == g_){
            return 60 * (((b_ - r_)/delta)+2);
        }
        return 60 * (((b_ - g_)/delta)+4);
    }
    public void simpleFilter(Bitmap img, int r,int g,int b){
        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
        for(int i = 0; i<pixels.length;i++){
            pixels[i] = rgb( r == -1 ? red(pixels[i]) : r,
                     g == -1 ? green(pixels[i]) : g,
                    b == -1 ? blue(pixels[i]) : b
                    );
        }
        img.setPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
    }

    public void toGrey(Bitmap img){
        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
        for(int i = 0; i<pixels.length;i++){
            int newColor = (int)Math.round(0.3*red(pixels[i]) + 0.59*green(pixels[i]) + 0.11*blue(pixels[i]));
            pixels[i] = rgb(newColor, newColor, newColor);
            //pixels[i] = rgb(red(pixels[i]),0,0);
            //pixels[i] = rgb(0,green(pixels[i]),blue(pixels[i]));
            //getHue(red(pixels[i]), green(pixels[i]), blue(pixels[i]));
        }
        img.setPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
        increaseLum(img, 60);
    }

    private void getHistrogram(Bitmap img){
        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
        histogram = new int [256];
        Arrays.fill(histogram, 0);
        for (int pixel : pixels) {
            histogram[(int)Math.round(0.3*red(pixel) + 0.59*green(pixel) + 0.11*blue(pixel))]++;
        }
        //(int i = 0; i<pixels.length;i++){
    }

    public void weirdFiler1(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width*height];
        img.getPixels(pixels, 0, width, 0,0,width, height);
        int[] pixelsBlur = pixels.clone();
        for(int i = width; i<pixels.length - width;i++){
            if (i%width == 0){
                int red = (red(pixels[i]) + red(pixels[i+1]) + red(pixels[i+1-width]) + red(pixels[i-width]) + red(pixels[i+width]) + red(pixels[i+1+width]))/6;
                int blue = (blue(pixels[i]) + blue(pixels[i+1]) + blue(pixels[i+1-width]) + blue(pixels[i-width]) + blue(pixels[i+width]) + blue(pixels[i+1+width]))/6;
                int green = (green(pixels[i]) + green(pixels[i+1]) + green(pixels[i+1-width]) + green(pixels[i-width]) + green(pixels[i+width]) + green(pixels[i+1+width]))/6;
                pixelsBlur[i] = rgb(red, green, blue);
            }
            else if (i%width == width-1){
                int red = (red(pixels[i]) + red(pixels[i-1]) + red(pixels[i-1-width]) + red(pixels[i-width]) + red(pixels[i+width]) + red(pixels[i-1+width]))/6;
                int blue = (blue(pixels[i]) + blue(pixels[i-1]) + blue(pixels[i-1-width]) + blue(pixels[i-width]) + blue(pixels[i+width]) + blue(pixels[i-1+width]))/6;
                int green = (green(pixels[i]) + green(pixels[i-1]) + green(pixels[i-1-width]) + green(pixels[i-width]) + green(pixels[i+width]) + green(pixels[i-1+width]))/6;
                pixelsBlur[i] = rgb(red, green, blue);
            }
            else{
                int red = (red(pixels[i]) +red(pixels[i+1]) + red(pixels[i+1-width]) + red(pixels[i-1]) + red(pixels[i-1-width]) + red(pixels[i-width]) + red(pixels[i+width]) + red(pixels[i+1+width])+ red(pixels[i-1+width])/9);
                int blue = (blue(pixels[i]) +blue(pixels[i+1]) + blue(pixels[i+1-width]) + blue(pixels[i-1]) + blue(pixels[i-1-width]) + blue(pixels[i-width]) + blue(pixels[i+width]) + blue(pixels[i+1+width])+ blue(pixels[i-1+width])/9);
                int green = (green(pixels[i]) +green(pixels[i+1]) + green(pixels[i+1-width])+ green(pixels[i-1]) + green(pixels[i-1-width]) + green(pixels[i-width]) + green(pixels[i+width]) + green(pixels[i+1+width])+ green(pixels[i-1+width])/9);
                pixelsBlur[i] = rgb(red,green,blue);
            }
        }
        img.setPixels(pixelsBlur, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
    }

    public void greyKeepColor(Bitmap img, int hue){
        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
        for(int i = 0; i<pixels.length;i++){
            float[] hsv = new float[3];
            //Color. colorToHSV(pixels[i],hsv);
            //if( !(Math.abs(hsv[0] - hue) <= 30 || Math.abs(hsv[0] - hue) >= 320 ))
            double huei = getHue(red(pixels[i]), green(pixels[i]), blue(pixels[i]));
            if( !(Math.abs(huei - hue) <= 30 || Math.abs(huei - hue) >= 320 ))
            {
                int newColor = (int) Math.round(0.3 * red(pixels[i]) + 0.59 * green(pixels[i]) + 0.11 * blue(pixels[i]));
                pixels[i] = rgb(newColor, newColor, newColor);
                //pixels[i] = rgb(red(pixels[i]),0,0);
                //pixels[i] = rgb(0,green(pixels[i]),blue(pixels[i]));
            }
        }

        img.setPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());

    }

    public void increaseLum(Bitmap img, int d){
        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
        for(int i = 0; i<pixels.length;i++){
            pixels[i] = rgb(Math.max(0, Math.min(255,red(pixels[i])+ d)), Math.max(0, Math.min(255,green(pixels[i]) + d)), Math.max(0, Math.min(255,blue(pixels[i]) + d)));
        }
        img.setPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
    }

    public void DynamicLinearExtension(Bitmap img){
        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
        getHistrogram(img);
        int min=0;
        while(histogram[min] == 0) min++;
        int max=255;
        while(histogram[max] == 0) max--;
        int diff = max-min;
        int[] extDyn = new int[256];
        for(int i = min; i < 255; i++){
            extDyn[i] = 255*(i-min)/diff;
        }
        Log.w("LE DECES", "DynamicLinearExtension: " + pixels.length );
        for(int i = 0; i<pixels.length;i++){
            int tmp = (int) Math.round(0.3 * red(pixels[i]) + 0.59 * green(pixels[i]) + 0.11 * blue(pixels[i]));
            pixels[i] = rgb(extDyn[tmp], extDyn[tmp], extDyn[tmp]);
        }
        img.setPixels(pixels, 0, img.getWidth(), 0,0,img.getWidth(), img.getHeight());
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
            bmp_Original = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            bmp_Copy = bmp_Original.copy(Bitmap.Config.ARGB_8888,true);
            choiceDone = true;
            getHistrogram(bmp_Copy);
            seekBar.setProgress(0);
        }
    }
}
