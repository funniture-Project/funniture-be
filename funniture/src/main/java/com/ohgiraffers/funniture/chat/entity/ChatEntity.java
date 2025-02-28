package com.ohgiraffers.funniture.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_chatqalist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatEntity {

    @Id
    @Column(name = "chat_qa_no")
    private int chatQaNo;

    // 질문 레벨
    @Column(name = "chat_qa_level")
    private int chatQaLevel;

    // 질문 내용
    @Column(name = "chat_qa_qu_content")
    private String chatQaQuContent;

    // 질문 답변 내용
    @Column(name = "chat_qa_an_content")
    private String chatQaAnContent;

    // 하위질문 존재여부
    @Column(name = "next_qu_absence")
    private Boolean nextQuAbsence;

    // 관리자 연결 여부
    @Column(name = "admin_connect_absence")
    private Boolean adminConnectAbsence;

    // 상위 질문 번호
    @Column(name = "ref_qu_no")
    private Integer refQuNo;

}
