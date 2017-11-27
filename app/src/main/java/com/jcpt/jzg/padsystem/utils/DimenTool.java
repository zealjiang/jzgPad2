package com.jcpt.jzg.padsystem.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Created by libo on 2017/7/18.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 自动适配生成 dimens 以适配各种大小屏幕,
 * 如果在dimens里新增了值，必须重新运行此工具类的main函数，使其重新自动生成多套 dimens
 */
    public class DimenTool {

        public static void gen() {
            //以此文件夹下的dimens.xml文件内容为初始值参照
            File file = new File("./app/src/main/res/values/dimens.xml");

            BufferedReader reader = null;
            StringBuilder sw240 = new StringBuilder();


            StringBuilder sw360 = new StringBuilder();// 手机：W:  1920 H: 1080  density: 3.0   densityDPI： 480

            StringBuilder sw392 = new StringBuilder();//小米 MIX2： W:  1920 H: 1080  density: 2.75  densityDPI:  440

            StringBuilder sw480 = new StringBuilder();
            StringBuilder sw600 = new StringBuilder();// pad： W:  1920 H: 1200  density: 2.0   densityDPI:  320
            StringBuilder sw720 = new StringBuilder();
            StringBuilder sw800 = new StringBuilder();
            StringBuilder w820 = new StringBuilder();

            try {

                System.out.println("生成不同分辨率：");

                reader = new BufferedReader(new FileReader(file));

                String tempString;

                int line = 1;

                // 一次读入一行，直到读入null为文件结束

                while ((tempString = reader.readLine()) != null) {


                    if (tempString.contains("</dimen>")) {

                        //tempString = tempString.replaceAll(" ", "");

                        String start = tempString.substring(0, tempString.indexOf(">") + 1);

                        String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                        //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                        Double num = Double.parseDouble
                                (tempString.substring(tempString.indexOf(">") + 1,
                                        tempString.indexOf("</dimen>") - 2));

                        String unit = tempString.substring(tempString.indexOf("</dimen>") -2,
                                                           tempString.indexOf("</dimen>"));


                            //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                            sw240.append(start).append( num * 0.4).append(end).append("\r\n");

//                          sw360.append(start).append( num * 0.6).append(end).append("\r\n");

                  //按照等比例，缩放系数是0.6，不过手机上效果太小，调整到0.75，而字体调整到0.85以免太小
                        if(unit.equals("sp")){

                            sw360.append(start).append( num * 0.85).append(end).append("\r\n");

                            sw392.append(start).append( num * 0.9).append(end).append("\r\n");//小米MIX2： W:  1920 H: 1080  density: 2.75   densityDPI:  440
                        }else {

                            sw360.append(start).append( num * 0.75).append(end).append("\r\n");

                            sw392.append(start).append( num * 0.8).append(end).append("\r\n");//小米MIX2： W:  1920 H: 1080  density: 2.75   densityDPI:  440
                        }


                            sw480.append(start).append(num * 0.8).append(end).append("\r\n");

                            sw600.append(start).append(num * 1).append(end).append("\r\n"); // 以pad为基准 1 ，计算其他等比例的缩放系数

                            sw720.append(start).append(num * 1.2).append(end).append("\r\n");

                            sw800.append(start).append(num * 1.3).append(end).append("\r\n");

                            w820.append(start).append(num * 1.3).append(end).append("\r\n");

                    } else {

                        append(sw240, sw360,sw392, sw480, sw600, sw720, sw800, w820, tempString);

                    }

                    line++;

                }

                reader.close();
                System.out.println("<!--  sw240 -->");

                System.out.println(sw240);

                System.out.println("<!--  sw360 -->");

                System.out.println(sw360);

                System.out.println("<!--  sw480 -->");

                System.out.println(sw480);

                System.out.println("<!--  sw600 -->");

                System.out.println(sw600);

                System.out.println("<!--  sw720 -->");

                System.out.println(sw720);

                System.out.println("<!--  sw800 -->");

                System.out.println(sw800);

                String sw240file = "./app/src/main/res/values-sw240dp/dimens.xml";

                String sw360file = "./app/src/main/res/values-sw360dp/dimens.xml";

                //小米MIX2： W:  1920 H: 1080  density: 2.75   densityDPI:  440
                String sw392file = "./app/src/main/res/values-sw392dp/dimens.xml";

                String sw480file = "./app/src/main/res/values-sw480dp/dimens.xml";

                String sw600file = "./app/src/main/res/values-sw600dp/dimens.xml";

                String sw720file = "./app/src/main/res/values-sw720dp/dimens.xml";

                String sw800file = "./app/src/main/res/values-sw800dp/dimens.xml";

                String w820file = "./app/src/main/res/values-w820dp/dimens.xml";

                //将新的内容，写入到指定的文件中去
                writeFile(sw240file, sw240.toString());
//
                writeFile(sw360file, sw360.toString());  //手机5.5寸：W:  1920 H: 1080  density:3.0   densityDPI： 480

                writeFile(sw392file, sw392.toString());   //小米MIX2： W:  1920 H: 1080  density: 2.75   densityDPI:  440


                writeFile(sw480file, sw480.toString());

                writeFile(sw600file, sw600.toString());  //Pad :     W:  1920  H: 1200  density:
                // 2.0   densityDPI:  320

                writeFile(sw720file, sw720.toString());

                writeFile(sw800file, sw800.toString());

                writeFile(w820file, w820.toString());

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                if (reader != null) {

                    try {

                        reader.close();

                    } catch (IOException e1) {

                        e1.printStackTrace();

                    }

                }

            }

        }

    private static void append(StringBuilder sw240
            , StringBuilder sw360, StringBuilder sw392,StringBuilder sw480,
                               StringBuilder sw600, StringBuilder sw720, StringBuilder sw800,
                               StringBuilder w820, String tempString) {
        sw240.append(tempString).append("\r\n");

        sw360.append(tempString).append("\r\n");

        sw392.append(tempString).append("\r\n");

        sw480.append(tempString).append("\r\n");

        sw600.append(tempString).append("\r\n");

        sw720.append(tempString).append("\r\n");

        sw800.append(tempString).append("\r\n");

        w820.append(tempString).append("\r\n");
    }


    /**
         * 写入方法
         *
         */

        public static void writeFile(String file, String text) {

            PrintWriter out = null;

            try {

                out = new PrintWriter(new BufferedWriter(new FileWriter(file)));

                out.println(text);

            } catch (IOException e) {

                e.printStackTrace();

            }



            out.close();

        }
        public static void main(String[] args) {

            gen();

        }

    }


