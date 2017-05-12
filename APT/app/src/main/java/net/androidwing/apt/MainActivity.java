package net.androidwing.apt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.Contract;
import com.example.Inject;

@Contract public class MainActivity extends AppCompatActivity implements MainContract.View {

  @Inject MainContract.Presenter mPresenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MainInjector.inject(this);

  }
}
