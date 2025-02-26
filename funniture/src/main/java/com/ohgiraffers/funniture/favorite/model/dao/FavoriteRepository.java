package com.ohgiraffers.funniture.favorite.model.dao;

import com.ohgiraffers.funniture.favorite.entity.FavoriteEntity;

import java.util.List;

public interface FavoriteRepository{
    List<FavoriteEntity> findAllByMemberId(String memberId);
}
