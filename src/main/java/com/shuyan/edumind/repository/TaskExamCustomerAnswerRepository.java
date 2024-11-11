package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.TaskExamCustomerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskExamCustomerAnswerRepository extends JpaRepository<TaskExamCustomerAnswer,Integer> {
}
