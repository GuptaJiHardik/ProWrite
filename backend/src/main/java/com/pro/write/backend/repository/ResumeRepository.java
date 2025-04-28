package com.pro.write.backend.repository;

import com.pro.write.backend.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, String> {
}
