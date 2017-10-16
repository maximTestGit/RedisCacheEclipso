/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache.serialize;

/**
 *
 * @author maxim
 */
public interface DataSerializer<TData> {
    String serialize(TData data);
}
