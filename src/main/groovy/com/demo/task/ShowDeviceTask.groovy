package com.demo.task;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;

/**
 * Author:yuanzeyao
 * Date:2017/11/15 11:55
 * Email:yuanzeyao@qiyi.com
 */

public class ShowDeviceTask extends DefaultTask {

    public ShowDeviceTask(){
        setGroup("custom")
        setDescription("show Devicese")
    }

    @TaskAction
    def showDevices(){
        def adbExe = project.android.getAdbExe().toString()
        println "${adbExe} devices".execute().text
    }
}
