package com.self.roomescape.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private Long rid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private UserInfo userInfo;
    private int tid;
    private float rating;

    private int isSuccess;
    private int hint;
    private int clearTime;
    private int player;
    private int recPlayer;
    private String active;
    private int chaegamDif;

    @JsonFormat(pattern = "yyyy.MM.dd")
    @Temporal(TemporalType.DATE)
    private Date playDate;

    @JsonFormat(pattern = "yyyy.MM.dd")
    @CreationTimestamp
    private Timestamp regDate;
    private String content;

}
