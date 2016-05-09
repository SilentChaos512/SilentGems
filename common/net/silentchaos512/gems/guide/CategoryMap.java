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

    // Guide-API reverses the list, so we append to the front end.
    sortedValues.add(0, value);
    return super.put(key, value);
  }

  @Override
  public Collection<V> values() {

    return sortedValues;
  }
}
