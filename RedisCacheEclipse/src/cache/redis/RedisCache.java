/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache.redis;

import cache.serialize.DataSerializer;
import cache.base.impl.CacheBase;
import cache.partitions.CacheGetterMerger;
import cache.partitions.CacheSetterSplitter;
import java.util.HashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;

/**
 *
 * @author maxim
 * @param <TData>
 */
public class RedisCache<TData>
        extends CacheBase<TData> {

    protected enum RedisDataType {
        STRING,
        //LIST,
        HASH
    }
    private Jedis server;
    private DataSerializer<TData> serializer;

    protected final Jedis getServer() {
        return this.server;
    }

    protected final void setServer(Jedis newServer) {
        this.server = newServer;
    }

    public String serverInfo() {
        String result = this.server.info();
        return result;
    }

    public RedisCache(
            String serverName, int port,
            CacheSetterSplitter<TData> setterSplitter,
            CacheGetterMerger<TData> getterMerger,
            DataSerializer<TData> serializer) {
        super(setterSplitter, getterMerger);
        Jedis srv = new Jedis(serverName, port);
        this.serializer = serializer;
        this.setServer(srv);
    }

    @Override
    protected KeyValues<TData> provideData(Key key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void persistData(KeyValues<TData> data) throws IllegalArgumentException {
        RedisDataType redisAction = determineRedisType(data);
        switch (redisAction) {
            case STRING:
                persistString(data);
                break;
//            case LIST:
//                persistList(data);
//                break;
            case HASH:
                persistHash(data);
                break;
            default:
                throw new IllegalArgumentException("Unknown RedisDataType: " + redisAction);
        }
    }

    private RedisDataType determineRedisType(KeyValues<TData> data) {
        RedisDataType result;
        if (data.innerKeys == null) {
            result = RedisDataType.STRING;
        } else {
            result = RedisDataType.HASH;

        }
        return result;
    }

    private void persistString(KeyValues<TData> data) {
        this.getServer().set(data.outerKey, getSerializer().serialize(data.values[0]));
    }

//    private void persistList(KeyValues<TData> data) {
//        ArrayList<String> strings = new ArrayList<String>(data.values.length);
//        for (int i = 0; i < data.values.length; i++) {
//            strings.add(getSerializer().serialize(data.values[i]));
//        }
//        String[] stringArray = new String[strings.size()];
//        this.getServer().lpush(data.outerKey, stringArray);
//    }
    
    private void persistHash(KeyValues<TData> data) {
        Map<String, String> set = new HashMap<>();
        for (int i = 0; i < data.values.length; i++) {
            set.put(data.innerKeys[i], getSerializer().serialize(data.values[i]));
        }
        this.getServer().hmset(data.outerKey, set);
    }

    /**
     * @return the serializer
     */
    protected DataSerializer<TData> getSerializer() {
        return serializer;
    }

    /**
     * @param serializer the serializer to set
     */
    protected void setSerializer(DataSerializer<TData> serializer) {
        this.serializer = serializer;
    }

}
