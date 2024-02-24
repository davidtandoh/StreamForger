package com.streamforger.transform;

public interface DataProcessor<T, R> {
    R process(T data);
}
