package com.streamforger.extract;

public interface DataSource<T> {
    void initialize();
    Iterable<T> readData();
    void acknowledge(T message);
}
