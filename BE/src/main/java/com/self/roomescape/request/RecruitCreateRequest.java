package com.self.roomescape.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecruitCreateRequest {
    @ApiModelProperty(value = "글 제목", required = true, example = "코난 하러 가실분 너만 오면 고")
    private String title;

    @ApiModelProperty(value = "글 내용", required = true, example = "카톡 주소 : @@")
    private String content;

    @ApiModelProperty(value = "말머리", required = true, example = "구인")
    private String header;
}
