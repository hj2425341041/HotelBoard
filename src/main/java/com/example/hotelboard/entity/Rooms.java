package com.example.hotelboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column
    private Long roomNum;

    @Column(name="roomType")
    @Enumerated(EnumType.STRING)
    private Type roomType;


    //객실 유형 Enum 타입 정의
    public enum Type {
        Sweet, Deluxe, Standard
    }
}
