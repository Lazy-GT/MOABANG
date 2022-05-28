package response;

import lombok.Data;

import javax.persistence.*;

@Data
public class ThemeListResponse {
    private int tid;
    private int cid;
    private int difficulty;
    private String tname;
    private String img;
    private String description;
    private String rplayer;
    private String time;
    private String genre;
    private String type;
    private float grade;
//    private int reviewCnt;
    private String activity;
}
