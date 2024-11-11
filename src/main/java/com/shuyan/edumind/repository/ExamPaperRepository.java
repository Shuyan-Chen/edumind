package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.ExamPaper;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperPageVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPaperRepository extends JpaRepository<ExamPaper, Integer> {

    @Query("SELECT ep FROM ExamPaper ep WHERE ep.paperType = :paperType AND ep.subjectId = :subjectId AND ep.gradeLevel = :gradeLevel")
    Page<ExamPaper> findByPaperTypeAndSubjectIdAndGradeLevel(@Param("paperType") Integer paperType, @Param("subjectId") Integer subjectId, @Param("gradeLevel") Integer gradeLevel, Pageable pageable);

}
