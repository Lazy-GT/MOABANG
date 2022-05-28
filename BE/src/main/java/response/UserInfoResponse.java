package response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class UserInfoResponse {
    private Long uid;
    private String nickname;
    private String email;
    private String p_img;
}
