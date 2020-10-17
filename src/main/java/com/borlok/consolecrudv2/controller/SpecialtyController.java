package com.borlok.consolecrudv2.controller;

import com.borlok.consolecrudv2.model.Specialty;
import com.borlok.consolecrudv2.repository.SpecialtyRepository;
import com.borlok.consolecrudv2.repository.io.SpecialtyRepositoryImpl;

import java.util.List;

public class SpecialtyController {
    private final SpecialtyRepository specialtyRepository;

    public SpecialtyController() {
        specialtyRepository = SpecialtyRepositoryImpl.getInstance();
    }

    public Specialty create(String name) {
        Specialty specialty = new Specialty(name);
        List<Specialty> specialtyList = specialtyRepository.getAll();
        if (specialtyList.isEmpty())
            specialty.setId(1);
        else
            specialty.setId(specialtyList.get(specialtyList.size() - 1).getId() + 1);
       return specialtyRepository.create(specialty);
    }

    public List<Specialty> read() {
        return specialtyRepository.getAll();
    }

    public Specialty getById(Long id) {
        return specialtyRepository.getById(id);
    }

    public Specialty update (String name, Long id) {
        Specialty specialty = new Specialty(name);
        specialty.setId(id.intValue());
        return specialtyRepository.update(specialty, id);
    }

    public void delete(Long id) {
        specialtyRepository.delete(id);
    }
}
