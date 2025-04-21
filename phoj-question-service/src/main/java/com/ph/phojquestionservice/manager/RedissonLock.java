package com.ph.phojquestionservice.manager;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Supplier;

@Service
public class RedissonLock {

    @Resource
    private RedissonClient redissonClient;
    public <T> T lockExecute(String lockKey, Supplier<T> supplier){
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock();
            return supplier.get();
        }finally {
            lock.unlock();
        }
    }
}
