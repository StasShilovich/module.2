package com.epam.esm.model.service.converter;

public interface Converter<T, K> {

    T toDTO(K k);

    K fromDTO(T t);
}
