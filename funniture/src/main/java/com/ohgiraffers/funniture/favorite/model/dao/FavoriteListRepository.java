package com.ohgiraffers.funniture.favorite.model.dao;

import com.ohgiraffers.funniture.favorite.entity.FavoriteCombinedKey;
import com.ohgiraffers.funniture.favorite.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteEntity, FavoriteCombinedKey> {

    List<FavoriteEntity> findAllByMemberId(String memberId);

    @Modifying
    @Query(value = """
        DELETE
        FROM FavoriteEntity like 
        WHERE like.memberId = :memberId 
        and like.productNo IN :deleteData
    """)
    void removeAllByMemIdANDProductNoIn(@Param("memberId")String memberId,@Param("deleteData") List<String> deleteData);
}
