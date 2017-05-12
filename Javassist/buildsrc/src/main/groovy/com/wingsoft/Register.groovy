package com.wingsoft;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import com.android.build.gradle.AppExtension
/**
 * Created by wing on 5/10/17.*/

public class Register implements Plugin<Project> {

  @Override public void apply(Project project) {

    def android = project.extensions.findByType(AppExtension)
    android.registerTransform(new PreDexTransform(project))
  }
}
