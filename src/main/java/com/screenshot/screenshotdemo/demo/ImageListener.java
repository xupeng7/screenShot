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
import java.math.BigInteger;

public class ImageListener extends FileAlterationListenerAdaptor {
    /*
        private static int a = 0;
        private static int b = 0;*/
    //表示第一次进来
    private static boolean isFirst = true;


    public static String Reportname = "gsqxjb_risk_zyt_road";

    public static String FileLocaltion = File.separator + "static" + File.separator + "pdf" + File.separator;

    public File DirContext;

    public ImageListener(File dirContext) {
        super();
        DirContext = dirContext;
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
            System.out.println("文件名" + file.getName().substring(0, 8));
            //todo 也可以不用bigInteager 取到上次截屏的文件名 然后转为int -5 之后再转为string
            System.out.println("相减之后" + new BigInteger(file.getName().substring(0, 8)).subtract(new BigInteger("5")));
            String lastFileName = String.valueOf(new BigInteger(file.getName().substring(0, 8)).subtract(new BigInteger("5")));
            File lastFile = new File("E://screenShot/" + lastFileName + ".png");
            String fileMD5 = FileMD5Uitl.getFileMD5(file);
            String fileMD52 = FileMD5Uitl.getFileMD5(lastFile);
            System.out.println(file.getName() + " 的MD5值是：" + fileMD5);
            System.out.println(lastFile.getName() + " 的MD5值是：" + fileMD52);

            if (fileMD5.equals(fileMD52)) {
                System.out.println("两张图片一致..");
            } else {
                System.out.println("两张图片不一致...");
            }

            //不管图片是否一致 都将画面中全部的红蓝点个数都数出来
            getImagePixel("E:\\screenShot\\" + file.getName(), false);

            //todo 暂时未解决红蓝点新增时累加的问题,可能是向下加,可能是像左移动来加.无法判断是哪一种加法
            /* //如果是第一次
            if (isFirst){
                System.out.println("第一次数,将全部个数数出来");
                getImagePixel("E:\\screenShot\\" + file.getName(),false);
                isFirst=false;
            }else if (fileMD5.equals(fileMD52)){
                System.out.println("两张图片一致..");
                getImagePixel("E:\\screenShot\\" + file.getName(),false);
            }else {
                System.out.println("两张图片不一致...");
                //umActive("E:\\screenShot\\" + file.getName());
                getImagePixel("E:\\screenShot\\" + file.getName(),true);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long didTime = System.currentTimeMillis();
        System.out.println("总耗时" + (didTime - preTime) + "ms");
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
     * flag 是true表示是读取后面新增的红蓝点数  false表示是第一次读取全部的个数
     *
     * @throws Exception
     */
    public void getImagePixel(String image, boolean flag) throws Exception {
        int[] rgb = new int[3];
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // todo 未测试 如果图片中的红蓝点往左移动,那么只数最后一列的个数.这只能实现往左移动的累加,不能实现向下的累加
        if (flag) {
          /*  for (int h = 4; h < 255; h = h + 21) {
                for (int w = 700; w < 845; w = w + 21) {
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

            System.out.println("加上最新的蓝色总数" + a);
            System.out.println("加上最新的红色总数" + b);
            System.out.println(".................");*/
        } else {

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

    }

    /*  *//**如果截取的图片与上一张不一样就调用此方法
     * 数每次更新的区域
     * @param image
     *//*
    public void sumActive(String image) {

        int[] rgb = new int[3];
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int h = 4; h < 255; h = h + 21) {
            for (int w = 816; w < 850; w = w + 21) {
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


    }*/

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
        System.setProperty("java.awt.headless", "false");
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
