package cn.wpin.redis.hash;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisHashTest {

    @Autowired
    private RedisHash redisHash;

    @Test
    public void test(){
        redisHash.testPut();
        redisHash.testPutAll();
        redisHash.testDelete();
        redisHash.testHasKey();
        redisHash.testGet();
        redisHash.testIncrement();
    }
}
