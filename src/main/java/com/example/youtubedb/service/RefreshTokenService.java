package com.example.youtubedb.service;

public abstract class RefreshTokenService<K, V> {
  RefreshTokenObserver observer;

  public void attach(RefreshTokenObserver argObserver) {
    observer = argObserver;
  }

  public V getValue(K key) {
    return (V) observer.getValue(key);
  }

  public void notifyObserversForUpdate(K key, V value) {
    observer.update(key, value);
  }

  public void notifyObserversForDelete(K key) {
    observer.delete(key);
  }
}
