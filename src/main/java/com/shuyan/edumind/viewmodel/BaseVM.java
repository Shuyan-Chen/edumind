package com.shuyan.edumind.viewmodel;

import com.shuyan.edumind.utility.ModelMapperSingle;
import org.modelmapper.ModelMapper;


public class BaseVM {

    protected static ModelMapper modelMapper = ModelMapperSingle.Instance();

    public static ModelMapper getModelMapper() {
        return modelMapper;
    }

    public static void setModelMapper(ModelMapper modelMapper) {
        BaseVM.modelMapper = modelMapper;
    }
}
