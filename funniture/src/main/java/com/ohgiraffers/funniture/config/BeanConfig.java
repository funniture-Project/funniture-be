package com.ohgiraffers.funniture.config;

import org.apache.ibatis.annotations.Mapper;
import org.modelmapper.ModelMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.ohgiraffers.funniture", annotationClass = Mapper.class)
@ComponentScan(basePackages = "com.ohgiraffers.funniture") // funniture 하위 다 읽을 수 있게 설정
public class BeanConfig {

    @Bean // build.gradle에 의존성 넣고 추가 설정 해줘야함
    public ModelMapper modelMapper () {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration() // 설정 가져오기
                // private 필드에 접근하기 위한 설정
                // 가져온 설정을 다시 바꿔주기
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                // DTO 필드와 Entity 필드 매칭 가능 여부 설정
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }
}
