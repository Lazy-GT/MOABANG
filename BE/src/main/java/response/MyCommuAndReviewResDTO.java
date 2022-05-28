package response;

import com.self.roomescape.entity.Community;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MyCommuAndReviewResDTO {
    List<MyThemeDetailResDTO> reviewThemeRes;
    Map<String, List<?>> communityRes;
}
