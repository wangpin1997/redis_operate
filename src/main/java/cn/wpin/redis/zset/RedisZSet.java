package cn.wpin.redis.zset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * redisTemplate operate the zSet;
 */
@Component
public class RedisZSet {

    @Autowired
    private RedisTemplate redisTemplate;

    private ZSetOperations<String,Object> zSet;

    @PostConstruct
    private void before(){
        zSet=redisTemplate.opsForZSet();
    }

    /**
     * the method add()
     */
    public void testAdd(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        // the three param is double
        zSet.add(methodName,"a",1.0);
        ZSetOperations.TypedTuple<Object> obj=new DefaultTypedTuple<>("b",2.0);
        ZSetOperations.TypedTuple<Object> obj2=new DefaultTypedTuple<>("c",3.0);
        Set<ZSetOperations.TypedTuple<Object>>tuples=new HashSet<>();
        tuples.add(obj);
        tuples.add(obj2);
        //print 2
        System.out.println(zSet.add(methodName,tuples));
        // according to the score asc .  print [a,b,c]
        System.out.println(zSet.range(methodName,0,-1));
    }

    /**
     * the method remove() range()
     */
    public void testRemove(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        zSet.add(methodName,"a",1.0);
        System.out.println(zSet.range(methodName,0,-1));
        zSet.remove(methodName,"a");
        System.out.println(zSet.range(methodName,0,-1));
    }

    public void testIncrementScore(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        //print []
        System.out.println(redisTemplate.keys(methodName));
        //print -1.0
        System.out.println(zSet.incrementScore(methodName,"a",-1));
        // print [testIncrementScore]
        System.out.println(redisTemplate.keys(methodName));
    }

    public void testRank(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        zSet.add(methodName,"a",1);
        zSet.add(methodName,"b",3);
        zSet.add(methodName,"c",2);
        zSet.add(methodName,"d",-1);
        //from small to large. print [d,a,c,d]
        System.out.println(zSet.range(methodName,0,-1));
        // from small to large and return the index . [3]
        System.out.println(zSet.rank(methodName,"b"));
        System.out.println(zSet.reverseRange(methodName,0,-1));
    }

    public void testRangeWithScores(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
    }
}
