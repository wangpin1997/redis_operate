package cn.wpin.redis.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redisTemplate operate redis List
 * @author wangpin
 */
@Component
public class RedisList {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisProperties redisProperties;

    private ListOperations<String, Object> lists;

    /**
     * 方法执行前初始化，怕redisTemplate还没被装配，所以放在这里来实例化
     */
    @PostConstruct
    private void before(){
        //指定下database，更好的使用图形化界面看数据
        redisProperties.setDatabase(1);
        lists=redisTemplate.opsForList();
    }


    /**
     * 给list添加值，左添，即后入的就是下标小的
     * range（key,0,-1）是全部输出，其他的是输出对应下标内的值
     */
    public void testLeftPush(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.leftPush(methodName,"a");
        lists.leftPush(methodName,"b");
        lists.leftPush(methodName,"c");
        lists.range(methodName,0,-1).stream().forEach(a-> System.out.println(a));
        lists.leftPush(methodName,"d");
        lists.leftPush(methodName,"e");
        lists.leftPush(methodName,"f");
        lists.range(methodName,0,1).stream().forEach(a-> System.out.println(a));
    }

    /**
     * 给list添加值，trim()方法是剪切的意思
     * (key,0,-1)全部剪切，其他剪切对应下标
     */
    public void testTrim(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.leftPush(methodName,"a");
        lists.leftPush(methodName,"b");
        lists.leftPush(methodName,"c");
        lists.trim(methodName,0,3);
        lists.range(methodName,0,-1).stream().forEach(b-> System.out.println(b));
    }

    /**
     * 获取key的长度 to get the key length
     */
    public void testSize(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        //print 0
        System.out.println(lists.size(methodName));
        lists.leftPush(methodName,methodName);
        //print 1
        System.out.println(lists.size(methodName));

    }

    /**
     * leftPushAll multi args and collections
     */
    public void testLeftPushAll(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.leftPushAll(methodName,"d","e","f");
        System.out.println(lists.size(methodName));
        List<String> objects=new LinkedList<>();
        objects.add("a");
        objects.add("b");
        objects.add("c");
        lists.leftPushAll(methodName,objects);
        //集合只算一个长度么？
        System.out.println(lists.size(methodName));
    }

    /**
     * 给已经存在的key添加数据
     */
    public void testLeftPushIfPresent(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.leftPushIfPresent(methodName,"123");
        //print 0 because the key not exist
        System.out.println(lists.size(methodName));
        lists.leftPush(methodName,"a");
        lists.leftPushIfPresent(methodName,"b");
        //print 2
        System.out.println(lists.size(methodName));
    }

    /**
     * 右边入队，即后进的就是下标大的
     */
    public void testRightPush(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.rightPush(methodName,"a");
        lists.rightPush(methodName,"b");
        lists.rightPush(methodName,"c");
        //将d插入到b的右边（后面）
        lists.rightPush(methodName,"b","d");
        //print a b c
        lists.range(methodName,0,-1).stream().forEach(a-> System.out.println(a));
    }

    /**
     * 右边添加多个
     */
    public void testRightPushAll(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.rightPushAll(methodName,"a","b","c");
        System.out.println(lists.size(methodName));
        lists.rightPushAll(methodName,new String []{"d","e","f"});
        lists.range(methodName,0,-1).stream().forEach(a-> System.out.println(a));
    }

    /**
     * 只有已存在的key才能添加进去
     */
    public void testRightPushIfPersent(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.rightPushIfPresent(methodName,"a");
        System.out.println(lists.size(methodName));
        lists.rightPush(methodName,"b");
        lists.rightPushIfPresent(methodName,"c");
        System.out.println(lists.size(methodName));
    }

    /**
     * 向指定下标插入值
     */
    public void testSet(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.rightPush(methodName,"a");
        //out of index range error
        lists.set(methodName,2,"b");
        // error no such key
        lists.set(methodName+"1",0,"b");
    }

    /**
     * 移除元素
     */
    public void testRemove(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.rightPush(methodName,"a");
        lists.rightPush(methodName,"b");
        lists.rightPush(methodName,"a");
        //移除第一个a，
        lists.remove(methodName,1,"a");

    }

    /**
     * 拿到指定下标的value值
     */
    public void testIndex(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.rightPushAll(methodName,new String[]{"a","b","c"});
        System.out.println(lists.index(methodName,2));
    }

    public void testLeftPop(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        lists.rightPushAll(methodName,new String[]{"a","b","c"});
        lists.leftPop(methodName);
        //可设置超时时间
        lists.leftPop(methodName,2L, TimeUnit.SECONDS);
        // print b c
        lists.range(methodName,0,-1).stream().forEach(a-> System.out.println(a));

    }

    /**
     * 删除左边队列的最右边节点并添加到右边边队列
     */
    public void testLeftPopAndLeftPush(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        redisTemplate.delete(methodName+1);
        lists.rightPushAll(methodName,"a","b","c");
        lists.rightPopAndLeftPush(methodName,methodName+1);
        //指定超时时间
        lists.rightPopAndLeftPush(methodName,methodName+1,2L,TimeUnit.SECONDS);
        lists.range(methodName,0,-1).stream().forEach(a-> System.out.println(a));
        lists.range(methodName+1,0,-1).stream().forEach(a-> System.out.println(a));
    }





}
