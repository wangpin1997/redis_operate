package cn.wpin.redis.zset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        redisZSet.testIncrementScore();
        redisZSet.testRank();
        redisZSet.testRangeWithScores();
        redisZSet.testBound();
    }

}
