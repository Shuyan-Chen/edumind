package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {
    List<Subject> findByLevel(Integer level);
}
