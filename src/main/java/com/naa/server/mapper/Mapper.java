package com.naa.server.mapper;

public interface Mapper <F, T>{
    T map(F object);

    default T mapFrom(F fromObject, T toObject){
        return toObject;
    }
}
