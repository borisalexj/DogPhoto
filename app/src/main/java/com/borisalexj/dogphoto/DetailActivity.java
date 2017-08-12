package com.borisalexj.dogphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class DetailActivity extends AppCompatActivity {
    private String TAG = Info.TAG + this.getClass().getSimpleName();

    ImageView detail_image_view;
    TextView details_date;
    TextView details_geo;
    TextView details_poroda;
    TextView details_size;
    TextView details_mast;
    TextView details_oshiynik;
    TextView details_name;
    TextView details_klipsa;
    TextView details_osoblivi_prikmety;
    TextView details_primitki;
    DogModel dm = new DogModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent incomingIntent = getIntent();
        int dog_id = -1;
        if (incomingIntent != null) {
            dog_id = incomingIntent.getIntExtra("dog_id", -1);
        }

        dm = (new DogOrm(this, TAG)).getDog(dog_id);

        detail_image_view = (ImageView) findViewById(R.id.detail_image_view);
        details_date = (TextView) findViewById(R.id.details_date);
        details_geo = (TextView) findViewById(R.id.details_geo);
        details_poroda = (TextView) findViewById(R.id.details_poroda);
        details_size = (TextView) findViewById(R.id.details_size);
        details_mast = (TextView) findViewById(R.id.details_mast);
        details_oshiynik = (TextView) findViewById(R.id.details_oshiynik);
        details_name = (TextView) findViewById(R.id.details_name);
        details_klipsa = (TextView) findViewById(R.id.details_klipsa);
        details_osoblivi_prikmety = (TextView) findViewById(R.id.details_osoblivi_prikmety);
        details_primitki = (TextView) findViewById(R.id.details_primitki);

        fillData();
    }

    private void fillData() {
        String filename = dm.getPhoto();
        if (!TextUtils.isEmpty(filename)) {
            File imgFile = new File(filename);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                detail_image_view.setImageBitmap(myBitmap);

            }
        }


        details_date.setText(dm.getDate());
        details_geo.setText(dm.getAddress());
        details_poroda.setText(dm.getPoroda());
        details_size.setText(dm.getSize());
        details_mast.setText(dm.getMast());
        details_oshiynik.setText(dm.getOshiynik());
        details_name.setText(dm.getName());
        details_klipsa.setText(dm.getKlipsa());
        details_osoblivi_prikmety.setText(dm.getPrikmety());
        details_primitki.setText(dm.getPrimitki());
    }
}
