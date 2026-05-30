package org.example.mapper;

public interface DtoMapperInterface<D, E> {
    D toDto(E object);

    E toEntity(D object);
}
