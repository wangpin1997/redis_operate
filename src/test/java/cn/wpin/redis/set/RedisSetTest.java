package cn.wpin.redis.set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisSetTest {

    @Autowired
    private RedisSet redisSet;

    /**
     * know what it mean bu the name of method
     */
    @Test
    public void test(){
        redisSet.testAddAndSizeAndMember();
        redisSet.testRemoveAndMoveAndPop();
        redisSet.testIntersect();
        redisSet.testScan();
        redisSet.testRandomMember();
        redisSet.testUnion();
        redisSet.testDifference();
    }
}
