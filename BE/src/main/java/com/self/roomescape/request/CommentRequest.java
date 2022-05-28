package com.self.roomescape.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommentRequest {
    private String content;
    private long communityId;
}
