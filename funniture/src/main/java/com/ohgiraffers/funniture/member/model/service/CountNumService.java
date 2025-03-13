package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.CountEntity;
import com.ohgiraffers.funniture.member.model.dao.CountRepository;
import com.ohgiraffers.funniture.member.model.dto.ConnectCountDTO;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountNumService {

    private final ModelMapper modelMapper;
    private final CountRepository countRepository;

    @Transient
    public void updateCount(String role) {
        LocalDate today = LocalDate.now();

        try{
            CountEntity countEntity = countRepository.findTodayData(today,role);

            if (countEntity == null){
                CountEntity count = new CountEntity(today,role.toString(),1);
                countRepository.save(count);
            } else {
                countEntity = countEntity.toBuilder()
                        .connectCount(countEntity.getConnectCount() + 1)
                        .build();

                countRepository.save(countEntity);
            }

        }catch (Exception e){
            System.out.println("저장중 에러 e = " + e);
        }
    }

    public List<ConnectCountDTO> getThisMonth() {
        Integer month = LocalDate.now().getMonthValue();
        Integer year = LocalDate.now().getYear();

        // 월을 두 자릿수로 포맷
        String formattedMonth = String.format("%02d", month);

        String yearMonth = year +"-"+formattedMonth;

        List<CountEntity> connectList = countRepository.getCountByMonth(yearMonth);

        if (connectList.isEmpty()) {
            return null;
        }

        List<ConnectCountDTO> countDTOList = connectList.stream().map(item -> modelMapper.map(item, ConnectCountDTO.class))
                                                                    .collect(Collectors.toList());

        return countDTOList;
    }
}
