package com.demo.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.Format
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.io.FileType
import org.apache.commons.io.FileUtils

/**
 * Author:yuanzeyao
 * Date:2017/11/15 16:33
 * Email:yuanzeyao@qiyi.com
 */

public class FirstTransform extends Transform{
    @Override
    String getName() {
        return "MyFirstTransForm"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_JARS
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
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        if (!isIncremental()) {
            transformInvocation.outputProvider.deleteAll()
        }


        transformInvocation.inputs.each {
            it.directoryInputs.each { directoryInput ->

                directoryInput.file.traverse (type: FileType.FILES){
                    println "it path:"+it.path;
                    def entryName = it.path.substring(directoryInput.file.path.length() + 1)
                    def destName = directoryInput.name + '/' + entryName
                    def dest = transformInvocation.outputProvider.getContentLocation(
                            destName, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                    println "entryName:"+entryName;
                    println "destName:"+destName;
                    println "dest:"+dest;
                    FileUtils.copyFile(it, dest)
                }
            }

            it.jarInputs.each { jarInput ->
                def dest = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                println "jar:"+jarInput.file
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }
}
