package com.mmg.app.controller;

import com.mmg.app.model.Type;
import com.mmg.app.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/types")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @PostMapping
    public Type createType(@RequestBody Type type) {
        return typeService.saveType(type);
    }

    @GetMapping
    public List<Type> getAllTypes() {
        return typeService.getAllTypes();
    }
}
