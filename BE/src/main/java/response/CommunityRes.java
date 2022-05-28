package response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CommunityRes {
    private long coid;
    private String content;
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date regDate;
    private long uid;
    private long communityId;
    private String userProfile;
    private String userName;
    private String Email;
    private int reportCnt;
}
