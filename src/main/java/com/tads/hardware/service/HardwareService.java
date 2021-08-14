package com.tads.hardware.service;

import java.util.List;

import com.tads.hardware.model.Hardware;
import com.tads.hardware.repository.HardwareRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HardwareService {
    
    HardwareRepository repository;

    @Autowired
    public void setRepository(HardwareRepository repository) {
        this.repository = repository;
    }

    public List<Hardware> findAll() {
        return repository.findAll();
    }

    public Hardware findById(Long id) {
        return repository.getById(id);
    }

    public void save(Hardware h) {
        repository.save(h);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
