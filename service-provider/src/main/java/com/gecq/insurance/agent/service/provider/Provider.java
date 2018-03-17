package com.gecq.insurance.agent.service.provider;

import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


/**
 * Created by gecha on 2016/12/30.
 */
public class Provider implements Container {
    private final Logger logger = LoggerFactory.getLogger(Provider.class);
    private ClassPathXmlApplicationContext context;

    @Override
    public void start() {
        context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
        context.start();
    }

    @Override
    public void stop() {
        try {
            if (context != null) {
                context.stop();
                context.close();
                context = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }
}
