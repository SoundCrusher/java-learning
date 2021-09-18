package com.ogrom.javalearning.unsafe;

/**
 * <p>Company: </p>
 * <p>Description:  </p>
 *
 * @author wanghao
 * @version 1.0.0
 * @email "mailto:ogromwang@gmail.com"
 * @date 2021.09.18 14:00
 * @since 1.0.0
 */
public interface IUnsafeDemo {

    /**
     * 内存操作
     *
     * @since 1.0.0
     */
    void memory();

    /**
     * Cas 操作
     *
     * @since 1.0.0
     */
    void cas();

    /**
     * Clazz 操作
     *
     * @since 1.0.0
     */
    void clazz();

    /**
     * Obj opt 对象操作
     *
     * @since 1.0.0
     */
    void objOpt();

    /**
     * Thread 线程调度
     *
     * @since 1.0.0
     */
    void thread();

    /**
     * System info 系统信息
     *
     * @since 1.0.0
     */
    void systemInfo();

    /**
     * Memory barrier 内存屏障
     *
     * @since 1.0.0
     */
    void memoryBarrier();

    /**
     * Array 数组操作
     *
     * @since 1.0.0
     */
    void array();

}
