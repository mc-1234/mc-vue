package com.mc.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author mc
 * @date 2023/3/2
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.mc.*"})
@MapperScan(value = {"com.mc.*.mapper","com.mc.*.*.mapper"})
public class MCApplication {
    public static void main(String[] args) {

        {
            SpringApplication.run(MCApplication.class, args);
            System.out.println("(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                    " .-------.       ____     __        \n" +
                    " |  _ _   \\      \\   \\   /  /    \n" +
                    " | ( ' )  |       \\  _. /  '       \n" +
                    " |(_ o _) /        _( )_ .'         \n" +
                    " | (_,_).' __  ___(_ o _)'          \n" +
                    " |  |\\ \\  |  ||   |(_,_)'         \n" +
                    " |  | \\ `'   /|   `-'  /           \n" +
                    " |  |  \\    /  \\      /           \n" +
                    " ''-'   `'-'    `-..-'              ");
        }
    }
}
