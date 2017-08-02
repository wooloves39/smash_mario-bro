package com.example.parkjaeha.supermario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import static com.example.parkjaeha.supermario.R.id.img_map;

/**
 * Created by parkjaeha on 2017-07-28.
 */

public class SubMenu extends ActionBarActivity {
//맵나오는 화면


    int mapNumber=9;
    ImageView imageView;
    Info image = new Info();
    Gallery gallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.submenu);

        imageView = (ImageView) findViewById(img_map);

        imageView.setImageResource(image.imageIDs[0]);
        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainact = new Intent(SubMenu.this,CharacterMenu.class);
                mainact.putExtra("map",mapNumber);
                startActivity(mainact);
            }
        });

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(position == 9 ){

                            imageView.setImageResource(image.imageIDs[position]);
                    position = (int)(Math.random()*9);
                }else {

                    imageView.setImageResource(image.imageIDs[position]);
                }

                System.out.println(position);
                mapNumber = position;

            }
        });


    }



        public class ImageAdapter extends BaseAdapter{
            private Context context;
            private int itemBackground;
            public ImageAdapter(Context c){
                context = c;
            }
            public int getCount(){
                return image.imageIDs.length;
            }

            public Object getItem(int postion){
            return postion;
            }

            public long getItemId(int postion){
                return postion;
            }
            public View getView(int postion, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(image.imageIDs[postion]);
                imageView.setLayoutParams(new Gallery.LayoutParams(400, 400));
                imageView.setBackgroundColor(Color.GRAY);
                return imageView;
            }

         }

}
