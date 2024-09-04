package com.example.hotelboard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    // ManyToOne 관계를 설정하며, 외래 키를 정의하는 어노테이션
    @ManyToOne
    @JoinColumn(name = "member_id") // 외래 키 설정
    private Users memberId; // 사용자의 정보와 연결된 외래 키

    @Column
    private Date checkIn;

    @Column
    private Date checkOut;

    @Column
    private Long roomId;

    @Column
    private String inputName;

    @Column
    private String memo;

    @Column
    private Date reservedAt;

    @Column(name = "reserveProcess")
    @Enumerated(EnumType.STRING)
    private Process reserveProcess;

    public Reservations(Users user, Date checkIn, Date checkOut, Long roomId, String inputName, String memo, Process reserveProcess) {
        this.reserveProcess = Process.예약완료; // 기본값 설정
    }

    // 추가된 생성자
    public Reservations(Users user, Date checkIn, Date checkOut, Long roomId, String inputName, String memo, Date date, Process reserveProcess) {
        this.memberId = user;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomId = roomId;
        this.inputName = inputName;
        this.memo = memo;
        this.reservedAt = date;
        this.reserveProcess = reserveProcess;
    }


    // Process Enum 타입 정의
    public enum Process {
        예약완료
    }
}
