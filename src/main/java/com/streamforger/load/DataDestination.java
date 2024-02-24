package com.streamforger.load;

public interface DataDestination<T> {
    void initialize();
    void writeData(T data);
}
