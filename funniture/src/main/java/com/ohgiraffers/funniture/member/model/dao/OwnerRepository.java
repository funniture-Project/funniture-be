package com.ohgiraffers.funniture.member.model.dao;

import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerInfoEntity, Long> {
    Optional<OwnerInfoEntity> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    boolean existsByStoreNoAndMemberIdNot(String storeNo, String memberId);
}
