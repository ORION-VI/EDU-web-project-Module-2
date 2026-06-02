package org.example.mapper;

public interface DtoMapperInterface<RQ, RS, E> {
    RS toDto(E object);

    E toEntity(RQ object);
}
