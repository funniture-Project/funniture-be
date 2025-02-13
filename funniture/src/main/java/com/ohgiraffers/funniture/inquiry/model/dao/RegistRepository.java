package com.ohgiraffers.funniture.inquiry.model.dao;

import com.ohgiraffers.funniture.inquiry.entity.InquiryRegistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistRepository  extends JpaRepository<InquiryRegistEntity , String> {
}
