package com.tads.hardware.repository;

import com.tads.hardware.model.Hardware;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HardwareRepository extends JpaRepository<Hardware, Long>{
    
}
