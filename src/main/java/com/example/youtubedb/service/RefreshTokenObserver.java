package com.example.youtubedb.service;

public interface RefreshTokenObserver<K, V> {
  void update(K key, V value);
  void delete(K key);
  V getValue(K key);
}
