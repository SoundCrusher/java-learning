package com.ogrom.javalearning.unsafe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Company: </p>
 * <p>Description:  </p>
 *
 * @author wanghao
 * @version x.x.x
 * @email "mailto:wanghao@fkhwl.com"
 * @date 2021.09.18 14:07
 * @since x.x.x
 */
@SpringBootTest
class UnsafeDemoTest {

    @Resource
    private IUnsafeDemo unsafeDemo;

    @Test
    void memory() {
        this.unsafeDemo.memory();
    }

    @Test
    void cas() {
    }

    @Test
    void clazz() {
    }

    @Test
    void objOpt() {
    }

    @Test
    void thread() {
    }

    @Test
    void systemInfo() {
    }

    @Test
    void memoryBarrier() {
    }

    @Test
    void array() {
    }
}