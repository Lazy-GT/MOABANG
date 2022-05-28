package com.self.roomescape.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {
    private long coid;
    private String content;
}
