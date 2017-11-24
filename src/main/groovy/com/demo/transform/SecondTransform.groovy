package com.demo.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.Format
import com.android.build.gradle.internal.pipeline.TransformManager;

/**
 * Author:yuanzeyao
 * Date:2017/11/15 16:33
 * Email:yuanzeyao@qiyi.com
 */

public class SecondTransform extends Transform{
    @Override
    String getName() {
        return "second"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
         return TransformManager.CONTENT_JARS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        def outDir = transformInvocation
                .getOutputProvider().getContentLocation("second", outputTypes, scopes, Format.DIRECTORY)
        outDir.deleteDir()
        outDir.mkdirs()
        transformInvocation.getInputs().each {
            it.directoryInputs.each {
                int pathBitLen = it.file.toString().length()
                it.file.traverse {
                    def path = "${it.toString().substring(pathBitLen)}"
                    if (it.isDirectory()) {
                        new File(outDir, path).mkdirs()
                    } else {
                        if (! path.endsWith("BuildConfig.class")) {
                            new File(outDir, path).bytes = it.bytes
                        }
                    }
                }
            }
        }
    }
}
