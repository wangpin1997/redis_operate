package cn.wpin.redis.hash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * redisTemplate operate redis hash
 */
@Component
public class RedisHash {

    @Autowired
    private RedisTemplate redisTemplate;

    private HashOperations<String,String,Object> hashOperations;

    @PostConstruct
    private void before(){
        hashOperations= redisTemplate.opsForHash();
    }


    /**
     * hash put
     * entries 拿到所有的key value
     */
    public void testPut(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        hashOperations.put(methodName,"key1","a");
        hashOperations.put(methodName,"key2","b");
        hashOperations.put(methodName,"key3","c");
        Map<String,Object> map=hashOperations.entries(methodName);
        System.out.println(map);
    }

    /**
     * multi put hash
     */
    public void testPutAll(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        Map<String,Object> map=new HashMap<>();
        map.put("key1","a");
        map.put("key2","b");
        map.put("key3","c");
        hashOperations.putAll(methodName,map);
        System.out.println(hashOperations.entries(methodName));
    }

    /**
     * hash delete
     */
    public void testDelete(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        Map<String,Object> map=new HashMap();
        map.put("k1","v1");
        map.put("k2","v2");
        hashOperations.putAll(methodName,map);
        System.out.println(hashOperations.entries(methodName));
        //print {k1=v1,k2=v2}
        hashOperations.delete(methodName,"k1");
        System.out.println(hashOperations.entries(methodName));
        //print {k2=v2}
    }

    /**\
     * judge the key  is exist?
     */
    public void testHasKey(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        Map<String,Object> map=new HashMap<>();
        map.put("key1","a");
        map.put("key2","b");
        hashOperations.putAll(methodName,map);
        System.out.println(hashOperations.hasKey(methodName,"ket1"));
        System.out.println(hashOperations.hasKey(methodName,"ket3"));
    }

    /**
     * hash get() and multiGet()
     */
    public void testGet(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        Map<String,Object> map=new HashMap<>();
        map.put("key1","1");
        map.put("key2","2");
        hashOperations.putAll(methodName,map);
        //true
        System.out.println(hashOperations.get(methodName,"key1"));
        //false
        System.out.println(hashOperations.get(methodName,"key3"));
        //[1,2,null]
        System.out.println(hashOperations.multiGet(methodName, Arrays.asList("key1","key2","key3")));

    }

    /**
     * the hash value increment
     */
    public void testIncrement(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        Map<String,Object> map=new HashMap<>();
        map.put("key1","1");
        map.put("key2","v2");
        map.put("key3","v3");
        hashOperations.putAll(methodName,map);
        //print 2
        System.out.println(hashOperations.increment(methodName,"key1",1));
        //print 2.1
        System.out.println(hashOperations.increment(methodName,"key1",1.1));
        //error hash value not integer
        System.out.println(hashOperations.increment(methodName,"key2",1));
        //error hash value not float
        System.out.println(hashOperations.increment(methodName,"key1",1.1));

    }

    /**
     * return keys list ,values list and size
     */
    public void testKeysAndValues(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        Map<String,Object> map=new HashMap<>();
        map.put("k1","v1");
        map.put("k2","v2");
        map.put("k3","v3");
        hashOperations.putAll(methodName,map);
        //[k1,k2,k3]
        System.out.println(hashOperations.keys(methodName));
        //[v1,v2,v3]
        System.out.println(hashOperations.values(methodName));
        //print 3
        System.out.println(hashOperations.size(methodName));
        // only the hashKey not exist to success  return true
        System.out.println(hashOperations.putIfAbsent("pw","key","a"));
        //print false because the hashKey exist
        System.out.println(hashOperations.putIfAbsent("pw","key","a"));
    }

    /**
     * scan the matching  entries
     */
    public void testScan(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        Map<String,Object> map=new HashMap<>();
        map.put("key2","v1");
        map.put("key1","v2");
        map.put("key3","v3");
        hashOperations.putAll(methodName,map);
        Cursor<Map.Entry<String,Object>> cursor=hashOperations.scan(methodName, ScanOptions.NONE);
        while (cursor.hasNext()){
            Map.Entry<String,Object> entry=cursor.next();
            System.out.println(entry.getKey()+"--"+entry.getValue());
        }

    }
}
