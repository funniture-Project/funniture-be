package com.ohgiraffers.funniture.favorite.model.service;

import com.ohgiraffers.funniture.favorite.entity.FavoriteEntity;
import com.ohgiraffers.funniture.favorite.model.dao.FavoriteRepository;
import com.ohgiraffers.funniture.favorite.model.dto.FavoriteDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ModelMapper modelMapper;

    public List<FavoriteDTO> findListById(String memberId) {
        List<FavoriteEntity> favoriteList = favoriteRepository.findAllByMemberId(memberId);

        return favoriteList.stream()
                .map(item -> modelMapper.map(item, FavoriteDTO.class))
                .collect(Collectors.toList());
    }
}
