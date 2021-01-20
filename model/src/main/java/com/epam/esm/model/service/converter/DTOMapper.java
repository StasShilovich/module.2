package com.epam.esm.model.service.converter;

public interface DTOMapper<T, K> {

    T toDTO(K k);

    K fromDTO(T t);
}
