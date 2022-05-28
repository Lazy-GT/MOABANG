package response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
public class MyThemeDetailResDTO {
    private int tid;
    private String tname;
    private String img;
    private int player;
    private double rating;
    private String cname;
    private int isSuccess;
    @JsonFormat(pattern = "yyyy.MM.dd")
    @Temporal(TemporalType.DATE)
    private Date playDate;
}
