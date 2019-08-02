package cn.wpin.redis.set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;

/**
 * redisTemplate operate set
 * @author wangpin
 */
@Component
public class RedisSet {

    @Autowired
    private RedisTemplate redisTemplate;

    private SetOperations<String, Object> set;

    @PostConstruct
    private void before() {
        set = redisTemplate.opsForSet();
    }

    /**
     * the method add() size() members() isMember()
     */
    public void testAddAndSizeAndMember() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        //print 1
        System.out.println(set.add(methodName, methodName));
        //print 2
        System.out.println(set.add(methodName, methodName, "b", "c"));
        //print 3
        System.out.println(set.size(methodName));
        //print 2
        System.out.println(set.add(methodName, new Object[]{1, 2}));
        //return all members print[1, c, testAddAndSizeAndMember, b, 2]
        System.out.println(set.members(methodName));
        //return true
        System.out.println(set.isMember(methodName, 1));

    }

    /**
     * the method remove(), move() ,pop()
     */
    public void testRemoveAndMoveAndPop() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        set.add(methodName, "a", "b", "c");
        //print 2
        System.out.println(set.remove(methodName, "a", "b"));
        //print c
        System.out.println(set.members(methodName));
        set.add(methodName, "a", "b", "c");
        //remove and return  one random member print a
        System.out.println(set.pop(methodName));
        //print [b,c]
        System.out.println(set.members(methodName));
        //print true
        System.out.println(set.move(methodName, "a", methodName));
        //print false because the destKey not exist
        System.out.println(set.move(methodName, "a", "key1"));
        set.add("key1", "a");
        //print true
        System.out.println(set.move(methodName, "a", "key1"));
    }

    /**
     * the method intersect() intersectAndStore()
     */
    public void testIntersect() {
        redisTemplate.delete("k1");
        redisTemplate.delete("k2");
        set.add("k1", "a", "v", "c", "d");
        set.add("k2", "a", "b", "c", "e");
        //print [a,c]
        System.out.println(set.intersect("k1", "k2"));
        set.add("k3", "a", "f", "g");
        //print [a]
        System.out.println(set.intersect("k1", Arrays.asList("k2", "k3")));
        //print 2 add the intersection of k1 k2 to k4
        System.out.println(set.intersectAndStore("k1", "k2", "k4"));
        redisTemplate.delete("k4");
        //print 1 add the intersection of k1,k2,and k3 to k4
        System.out.println(set.intersectAndStore("k1", Arrays.asList("k2", "k3"), "k4"));
    }

    /**
     * the method union() unionAndStore()
     */
    public void testUnion() {
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName+"2");
        redisTemplate.delete(methodName+"3");
        redisTemplate.delete(methodName+"4");
        redisTemplate.delete(methodName+"5");
        redisTemplate.delete(methodName+"6");
        set.add(methodName+"2", "a", "b", "c", "d", "e");
        set.add(methodName+"3", "c", "d", "e", "f", "g");
        //key and otherKey  union
        Set<Object> union = set.union(methodName+"2", methodName+"3");
        System.out.println(union);//[f, g, d, a, e, c, b]
        set.add(methodName+"4", "c", "h");
        //key and multi otherKey of collections union
        System.out.println(set.union(methodName+"2", Arrays.asList(methodName+"3",methodName+ "4")));//[h, f, g, d, a, e, c, b]
        //key and  otherKey s union  add to destKey
        System.out.println(set.unionAndStore(methodName+"2", methodName+"3", methodName+"5"));//7
        System.out.println(set.members("5"));//[f, g, d, a, e, c, b]
        //key and multi otherKey of collections union  add to destKey
        System.out.println(set.unionAndStore(methodName+"2", Arrays.asList(methodName+"3",methodName+ "4"), methodName+"6"));//8
        System.out.println(set.members(methodName+"6"));//[h, f, g, d, a, e, c, b]
    }

    /**
     * the method difference() differenceAndStore()
     */
    public void testDifference(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName+"1");
        redisTemplate.delete(methodName+"2");
        redisTemplate.delete(methodName+"3");
        redisTemplate.delete(methodName+"4");
        redisTemplate.delete(methodName+"5");
        set.add(methodName+"1", "a","b","c","d","e");
        set.add(methodName+"2", "c","d","e","f","g");
        //key and otherKey difference
        Set<Object> difference = set.difference( methodName+"1", methodName+"2");
        System.out.println(difference);//[a, b]
        set.add(methodName+"3", "c","h");
        //key and multi otherKey difference
        System.out.println(set.difference(methodName+"1", Arrays.asList(methodName+"2",methodName+"3")));//[a, b]
        //key and otherKey difference to save destKey
        System.out.println(set.differenceAndStore(methodName+"1", methodName+"2",methodName+"4"));//2
        System.out.println(set.members(methodName+"4"));//[a, b]
        //key and multi otherKey to save destKey
        System.out.println(set.differenceAndStore(methodName+"1", Arrays.asList(methodName+"2",methodName+"3"),methodName+"5"));//2
        System.out.println(set.members(methodName+"5"));//[a, b]
    }

    /**
     * the method randomMember() randomMembers()
     */
    public void testRandomMember(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        set.add(methodName, "a","b","c","d","e");
        //随机获取key无序集合中的一个元素
        System.out.println(set.randomMember(methodName));//e
        System.out.println(set.randomMember(methodName));//d
        System.out.println(set.randomMember(methodName));//c
        System.out.println(set.randomMember(methodName));//b
        System.out.println(set.randomMember(methodName));//e
        //获取多个key无序集合中的元素，count表示个数
        System.out.println(set.randomMembers(methodName,8));//[e, a, e, e, d, e, b, e]
        System.out.println(set.randomMembers(methodName,4));//[d, c, d, d]
        //获取多个key无序集合中的元素（去重），count表示个数
        System.out.println(set.distinctRandomMembers(methodName,6));//[c, e, d, a, b]
        System.out.println(set.distinctRandomMembers(methodName,4));//c, b, e, d]
    }

    /**
     * 测试扫描
     */
    public void testScan(){
        String methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
        redisTemplate.delete(methodName);
        set.add(methodName,"a","v","c");
        //print a,v,c
        Cursor<Object> cursor=set.scan(methodName, ScanOptions.NONE);
        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }

}
