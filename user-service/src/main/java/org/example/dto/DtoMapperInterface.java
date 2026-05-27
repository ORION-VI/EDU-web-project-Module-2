package org.example.dto;

public interface DtoMapperInterface<D, E> {
    D toDto(E object);

    E toEntity(D object);
}
