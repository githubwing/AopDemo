package net.androidwing.apt;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.Contract;
import com.example.Inject;

/**
 * Created by wing on 5/9/17.
 */
@Contract
public class HahaActivity extends Activity implements HahaContract.View {
  @Inject  HahaPresenter mPresenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    HahaInjector.inject(this);
  }
}
