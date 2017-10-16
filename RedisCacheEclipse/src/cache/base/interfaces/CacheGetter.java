/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache.base.interfaces;

/**
 *
 * @author maxim
 * @param <TKey>
 * @param <TPersist>
 * @param <TData>
 */
public interface CacheGetter<TData> 
            extends Cache<TData> {
    TData getData(Key key);
}
