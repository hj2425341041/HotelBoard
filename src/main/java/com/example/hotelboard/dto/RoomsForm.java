package com.example.hotelboard.dto;

import com.example.hotelboard.entity.Rooms;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class RoomsForm {
    private Long roomId;
    private Long roomNum;
    private Rooms.Type roomType;

//    public Rooms toEntity() {
//        return new Rooms(roomId, roomNum, roomType);
//    }
}
