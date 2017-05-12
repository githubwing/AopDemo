package net.androidwing.aspectj;

import android.util.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by wing on 5/9/17.
 */
@Aspect public class ActivityTrace {
  private long timemillis;

  @Before("execution(* android.app.Activity.onCreate(..))")
  public void beforeMethod(JoinPoint joinPoint) {

    String key = joinPoint.getSignature().toString();
    if (!key.contains("net.androidwing.aspectj")) {
      return;
    }
    timemillis = System.currentTimeMillis();
  }

  @After("execution(* android.app.Activity.onResume(..))")
  public void afterMethod(JoinPoint joinPoint) {
    String key = joinPoint.getSignature().toString();
    if (!key.contains("net.androidwing.aspectj")) {
      return;
    }
    Log.e("wing",
        joinPoint.getThis().getClass() + "Activity started in :" + (System.currentTimeMillis()
            - timemillis) + "ms");
  }

  @Pointcut("execution(@net.androidwing.aspectj.CheckLogin * *(..))") public void checkLogin() {
  }

  @Before("checkLogin()") public void onDebugToolMethodBefore(JoinPoint joinPoint)
      throws Throwable {

    if (App.isLogin()) {
      Log.e("wing", "login");
    } else {
      Log.e("wing", "notLogin");
    }
  }
}
