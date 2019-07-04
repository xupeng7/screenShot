package com.screenshot.screenshotdemo.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableScheduling
public class ScreenShot2 {

    //文件名是有规则的数字命名 方便找到上一个文件来做比较
    public static int longName=10000000;

    /**
     * 截图
     *
     * @param filePath 截图保存文件夹路径
     * @param fileName 截图文件名称
     * @throws Exception
     */
    public static void captureScreen(String filePath, String fileName) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        // 截图保存的路径
        File screenFile = new File(filePath + fileName);
        // 如果文件夹路径不存在，则创建
        if (!screenFile.getParentFile().exists()) {
            screenFile.getParentFile().mkdirs();
        }

        // 指定屏幕区域，参数x y表示起点(左上角定位坐标) w h表示要截取的图片的宽跟高
        //todo 赌博网址的图片截取坐标暂时如下
        //BufferedImage subimage = image.getSubimage(386, 691, 685, 204);

        BufferedImage subimage = image.getSubimage(0, 0, 500, 500);
        ImageIO.write(subimage, "png", screenFile);

    }
    //5s 截屏一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void start() throws Exception {
        //设置为false 不使用headless模式
        System.setProperty("java.awt.headless","false");
        //文件名是有规则的数字命名 方便找到上一个文件来做比较
        String name = String.valueOf(longName);
        longName=longName+5;
        System.out.println(name);
        captureScreen("E:" + File.separator + "screenShot" + File.separator, name + ".png");
    }

}
