package cn.wpin.redis.list;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisListTest {

    @Autowired
    private RedisList redisList;



    @Test
    public void test(){
        //左添加，即范围取值
        redisList.testLeftPush();
        //值剪切
        redisList.testTrim();
        //获取key长度
        redisList.testSize();
        //多个添加
        redisList.testLeftPushAll();
        //给已存在的key添加
        redisList.testLeftPushIfPresent();
        //右边入队
        redisList.testRightPush();
        //右边入队多个
        redisList.testRightPushAll();
        //右边入队已存在的key
        redisList.testRightPushIfPersent();
        //向指定下标插入值
        redisList.testSet();
        //移除元素
        redisList.testRemove();
        //下标
        redisList.testIndex();
        //左边出队
        redisList.testLeftPop();
        //删除左边key的最右元素，入右边
        redisList.testLeftPopAndLeftPush();
    }
}
