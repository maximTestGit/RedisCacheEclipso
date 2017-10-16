/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache.base.impl;

import cache.partitions.CacheGetterMerger;
import cache.partitions.CacheSetterSplitter;
import cache.base.interfaces.CacheGetter;
import cache.base.interfaces.CacheSetter;

/**
 *
 * @author maxim
 * @param <TPersist>
 * @param <TData>
 */
public abstract class CacheBase<TData>
        implements CacheGetter<TData>, CacheSetter<TData> {

    protected CacheSetterSplitter<TData> setterSplitter;
    protected CacheGetterMerger<TData> getterMerger;

    public CacheBase(
            CacheSetterSplitter<TData> setterSplitter,
            CacheGetterMerger<TData> getterMerger) {
        
        this.setterSplitter = setterSplitter;
        this.getterMerger = getterMerger;
    }

    @Override
    public TData getData(Key key) {
        KeyValues<TData> partitions = provideData(key);
        TData result = getterMerger.merge(partitions);
        return result;
    }

    @Override
    public void setData(String id, TData data) {
        KeyValues<TData> partitions = setterSplitter.split(id, data);
        persistData(partitions);
    }

    protected abstract KeyValues<TData> provideData(Key key);
    protected abstract void persistData(KeyValues<TData> data);
    
    
}
