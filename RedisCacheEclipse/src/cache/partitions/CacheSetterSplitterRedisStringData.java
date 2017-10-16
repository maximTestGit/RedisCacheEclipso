/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache.partitions;

import cache.base.interfaces.Cache;
import java.lang.reflect.Array;

/**
 *
 * @author maxim
 */
public class CacheSetterSplitterRedisStringData implements CacheSetterSplitter<String> {

    private final String prefix;

    public CacheSetterSplitterRedisStringData(String prefix) {
        this.prefix = prefix;
    }
    @Override
    public Cache.KeyValues<String> split(String id, String data) {
        Cache.KeyValues<String> result = new Cache.KeyValues<>();
        result.outerKey = this.prefix+id;
        result.values = (String[]) Array.newInstance(String.class, 1);
        result.values[0] = data;
        return result;
    }
}
