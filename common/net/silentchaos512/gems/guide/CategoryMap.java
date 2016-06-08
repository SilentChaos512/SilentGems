package net.silentchaos512.gems.guide;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * A custom hash map that returns a reversed, sorted list of values when values() is called. Basically, this is just to
 * fight the random ordering of entries in the latest version of Guide-API (2.0.0-31).
 * 
 * @author Silent
 */
public class CategoryMap<K, V> extends HashMap<K, V> {

  List<V> sortedValues = Lists.newArrayList();

  @Override
  public V put(K key, V value) {

    // Guide-API no longer reverses the list, so append to the end.
    sortedValues.add(value);
    return super.put(key, value);
  }

  @Override
  public Collection<V> values() {

    return sortedValues;
  }
}
