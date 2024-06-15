package com.mmg.app.service.impl;

import com.mmg.app.model.Type;
import com.mmg.app.repository.TypeRepository;
import com.mmg.app.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }
}
