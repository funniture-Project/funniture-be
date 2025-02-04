package com.ohgiraffers.funniture.inquiry.model.dao;

import com.ohgiraffers.funniture.inquiry.entity.Inquiry;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, String> {

}
