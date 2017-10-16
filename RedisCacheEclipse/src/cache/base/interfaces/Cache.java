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
public interface Cache<TData> {

    public class Key {

        public String outerKey;
        public String[] innerKeys;
    }

    public class KeyValues<TData> extends Key {

        public KeyValues() {
            super();
        }
        public KeyValues(Key key) {
            outerKey = key.outerKey;
            innerKeys = key.innerKeys;
        }
        public TData[] values;
    }
}
