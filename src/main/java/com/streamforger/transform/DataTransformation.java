package com.streamforger.transform;

import java.util.Optional;

public interface DataTransformation<T, R> {
    R transform(T data);
}
