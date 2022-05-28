package response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


@Getter
@Setter
public class ThemeDetailResDTO {
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
    private boolean islike;
    private int min_player;
    private int max_player;
    @JsonFormat(pattern = "yyyy.MM.dd")
    @Temporal(TemporalType.DATE)
    private Date playDate;
    private boolean compare;

}
