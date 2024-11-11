package com.shuyan.edumind.service;
;

import com.shuyan.edumind.domain.TextContent;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface TextContentService  {

   TextContent findById(Integer id);
   void save(TextContent textContent);

    <T, R> TextContent jsonConvertInsert(List<T> list, Date now, Function<? super T, ? extends R> mapper);

    <T, R> TextContent jsonConvertUpdate(TextContent textContent, List<T> list, Function<? super T, ? extends R> mapper);

}
