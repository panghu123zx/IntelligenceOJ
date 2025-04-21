package com.ph.phojcommon.constant;

/**
 * 通用常量
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 交换机名称
     */
    String EXCHANGE_NAME = "oj_exchange";
    /**
     * 队列名称
     */
    String QUEUE_NAME = "oj_queue";
    /**
     * 路由键名称
     */
    String ROUTING_KEY = "oj_routing";

    /**
     * 死信交换机
     */
    String DLX_EXCHANGE = "dead_exchange";
    /**
     * 死信队列
     */
    String DLX_QUEUE = "dead_queue";
    /**
     * 死信路由
     */
    String DLX_ROUTING = "dead_routing";

}
