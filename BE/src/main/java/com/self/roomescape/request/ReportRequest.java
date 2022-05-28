package com.self.roomescape.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {
    private int target_id;
    private int category;
    private String reason;
}
