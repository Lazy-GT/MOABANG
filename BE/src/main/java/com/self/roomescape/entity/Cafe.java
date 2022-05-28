package com.self.roomescape.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "cafe")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;

//    private int oid;

    private String cname;
    private String cphone;
    private String url;
    private String time;
    private String img;
    private String location;
    private String lat;
    private String lon;
    @Column(name = "do")
    private String island;
    private String si;
}
