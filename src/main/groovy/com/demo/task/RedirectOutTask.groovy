package com.demo.task

import com.android.build.gradle.api.ApkVariant
import com.android.build.gradle.internal.scope.ConventionMappingHelper
import jdk.nashorn.internal.objects.annotations.Property
import org.gradle.api.Action;
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.com.esotericsoftware.kryo.NotNull;

/**
 * Author:yuanzeyao
 * Date:2017/11/23 16:27
 * Email:yuanzeyao@qiyi.com
 */

public class RedirectOutTask extends DefaultTask {
    @Input
    public String outputDir;
    @InputFile
    public File buildApkPath;
    public RedirectOutTask(){
        println "RedirectOutTask create....."+getName()
        setGroup("custom");
        setDescription("change out dir!")
    }

    @TaskAction
    def changeDir(){
        println "RedirectOutTask changeDir is execute..."
        File newDir = new File(outputDir);
        if(!newDir.exists()){
            newDir.mkdirs();
        }





        new File(outputDir,buildApkPath.getName()).withOutputStream { os->
            buildApkPath.withInputStream { is ->
                os << is;
            }
        }
    }

    public static class ConfigAction implements Action<RedirectOutTask> {
        @NotNull
        Project project;

        @NotNull
        ApkVariant variant

        public ConfigAction(Project mProject,ApkVariant mVariant){
            this.project = mProject;
            this.variant = mVariant;
        }


        @Override
        void execute(RedirectOutTask redirectOutTask) {
            println "execute ........................"
            File parentFile = variant.outputs.first().getOutputFile();
            println "outputs size:"+variant.outputs.size()
            variant.outputs.each { output ->
                def outputFolder =
                        "${parentFile.getParentFile().getParentFile().getAbsolutePath()}/customPath/${variant.dirName}"
                redirectOutTask.outputDir = outputFolder;
                redirectOutTask.buildApkPath = output.getOutputFile()
            }

            redirectOutTask.dependsOn variant.assemble
        }
    }
}
