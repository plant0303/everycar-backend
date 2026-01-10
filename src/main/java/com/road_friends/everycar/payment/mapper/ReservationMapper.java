package com.road_friends.everycar.payment.mapper;

import com.road_friends.everycar.payment.dto.PaymentCheckRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
    void insertReservation(PaymentCheckRequest request);
}