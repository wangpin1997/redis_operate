package cn.wpin.redis.string;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * stringRedisTemplate操作redis API
 * @author wangpin
 */
@Component
public class RedisString {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ValueOperations<String,String> values;


    /**
     * 在方法执行前初始化
     */
    @PostConstruct
    public void before(){
        values=stringRedisTemplate.opsForValue();
    }

    /**
     * string添加操作
     */
    public void testSet(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        //删除键，防止影响结果
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        System.out.println(values.get(methodName));
    }

    /**
     * string带有超时失效的添加操作
     */
    public void testSetTimeOut() throws InterruptedException {
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        //10秒后自动失效
        values.set(methodName,methodName,10L, TimeUnit.SECONDS);
        Thread.sleep(5000);
        System.out.println(stringRedisTemplate.hasKey(methodName));
        Thread.sleep(5000);
        System.out.println(stringRedisTemplate.hasKey(methodName));
        //依次输出为true和false
    }

    /**
     * 带偏移量的set操作
     * 比如下边本来value为“testSetOverWrite”，从偏移量set后成为“tetestSetOverWrite”
     */
    public void testSetOverWrite(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        System.out.println(stringRedisTemplate.hasKey(methodName));
        //带偏移量的set(从指定位置开始覆盖)
        values.set(methodName,methodName,2);
        System.out.println(values.get(methodName));
    }

    /**
     * 不能覆盖的set操作
     * 如果key已经存在，就输出false，否则true
     */
    public void testSetIfAbsent(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        System.out.println(values.setIfPresent(methodName,methodName));
        System.out.println(values.setIfPresent(methodName+"1",methodName+"1"));
        System.out.println(values.setIfAbsent(methodName,methodName));
        System.out.println(values.setIfAbsent(methodName+"2",methodName+"2"));
        //第一段前者为true，后者为false，这两个方法是互斥的
        //前者输出 false，因为已经存在 ，后者输出true
    }

    public void testMultiSetAndGet(){
        stringRedisTemplate.delete("wp0");
        stringRedisTemplate.delete("wp1");
        stringRedisTemplate.delete("wp2");
        stringRedisTemplate.delete("wp3");
        Map<String,String> param=new HashMap<>(16);
        param.put("wp0","wp0");
        param.put("wp1","wp1");
        param.put("wp2","wp2");
        param.put("wp3","wp3");
        //批量set值
        values.multiSet(param);
        //批量get值
        List<String> keyList=new LinkedList<>();
        keyList.add("wp0");
        keyList.add("wp1");
        keyList.add("wp2");
        //批量取值
        List<String> valueList=values.multiGet(keyList);
        System.out.println(valueList.toString());
        param.clear();
        param.put("wp2","wp21");
        param.put("wp4","wp4");
        //批量set，如果有不能全部满足就会返回false，所以下面输出“wp2”,而不是“wp21”
        System.out.println(values.multiSetIfAbsent(param));
        System.out.println(values.get("wp2"));
    }

    /**
     * 设置新的值并覆盖，并返回
     */
    public void testGetAndSet(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        System.out.println(values.getAndSet(methodName,methodName+"new"));
        System.out.println(values.get(methodName));
    }

    /**
     * 自增，支持long和double型，如果值不是数字会报下面错误
     */
    public void testIncrement(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,"2");
        System.out.println(values.increment(methodName,2L));
        System.out.println(values.increment(methodName,2.1));
        values.set(methodName+"1",methodName);
        System.out.println(values.increment(methodName+"1",2L));
        //依次输出 4,  4.1， redis.clients.jedis.exceptions.JedisDataException: ERR value is not an integer or out of range
    }

    /**
     * 追加，在值后面追加，如果key不存在就相当于set
     */
    public void testAppend(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        values.append(methodName,"123");
        System.out.println(values.get(methodName));
        // the result is "testAppend123"
    }

    /**
     * 截取，如下返回结果为"testG", （key,开始下标，结束下标）
     */
    public void testGetPart(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        System.out.println(values.get(methodName,0,4));
    }

    /**
     * 返回键对应值得长度，如下返回结果为“7”
     */
    public void testSize(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        System.out.println(values.size(methodName));
    }

    public void testSetBit(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        stringRedisTemplate.delete(methodName);
        values.set(methodName,methodName);
        System.out.println(values.setBit(methodName,9,true));
        System.out.println(values.get(methodName));

    }
}
