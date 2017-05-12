package net.androidwing.javassist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    startActivity(new Intent(this,HaHaActivity.class));
  }

  @Override protected void onResume() {
    super.onResume();
  }
}
