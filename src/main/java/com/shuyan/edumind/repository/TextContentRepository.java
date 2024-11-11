package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.TextContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextContentRepository extends JpaRepository<TextContent,Integer> {
}
