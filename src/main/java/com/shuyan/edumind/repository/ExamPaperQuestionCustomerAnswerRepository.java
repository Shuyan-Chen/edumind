package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.ExamPaperQuestionCustomerAnswer;
import com.shuyan.edumind.domain.other.KeyValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ExamPaperQuestionCustomerAnswerRepository extends JpaRepository<ExamPaperQuestionCustomerAnswer, Integer> {
    Page<ExamPaperQuestionCustomerAnswer> findByCreateUser(Integer createUser, Pageable pageable);

    List<ExamPaperQuestionCustomerAnswer> findByExamPaperAnswerId(Integer examPaperAnswerId);

    @Query(value = "SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS name, COUNT(*) AS value " +
            "FROM t_exam_paper_question_customer_answer " +
            "WHERE create_time BETWEEN :startTime AND :endTime " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d')", nativeQuery = true)
    List<KeyValue> countByCreateTimeBetween(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_exam_paper_question_customer_answer SET customer_score = :customerScore, do_right = :doRight WHERE id = :id", nativeQuery = true)
    int updateScores(@Param("id") Integer id, @Param("customerScore") Integer customerScore, @Param("doRight") Boolean doRight);

}
