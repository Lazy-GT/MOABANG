package com.self.roomescape.controller;

import com.self.roomescape.config.JwtTokenProvider;
import com.self.roomescape.entity.Report;
import com.self.roomescape.entity.User;
import com.self.roomescape.repository.ReportRepository;
import com.self.roomescape.repository.UserRepository;
import com.self.roomescape.request.ReportRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Api("신고 api")
@RequestMapping("/report")
@RestController
public class ReportContorller {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "신고 등록 및 취소", notes = "신고 등록 및 취소, category 리뷰  0, 게시글 1, 댓글 2")
    @PostMapping("/create")
    public ResponseEntity<?> createList(@RequestBody ReportRequest reportRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> cur_user = userRepository.findByEmail(useremail);

        if (!cur_user.isPresent()) {
            return new ResponseEntity<>("해당 유저 없음", HttpStatus.BAD_REQUEST);
        }

        // uid, category, target_id 3개로 찾아야함.
        Optional<Report> report = reportRepository.findByUserUidAndCategoryAndTargetId(cur_user.get().getUid(), reportRequest.getCategory(), reportRequest.getTarget_id());
        if (!report.isPresent()) { // 없으면 등록
            reportRepository.save(Report.builder().category(reportRequest.getCategory()).targetId(reportRequest.getTarget_id()).user(cur_user.get()).reason(reportRequest.getReason()).build());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } // 있으면 삭제
        else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
