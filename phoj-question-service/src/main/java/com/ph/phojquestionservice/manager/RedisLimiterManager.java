package com.ph.phojquestionservice.manager;

import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 提供限流服务
 */
@Service
@Slf4j
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流的实现
     * @param key 用于区分不同的限流器
     */
    public void redissonLimiter(String key){
        //创建限流器
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        //每一秒最多访问2次         限流类型         速率    时间间隔      时间单位
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        //请求令牌
        boolean res = rateLimiter.tryAcquire(1);
        //限流之后抛出异常
        if(!res){
            throw new BusinessException(ErrorCode.TO_MANY_REQUEST);
        }
    }
}
