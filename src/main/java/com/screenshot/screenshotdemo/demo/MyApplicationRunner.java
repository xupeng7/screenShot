package com.screenshot.screenshotdemo.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner{
    //项目启动后马上启动监听
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ImageListener.listenStart();
    }
}
