package cn.wpin.redis.zset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * redisTemplate operate the zSet;
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisZSetTest {

    @Autowired
    private RedisZSet redisZSet;


    @Test
    public void test(){
        redisZSet.testAdd();
        redisZSet.testRemove();
    }

}
