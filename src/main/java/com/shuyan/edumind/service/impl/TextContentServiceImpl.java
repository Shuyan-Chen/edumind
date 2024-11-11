package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.TextContent;
import com.shuyan.edumind.repository.TextContentRepository;
import com.shuyan.edumind.service.TextContentService;
import com.shuyan.edumind.utility.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TextContentServiceImpl implements TextContentService {

    private final TextContentRepository textContentRepository;

    @Autowired
    public TextContentServiceImpl(TextContentRepository textContentRepository) {
        this.textContentRepository = textContentRepository;
    }

    @Override
    public TextContent findById(Integer id) { return textContentRepository.findById(id).get();}

    @Override
    public void save(TextContent textContent) {
        textContentRepository.save(textContent);
    }

    @Override
    public <T, R> TextContent jsonConvertInsert(List<T> list, Date now, Function<? super T, ? extends R> mapper) {
        String frameTextContent = null;
        if (null == mapper) {
            frameTextContent = JsonUtil.toJsonStr(list);
        } else {
            List<R> mapList = list.stream().map(mapper).collect(Collectors.toList());
            frameTextContent = JsonUtil.toJsonStr(mapList);
        }
        TextContent textContent = new TextContent(frameTextContent, now);
        return textContent;
    }

    @Override
    public <T, R> TextContent jsonConvertUpdate(TextContent textContent, List<T> list, Function<? super T, ? extends R> mapper) {
        String frameTextContent = null;
        if (null == mapper) {
            frameTextContent = JsonUtil.toJsonStr(list);
        } else {
            List<R> mapList = list.stream().map(mapper).collect(Collectors.toList());
            frameTextContent = JsonUtil.toJsonStr(mapList);
        }
        textContent.setContent(frameTextContent);
        return textContent;
    }



}
