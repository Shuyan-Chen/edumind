package com.shuyan.edumind.service;


import com.shuyan.edumind.domain.Subject;
import com.shuyan.edumind.viewmodel.admin.education.SubjectPageRequestVM;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    List<Subject> findByLevel(Integer level);
    Subject findById(Integer id);

    List<Subject> findAll();

    Integer levelBySubjectId(Integer id);

    Page<Subject> page(SubjectPageRequestVM requestVM);
}
