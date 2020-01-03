package com.jx.ols_main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OlsMainApplicationTests {

    Log logger = LogFactory.getLog(OlsMainApplicationTests.class);
    @Test
    void contextLoads() {
        logger.info("日志已搞定");
    }

}
