package com.ogrom.javalearning.unsafe;

import org.springframework.stereotype.Component;

import sun.misc.Unsafe;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author wanghao
 * @version x.x.x
 * @email "mailto:wanghao@fkhwl.com"
 * @date 2021.09.18 13:49
 * @since x.x.x
 */
@Slf4j
@Component
public class UnsafeDemo implements IUnsafeDemo {

    /** Unsafe */
    private Unsafe unsafe;

    /**
     * 有两种手法可以获取 unsafe，不能直接通过静态方法get，如果检查到不是 启动类加载器加载器的，会报安全异常
     *   一：反射
     *   二：将加载 unsafe 的类的路径 通过 命令行参数追加到 启动类加载器
     *
     * @since y.y.y
     */
    @PostConstruct
    private void init() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            this.unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 内存操作
     *
     * @since 1.0.0
     */
    @Override
    public void memory() {
        // 返回的基地址
        long base = this.unsafe.allocateMemory(1024);
        log.info("返回的基地址为：{}", base);

        // 初始化值
        this.unsafe.setMemory(base, 1024, (byte) 0);

        // 释放内存
        log.info("释放内存: {}", base);
        this.unsafe.freeMemory(base);

        // DirectByteBuffer 中即使用了 Unsafe进行堆外内存的分配与初始化等
        // 当 DirectByteBuffer 仅被 Cleaner 引用（即为虚引用）时，其可以在任意
        // GC 时段被回收。当 DirectByteBuffer 实例对象被回收时，在 Reference-Handler
        // 线程操作中，会调用 Cleaner 的 clean 方法根据创建 Cleaner 时传入的 Deallocator 来进行堆外内存的释放。

        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference phantomReference = new PhantomReference(new Object(), referenceQueue);

        ThreadUtil.sleep(3000L);

    }

    /**
     * Cas 操作
     *
     * @since 1.0.0
     */
    @Override
    public void cas() {

    }

    /**
     * Clazz 操作
     *
     * @since 1.0.0
     */
    @Override
    public void clazz() {

    }

    /**
     * Obj opt 对象操作
     *
     * @since 1.0.0
     */
    @Override
    public void objOpt() {

    }

    /**
     * Thread 线程调度
     *
     * @since 1.0.0
     */
    @Override
    public void thread() {

    }

    /**
     * System info 系统信息
     *
     * @since 1.0.0
     */
    @Override
    public void systemInfo() {

    }

    /**
     * Memory barrier 内存屏障
     *
     * @since 1.0.0
     */
    @Override
    public void memoryBarrier() {

    }

    /**
     * Array 数组操作
     *
     * @since 1.0.0
     */
    @Override
    public void array() {

    }
}
