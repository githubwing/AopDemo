package com.wingsoft
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

public class PreDexTransform extends Transform {
  Project project

  public PreDexTransform(Project project) {
    this.project = project
  }

  @Override
  String getName() {
    return "preDex"
  }

  @Override
  Set<QualifiedContent.ContentType> getInputTypes() {
    return TransformManager.CONTENT_CLASS
  }

  @Override
  Set<? super QualifiedContent.Scope> getScopes() {
    return TransformManager.SCOPE_FULL_PROJECT
  }

  @Override
  boolean isIncremental() {
    return false
  }

  @Override
  void transform(Context context, Collection<TransformInput> inputs,
      Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider,
      boolean isIncremental) throws IOException, TransformException, InterruptedException {
    inputs.each {TransformInput input ->

      input.directoryInputs.each {DirectoryInput directoryInput->

        ClassInjector.injectDir(directoryInput.file.absolutePath)
        // 获取output目录
        def dest = outputProvider.getContentLocation(directoryInput.name,
            directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        project.logger.error('== 目录文件transform== '+ directoryInput.file.name)

        // 将input的目录复制到output指定目录
        FileUtils.copyDirectory(directoryInput.file, dest)
      }

      input.jarInputs.each {JarInput jarInput->

        //TODO 这里可以对input的文件做处理，比如代码注入！

        // 重命名输出文件（同目录copyFile会冲突）
        def jarName = jarInput.name
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        if(jarName.endsWith(".jar")) {
          jarName = jarName.substring(0,jarName.length()-4)
        }
        def dest = outputProvider.getContentLocation(jarName+md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        project.logger.error('== jar文件transform== ' + jarInput.file.name)

        FileUtils.copyFile(jarInput.file, dest)
      }
    }
  }
}