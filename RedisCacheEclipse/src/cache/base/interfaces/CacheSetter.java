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
 * @param <TData>
 */
public interface CacheSetter<TData> 
        extends Cache<TData> {
    void setData(String id, TData data);
}
