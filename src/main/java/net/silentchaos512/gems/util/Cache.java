package net.silentchaos512.gems.util;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenCustomHashMap;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Basically a {@link java.util.Map} which never exceeds the specified maximum size by either evicting
 * entries older than the specified age or if no such entries exist by returning a fallback value.<br>
 * Values are calculated with the specified {@link Function}.<br>
 * An {@link it.unimi.dsi.fastutil.Hash.Strategy} can optionally be specified to be able to use keys
 * whose default implementation isn't suitable for use in a {@link java.util.HashMap}<br>
 * This implementation is generified version of {@link net.minecraft.client.renderer.BannerTextures.Cache}
 */
public class Cache<K, V> {
  private final Object2ObjectLinkedOpenCustomHashMap<K, CacheEntry<V>> cacheMap;
  private final Function<K, CacheEntry<V>> loader;
  private final Consumer<V> cleanup;
  private final V fallback;
  private final long age;

  public Cache(Function<K, V> loader, V fallback, int size, long age) {
    this(new NaturalHashStrategy<>(), loader, fallback, size, age, null);
  }

  public Cache(Hash.Strategy<K> hash, Function<K, V> loader, V fallback, int size, long age,
      Consumer<V> cleanup) {
    if (hash == null) {
      hash = new NaturalHashStrategy<>();
    }
    cacheMap = new Object2ObjectLinkedOpenCustomHashMap<>(hash);
    this.loader = (key) -> {
      if (cacheMap.size() >= size && !freeCacheSlot()) {
        return null;
      }
      return new CacheEntry<>(loader.apply(key));
    };
    this.fallback = fallback;
    this.age = age;
    this.cleanup = cleanup;
  }

  public V get(K key) {
    CacheEntry<V> entry = cacheMap.getAndMoveToLast(key);
    if (entry == null) {
      entry = loader.apply(key);
      if (entry == null) {
        return fallback; //cache full
      }
      cacheMap.put(key, entry);
    }
    entry.timeStamp = System.currentTimeMillis();
    return entry.value;
  }

  private boolean freeCacheSlot() {
    CacheEntry<V> entry = cacheMap.get(cacheMap.firstKey());
    if (System.currentTimeMillis() - entry.timeStamp > age) {
      cacheMap.removeFirst();
      cleanup.accept(entry.value);
      return true;
    }
    return false;
  }

  public void clear() {
    cacheMap.forEach((k, v) -> cleanup.accept(v.value));
    cacheMap.clear();
  }

  private static class CacheEntry<V> {
    public final V value;
    public long timeStamp;

    private CacheEntry(V value) {
      this.value = value;
    }
  }

  private static class NaturalHashStrategy<K> implements Hash.Strategy<K> {
    @Override
    public int hashCode(K o) {
      return o.hashCode();
    }

    @Override
    public boolean equals(K a, K b) {
      return Objects.equals(a, b);
    }
  }
}
