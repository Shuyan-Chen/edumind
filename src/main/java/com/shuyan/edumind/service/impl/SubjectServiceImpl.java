package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.Subject;
import com.shuyan.edumind.repository.SubjectRepository;
import com.shuyan.edumind.service.SubjectService;
import com.shuyan.edumind.viewmodel.admin.education.SubjectPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject findById(Integer id) {
        return subjectRepository.findById(id).get();
    }

    @Override
    public int updateByIdFilter(Subject record) {
        return super.updateByIdFilter(record);
    }

    @Override
    public List<Subject> findByLevel(Integer level) {
        return subjectRepository.findByLevel(level);
    }

    @Override
    public List<Subject> findAll() {return subjectRepository.findAll();}

    @Override
    public Integer levelBySubjectId(Integer id) {
        return this.selectById(id).getLevel();
    }

    @Override
    public Page<Subject> page(SubjectPageRequestVM requestVM) {
        return subjectRepository.page(requestVM);
    }

}
