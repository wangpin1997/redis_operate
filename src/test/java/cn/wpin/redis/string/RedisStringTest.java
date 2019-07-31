package cn.wpin.redis.string;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RedisTemplate操作spring
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisStringTest {

    @Autowired
    private RedisString redisString;

    @Test
    public void test() throws InterruptedException {
        //set操作
       // redisString.testSet();
        //带有超时的添加操作
//        redisString.testSetTimeOut();
        //带偏移量的添加操作
//        redisString.testSetOverWrite();
        //不能覆盖的添加操作
//        redisString.testSetIfAbsent();
        //批量添加删除操作
//        redisString.testMultiSetAndGet();
        //设置新的值，覆盖并返回
      //  redisString.testGetAndSet();
        //自增操作
     //   redisString.testIncrement();
        //追加操作
       // redisString.testAppend();
        //截取操作
      //  redisString.testGetPart();
        //获取值长度操作
        //redisString.testSize();
        //位操作
        redisString.testSetBit();
    }


}
