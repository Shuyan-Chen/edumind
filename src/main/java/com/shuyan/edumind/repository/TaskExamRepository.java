package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.TaskExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskExamRepository extends JpaRepository<TaskExam, Integer> {

    Page<TaskExam> findByGradeLevel(Integer gradeLevel, Pageable pageable);


}
