package com.screenshot.screenshotdemo.demo;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageListener extends FileAlterationListenerAdaptor {


    public static String Reportname = "gsqxjb_risk_zyt_road";

    public static String FileLocaltion = File.separator + "static" + File.separator + "pdf" + File.separator;

    public File DirContext;

    public ImageListener(File dirContext) {
        super();
        DirContext = dirContext;
    }

    //文件夹创建
    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println(directory.getName() + "  |  文件夹被创建" + "  |  路径为：" + directory.getPath());
    }

    //文件夹改变
    @Override
    public void onDirectoryChange(File directory) {
        System.out.println(directory.getName() + "  |  文件夹被改变" + "  |   路径为：" + directory.getPath());
    }

    //文件夹删除
    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println(directory.getName() + "  |  文件夹被删除" + "  |   路径为：" + directory.getPath());
    }

    //文件创建
    @Override
    public void onFileCreate(File file) {
        Long preTime = System.currentTimeMillis();
        super.onFileCreate(file);
        System.out.println(file.getName() + "  |  文件被创建" + "  |   路径为：" + file.getPath());
        traverseFolder2(DirContext.getPath(), file.getName());
        int x = 0;

        try {
            x = getScreenPixel(100, 345);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        System.out.println(x + " - ");
        File file1 = new File("");
        try {
            String path = file1.getCanonicalPath();//获取项目根路径
        } catch (IOException e) {
            e.printStackTrace();
        }
        //getImagePixel("E:\\test.png");
        try {
            getImagePixel("E:\\screenShot\\" + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long didTime = System.currentTimeMillis();
        System.out.println("总耗时" + (didTime - preTime) + "ms");
    }

    //文件夹改变
    @Override
    public void onFileChange(File file) {
        super.onFileChange(file);
        System.out.println(file.getName() + "   |   文件被修改" + "  |   路径为：" + file.getPath());
        traverseFolder2(DirContext.getPath(), file.getName());
    }

    //文件删除
    @Override
    public void onFileDelete(File file) {
        super.onFileDelete(file);
        System.out.println(file.getName() + " 文件被删除" + "    路径为：" + file.getPath());
    }

    public void traverseFolder2(String path, String fileName) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder2(file2.getAbsolutePath(), fileName);
                    } else {
                        if (fileName.equals(file2.getName()) && file2.getName().contains(Reportname) && file2.getName().contains(".doc")) {
                            String name = file2.getName().substring(0, file2.getName().length() - 4);
                            // 得到静态资源的相对地址
                            String Apath = ImageListener.class.getClassLoader().getResource("").getPath().split("WEB-INF")[0].replaceAll("/", "\\\\"), subPath = Apath.substring(1, Apath.length());
                            File outFile = new File(subPath + FileLocaltion + name + ".pdf");
//                            WordToPdf(file2, outFile);
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    /**
     * 读取一张图片的RGB值
     *
     * @throws Exception
     */
    public void getImagePixel(String image) throws Exception {
        int[] rgb = new int[3];
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int a = 0;
        int b = 0;
        //第一区域


        for (int h = 4; h < 127; h = h + 21) {
            for (int w = 12; w < 600; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (w % 21 == 12) {
                    if (rgb[0] < 175 && rgb[2] > 175) {
                        a = a + 1;
                    } else if (rgb[0] > 175 && rgb[2] < 175) {
                        b = b + 1;
                    }
                }
            }
        }
        System.out.println("第一区域蓝色个数" + a);
        System.out.println("第一区域红色个数" + b);
        System.out.println(".................");
        /////////////////////////////////////////////////////////////////////////////////
        //第二区域测试 四个for循环

        int blue2 = 0;
        int red2 = 0;
        for (int h = 128; h < 186; h = h + 21) {
            for (int w = 6; w < 420; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (rgb[0] < 200 && rgb[2] > 175) {
                    blue2++;
                    a++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red2++;
                }

            }
        }

        for (int h = 128; h < 186; h = h + 21) {
            for (int w = 16; w < 420; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (rgb[0] < 200 && rgb[2] > 175) {
                    blue2++;
                    a++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red2++;
                }

            }
        }

        for (int h = 138; h < 186; h = h + 21) {
            for (int w = 6; w < 420; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (rgb[0] < 200 && rgb[2] > 175) {
                    a++;
                    blue2++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red2++;
                }

            }
        }

        for (int h = 138; h < 186; h = h + 21) {
            for (int w = 16; w < 420; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (rgb[0] < 200 && rgb[2] > 175) {
                    a++;
                    blue2++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red2++;

                }

            }
        }
        System.out.println("第二区域蓝色个数" + blue2);
        System.out.println("第二区域红色个数" + red2);
        System.out.println(".................");

        ////////////////////////////////////////////////////////////////////////////////////

        // 第三区域 两次width 位置起点不同 间距一样的for循环 为了解决间隔是小数的问题
        int blue3 = 0;
        int red3 = 0;
        for (int h = 194; h < 255; h = h + 10) {
            for (int w = 6; w < 420; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);

                if (rgb[0] < 175 && rgb[2] > 175) {
                    a++;
                    blue3++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red3++;
                }

            }
        }
        for (int h = 194; h < 255; h = h + 10) {
            for (int w = 16; w < 420; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (rgb[0] < 175 && rgb[2] > 175) {
                    a++;
                    blue3++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red3++;
                }

            }
        }

        System.out.println("第三区域蓝色个数" + blue3);
        System.out.println("第三区域红色个数" + red3);
        System.out.println(".................");
        /////////////////////////////////////////////////////////////////////////////////////
        //第四区域

        int blue4 = 0;
        int red4 = 0;

        for (int h = 194; h < 255; h = h + 21) {
            for (int w = 425; w < 570; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (rgb[0] < 200 && rgb[2] > 175) {
                    a++;
                    blue4++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red4++;
                }

            }
        }

        for (int h = 194; h < 255; h = h + 21) {
            for (int w = 435; w < 570; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (rgb[0] < 200 && rgb[2] > 175) {
                    a++;
                    blue4++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {

                    b++;
                    red4++;
                }

            }
        }

        for (int h = 204; h < 255; h = h + 21) {
            for (int w = 425; w < 570; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);

                if (rgb[0] < 200 && rgb[2] > 175) {
                    a++;
                    blue4++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red4++;
                }

            }
        }

        for (int h = 204; h < 255; h = h + 21) {
            for (int w = 435; w < 570; w = w + 21) {
                int pixel = bi.getRGB(w, h); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);

                if (rgb[0] < 200 && rgb[2] > 175) {
                    a++;
                    blue4++;
                } else if (rgb[0] > 175 && rgb[2] < 175) {
                    b++;
                    red4++;
                }

            }
        }

        System.out.println("第四区域蓝色个数" + blue4);
        System.out.println("第四区域红色个数" + red4);
        System.out.println(".................");
        System.out.println("蓝色的总个数" + a);
        System.out.println("红色的总个数" + b);
    }

    public void forTest() {

    }

    /**
     * 返回屏幕色彩值
     *
     * @param x
     * @param y
     * @return
     * @throws AWTException
     */
    public int getScreenPixel(int x, int y) throws AWTException { // 函数返回值为颜色的RGB值。
        Robot rb = null; // java.awt.image包中的类，可以用来抓取屏幕，即截屏。
        rb = new Robot();
        Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
        Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
        Rectangle rec = new Rectangle(0, 0, di.width, di.height);
        BufferedImage bi = rb.createScreenCapture(rec);
        int pixelColor = bi.getRGB(x, y);
        return 16777216 + pixelColor; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。
    }

    public static void listenStart() {
        File dir = new File("E://screenShot");
        FileAlterationMonitor monitor = new FileAlterationMonitor();
        IOFileFilter filter = FileFilterUtils.or(FileFilterUtils.directoryFileFilter(), FileFilterUtils.fileFileFilter());
        FileAlterationObserver observer = new FileAlterationObserver(dir, filter);
        observer.addListener(new ImageListener(dir));
        monitor.addObserver(observer);
        try {
            //开始监听
            monitor.start();
            System.out.println("文件监听中……");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
