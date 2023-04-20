package com.mc.web.controller.common;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import com.google.code.kaptcha.Producer;
import com.mc.common.constant.Constants;
import com.mc.common.core.domain.AjaxResult;
import com.mc.common.core.redis.RedisCache;
import com.mc.common.utils.sign.Base64;
import com.mc.common.utils.uuid.IdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 * 
 * @author mc
 */
@RestController
@Api(tags = {"通用-验证码"})
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;
    
    // 验证码类型
    @Value("${mc.captchaType}")
    private String captchaType;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    @ApiOperation("生成验证码")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        // 添加返回验证码内容
        ajax.put("capStr",capStr);
        //  验证码结果
        ajax.put("captchaCode",code);
        return ajax;
    }




    /**
     * 生成验证码
     */
    @GetMapping("/captchaImageHutool")
    @ApiOperation("captcha-生成验证码")
    public AjaxResult getHutoolCode(HttpServletResponse response) throws IOException
    {

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);

        //图形验证码写出，可以写出到文件，也可以写出到流
//        lineCaptcha.write(response.getOutputStream());
        BufferedImage image = null;

        String code = lineCaptcha.getCode();

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            image = captchaProducerMath.createImage(code);

            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }


        //输出code
        Console.log(lineCaptcha.getCode());


        //验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify(lineCaptcha.getCode());

        AjaxResult ajax = AjaxResult.success();
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        // 添加返回验证码内容

        ajax.put("capStr",code);
        //  验证码结果
//        ajax.put("captchaCode",code);
        return ajax;



    }
}
