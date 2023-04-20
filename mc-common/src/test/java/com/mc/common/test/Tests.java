package com.mc.common.test;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mc
 * @date 2023/3/3
 */

@SpringBootTest
public class Tests {

    @Test
    public void test() {
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);



        //图形验证码写出，可以写出到文件，也可以写出到流
        lineCaptcha.write("../img/line.png");

        //输出code
        Console.log(lineCaptcha.getCode());
        //验证图形验证码的有效性，返回boolean值
        Boolean isTrue = lineCaptcha.verify(lineCaptcha.getCode());

        //重新生成验证码
        lineCaptcha.createCode();
        lineCaptcha.write("../img/line.png");
        //新的验证码
        Console.log(lineCaptcha.getCode());
        //验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify("1234");
    }

    @Test
    public void test1(){
        for(int i=0; i<255; i++){ System.out.print("\032[38;2;"+i+";255;0m █"); }
    }

}
