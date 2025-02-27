package com.ohgiraffers.funniture.favorite.model.service;

import com.ohgiraffers.funniture.favorite.entity.FavoriteEntity;
import com.ohgiraffers.funniture.favorite.model.dao.FavoriteDetailInfoRepository;
import com.ohgiraffers.funniture.favorite.model.dao.FavoriteListRepository;
import com.ohgiraffers.funniture.favorite.model.dto.FavoriteDetailInfoDTO;
import com.ohgiraffers.funniture.favorite.model.dto.FavoriteListDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteDetailInfoRepository favoriteDetailInfoRepository;
    private final FavoriteListRepository favoriteListRepository;
    private final ModelMapper modelMapper;

    public List<FavoriteDetailInfoDTO> findListById(String memberId) {
        List<FavoriteEntity> favoriteDetailInfoList = favoriteDetailInfoRepository.findAllByMemberId(memberId);

        return favoriteDetailInfoList.stream()
                .map(item -> modelMapper.map(item, FavoriteDetailInfoDTO.class))
                .collect(Collectors.toList());
    }

    public List<FavoriteListDTO> getFavoriteList(String memberId) {
        List<FavoriteEntity> favoriteList = favoriteListRepository.findAllByMemberId(memberId);

        return favoriteList.stream().map(item -> modelMapper.map(item, FavoriteListDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteData(String memberId, List<String> deleteData) {
        System.out.println("memberId = " + memberId);
        System.out.println("deleteData = " + deleteData);

        try{
            favoriteListRepository.removeAllByMemIdANDProductNoIn(memberId,deleteData);
        } catch (Exception e){
            System.out.println("e = " + e);
            return false;
        }

        return true;
    }

    @Transactional
    public boolean addData(List<Map<String, String>> addFavoriteList) {

        try {
            List<FavoriteEntity> favoriteEntityList = addFavoriteList.stream().map(item -> modelMapper.map(item, FavoriteEntity.class))
                                                        .collect(Collectors.toList());
            System.out.println("favoriteEntityList = " + favoriteEntityList);

            favoriteListRepository.saveAll(favoriteEntityList);
        } catch (Exception e){
            System.out.println("e = " + e);

            return false;
        }

        return true;
    }
}
