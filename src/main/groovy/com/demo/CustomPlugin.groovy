package com.demo

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.BuildTypeData
import com.demo.task.RedirectOutTask
import com.demo.task.ShowDeviceTask
import com.demo.transform.FirstTransform
import com.demo.transform.SecondTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ResolutionStrategy;

/**
 * Author:yuanzeyao
 * Date:2017/11/15 10:54
 * Email:yuanzeyao@qiyi.com
 */

public class CustomPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
//        println("CustomPlugin:"+project.android.getAdbExe().toString());
//        println "Custom Plugin run Success....."
//        project.task("FirstTask"){
//            setGroup("Custom")
//            setDescription("Test CustomPlugin")
//        }

        Configuration config = project.configurations.create("mapfile") {
            visible = false
            transitive = false
            resolutionStrategy{
                cacheDynamicVersionsFor 0, 'seconds'
            }
            extendsFrom = []
        }


        def notation = [group     : "com.qiyi.video.allclasses",
                        name      : "mapping_lib",
                        version   : "latest.release"
        ]
//            println "Resolving artifact: ${notation}"
        Dependency dep = project.dependencies.add(config.name, notation)
        File file = config.fileCollection(dep).singleFile
        println "file:"+file.getAbsolutePath()

        //project.tasks.create("showDevice",ShowDeviceTask)
        project.afterEvaluate {
            project.android.applicationVariants.each { ApplicationVariant variant ->
                project.tasks.create("changeDir${variant.name.capitalize()}",RedirectOutTask,new RedirectOutTask.ConfigAction(project,variant))
            }
        }

        AppPlugin plugin = project.plugins.getPlugin("com.android.application")
        plugin.getVariantManager().buildTypes.each { key, BuildTypeData value ->
            println "key ->"+key;
            println "value:"+value.getSourceSet().toString()
        }

//        project.android.registerTransform(new FirstTransform())
//        project.android.registerTransform(new SecondTransform())
    }
}
