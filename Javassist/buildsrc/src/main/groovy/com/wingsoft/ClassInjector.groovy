package com.wingsoft

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtField
import javassist.CtMethod
import org.gradle.api.Project

class ClassInjector {

  def static pool = ClassPool.getDefault()
  def static androidJar = "/Users/wing/Library/Android/sdk/platforms/android-25/android.jar"

  static def injectDir(String path) {
    pool.appendClassPath(path)
    pool.insertClassPath(androidJar)
    File dir = new File(path)
    if (dir.isDirectory()) {
      dir.eachFileRecurse { File file ->

        String filePath = file.absolutePath
        if (filePath.endsWith(".class") && !filePath.contains('R$') &&
            !filePath.contains('R.class') &&
            !filePath.contains("BuildConfig.class")) {
          def className = filePath.split("debug")[1].
              substring(1).replace("/", ".").replace(".class", "")
          def c = pool.get(className)

          if (c.isFrozen()) {
            c.defrost()
          }

          def onCreateMethod = c.getDeclaredMethod("onCreate", pool.get("android.os.Bundle"))
          if (onCreateMethod != null) {
            c.addField(CtField.make("public long javassistTimeMillis;",c))
            onCreateMethod.insertBefore("javassistTimeMillis = System.currentTimeMillis();")
          }

          def onResumeMethod = c.getDeclaredMethod("onResume")
          if (onResumeMethod != null) {
            onCreateMethod.insertAfter("android.util.Log.e(${className}.class.getName(),\"Activity started in \"+(System.currentTimeMillis()- javassistTimeMillis)+\"ms\");")
          }


          c.writeFile(path)
          c.detach()
        }
      }
    }
  }
}

