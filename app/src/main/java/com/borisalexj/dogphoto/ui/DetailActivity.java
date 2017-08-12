package com.borisalexj.dogphoto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisalexj.dogphoto.R;
import com.borisalexj.dogphoto.db.DogOrm;
import com.borisalexj.dogphoto.models.DogModel;
import com.borisalexj.dogphoto.util.Constants;
import com.borisalexj.dogphoto.util.Utils;

public class DetailActivity extends AppCompatActivity {
    private String TAG = Constants.TAG + this.getClass().getSimpleName();

    private ImageView detailImageView;
    private TextView detailsDate;
    private TextView detailsGeo;
    private TextView detailsPoroda;
    private TextView detailsSize;
    private TextView detailsMast;
    private TextView detailsOshiynik;
    private TextView detailsName;
    private TextView detailsKlipsa;
    private TextView detailsOsobliviPrikmety;
    private TextView detailsPrimitki;
    private DogModel dm = new DogModel();

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

        detailImageView = (ImageView) findViewById(R.id.detail_image_view);
        detailsDate = (TextView) findViewById(R.id.details_date);
        detailsGeo = (TextView) findViewById(R.id.details_geo);
        detailsPoroda = (TextView) findViewById(R.id.details_poroda);
        detailsSize = (TextView) findViewById(R.id.details_size);
        detailsMast = (TextView) findViewById(R.id.details_mast);
        detailsOshiynik = (TextView) findViewById(R.id.details_oshiynik);
        detailsName = (TextView) findViewById(R.id.details_name);
        detailsKlipsa = (TextView) findViewById(R.id.details_klipsa);
        detailsOsobliviPrikmety = (TextView) findViewById(R.id.details_osoblivi_prikmety);
        detailsPrimitki = (TextView) findViewById(R.id.details_primitki);

        fillData();
    }

    private void fillData() {
        Utils.setImageViewFromFile(detailImageView, dm.getPhoto());
        detailsDate.setText(dm.getDate());
        detailsGeo.setText(dm.getAddress());
        detailsPoroda.setText(dm.getPoroda());
        detailsSize.setText(dm.getSize());
        detailsMast.setText(dm.getMast());
        detailsOshiynik.setText(dm.getOshiynik());
        detailsName.setText(dm.getName());
        detailsKlipsa.setText(dm.getKlipsa());
        detailsOsobliviPrikmety.setText(dm.getPrikmety());
        detailsPrimitki.setText(dm.getPrimitki());
    }
}
