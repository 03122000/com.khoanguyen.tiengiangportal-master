package app.tiengiangportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityWelcome extends AppCompatActivity {
    ImageView btnAllPlace;
    TextView btnScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnAllPlace = (ImageView) findViewById(R.id.btnAllPlace);
        btnScanner = (TextView) findViewById(R.id.btnScanner);
        btnAllPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(),ActivityMain.class);
                startActivity(intent);
            }
        });
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(),ActivityScanner.class);
                startActivity(intent);
            }
        });
    }
}
