
import cache.base.interfaces.Cache;
import cache.base.interfaces.CacheSetter;
import cache.partitions.CacheSetterSplitter;
import cache.partitions.CacheSetterSplitterRedisStringData;
import cache.redis.RedisCache;
import cache.serialize.DataSerializerJson;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class TestClass {

	public static void main(String[] args) {
		System.out.println("Test run");
		new TestClass().testSetData1();
		new TestClass().testSetData2();
		new TestClass().testSetData3();
	}
	
    public void testSetData1() {
        System.out.println("testSetData1");
        Date date = new Date();
        long id = date.getTime();
        RedisTestData data = new RedisTestData();
        data.intData = id;
        data.dateData = date;

        CacheSetter<Object> instance = new RedisCache<>(
                "localhost", 6379,
                new CacheSetterSplitterSimpleObj(),
                null,
                new DataSerializerJson<Object>()
        );
         instance.setData(new Long(id).toString(), data);
    }

    public class CacheSetterSplitterSimpleObj implements CacheSetterSplitter<Object> {

        @Override
        public Cache.KeyValues<Object> split(String id, Object data) {
            Cache.KeyValues<Object> result = new Cache.KeyValues<>();
            result.outerKey = "SimpleObj:"+id;
            result.values = (Object[]) Array.newInstance(Object.class, 1);
            result.values[0] = data;
            return result;
        }

    }

    public class CacheSetterSplitterRedisTestData implements CacheSetterSplitter<RedisTestData> {

        @Override
        public Cache.KeyValues<RedisTestData> split(String id, RedisTestData data) {
            Cache.KeyValues<RedisTestData> result = new Cache.KeyValues<>();
            result.outerKey = "RedisTestData:"+id;
            result.innerKeys = new String[]{"date", "int"};
            result.values = (RedisTestData[]) Array.newInstance(RedisTestData.class, 2);
            int iDate = Arrays.binarySearch(result.innerKeys, "date");
            result.values[iDate] = new RedisTestData();
            result.values[iDate].dateData = data.dateData;
            int iInt = Arrays.binarySearch(result.innerKeys, "int");
            result.values[iInt] = new RedisTestData();
            result.values[iInt].intData = data.intData;
            return result;
        }

    }
    
    public void testSetData2() {
        System.out.println("testSetData2");
        Date date = new Date();
        long id = date.getTime();
        RedisTestData data = new RedisTestData();
        data.intData = id;
        data.dateData = date;

//        Cache.Key key = new Cache.Key();
//        key.outerKey = "testSetData:2:"+id;
//        key.innerKeys = new String[]{"date", "int"};
        CacheSetter<RedisTestData> instance = new RedisCache<RedisTestData>(
                "localhost", 6379,
                new CacheSetterSplitterRedisTestData(),
                null,
                new DataSerializerJson<RedisTestData>()
        );
        instance.setData(Long.toString(id), data);
    }
    public void testSetData3() {
        System.out.println("testSetData3");

        Date date = new Date();
        long id = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = sdf.format(date);

        CacheSetter<String> instance = new RedisCache<String>(
                "localhost", 6379,
                new CacheSetterSplitterRedisStringData("string:"),
                null,
                new DataSerializerJson<String>()
        );
        instance.setData(Long.toString(id), data);
    }

    public class RedisTestData {

        public Long intData;
        public Date dateData;

    }
	

}
