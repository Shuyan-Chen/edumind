package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.ExamPaperAnswer;
import com.shuyan.edumind.domain.other.KeyValue;
import com.shuyan.edumind.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExamPaperAnswerRepository extends JpaRepository<ExamPaperAnswer,Integer> {

    Page<ExamPaperAnswer> findBySubjectIdAndCAndCreateUser(Integer subjectId, Integer createUser, Pageable pageable);

}
