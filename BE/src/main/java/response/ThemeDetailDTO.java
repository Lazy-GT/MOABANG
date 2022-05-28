package response;

import lombok.*;


@Data
@AllArgsConstructor
public class ThemeDetailDTO {
    private int tid;
    private int cid;
    private String tname;
    private String img;
    private String description;
    private String rplayer;
    private String time;
    private String genre;
    private String type;
    private int difficulty;
    private float grade;
    private String activity;
    private String cname;
    private String url;
    private String island;
    private String si;
    private long count;

}
