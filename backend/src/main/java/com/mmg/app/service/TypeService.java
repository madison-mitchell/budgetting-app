package com.mmg.app.service;

import com.mmg.app.model.Type;

import java.util.List;

public interface TypeService {
    Type saveType(Type type);
    List<Type> getAllTypes();
}
