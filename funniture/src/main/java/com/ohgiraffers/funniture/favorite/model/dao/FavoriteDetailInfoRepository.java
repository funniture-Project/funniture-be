package com.ohgiraffers.funniture.favorite.model.dao;

import com.ohgiraffers.funniture.favorite.entity.FavoriteEntity;

import java.util.List;

public interface FavoriteDetailInfoRepository {
    List<FavoriteEntity> findAllByMemberId(String memberId);
}
