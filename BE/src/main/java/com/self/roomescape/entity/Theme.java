package com.self.roomescape.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "theme")
public class Theme {

    @Id
    @Column(name = "tid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid")
    @ToString.Exclude
    private Cafe cafe;
    private int difficulty;
    private String tname;
    private String img;
    private String description;
    private String rplayer;
    private String time;
    private String genre;
    private String type;
    private float grade;
    private int reviewCnt;
    private String activity;

    @Transient
    private int count;

}
