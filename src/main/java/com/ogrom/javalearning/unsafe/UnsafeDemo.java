package com.ogrom.javalearning.unsafe;

import org.springframework.stereotype.Component;

import sun.misc.Unsafe;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.concurrent.locks.StampedLock;

import javax.annotation.PostConstruct;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: </p>
 * <p>Description:  </p>
 *
 * @author wanghao
 * @version 1.0.0
 * @email "mailto:ogromwang@gmail.com"
 * @date 2021.09.18 13:49
 * @since 1.0.0
 */
@Slf4j
@Component
public class UnsafeDemo implements IUnsafeDemo {

    /** Unsafe */
    private Unsafe unsafe;

    private int x = 0;
    private int y = 0;

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
        @Data
        @AllArgsConstructor
        class Demo {
            int x;
            int y;
        }
        Demo demo = new Demo(0, 0);
        long offset = this.unsafe.objectFieldOffset(ReflectUtil.getField(Demo.class, "x"));
        log.info("cas前的结果：{}", demo);
        this.unsafe.compareAndSwapInt(demo, offset, 0, 1);
        log.info("cas后的结果：{}", demo);

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
        // 内存屏障，禁止 load 操作重排序。屏障前的 load 操作不能被重排序到屏障后，屏障后的
        // load 操作不能被重排序到屏障前
        this.unsafe.loadFence();
        // 内存屏障，禁止 store 操作重排序。屏障前的 store 操作不能被重排序到屏障后，屏障后
        // 的 store 操作不能被重排序到屏障前
        this.unsafe.storeFence();
        // 内存屏障，禁止 load、store 操作重排序
        this.unsafe.fullFence();

        StampedLock stampedLock = new StampedLock();
        long stamp = stampedLock.tryOptimisticRead();
        log.info("获取乐观读锁：{}", stamp);

        int innerX = this.x, innerY = this.y;
        long l1 = stampedLock.writeLock();
        try {
            this.x = 1;
            this.y = 1;
        } finally {
            stampedLock.unlockWrite(l1);
        }
        log.info("获取乐观读锁：{}", stampedLock.tryOptimisticRead());
        // 从主内存中 load 变量
        // 校验读到的值，是否和主内存中的不一致了
        // validate方法中 第一行就是 load 屏障，避免了 加载值和validate 的指令重排序
        if (!stampedLock.validate(stamp)) {
            long l = stampedLock.readLock();
            log.info("获取读锁：{}", l);
            l = stampedLock.readLock();
            log.info("获取读锁：{}", l);
            try {
                // 重新从主内存中 load 变量
                innerX = this.x;
                innerY = this.y;
            } finally {
                stampedLock.unlockRead(l);
            }
        }
        // 执行操作

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
