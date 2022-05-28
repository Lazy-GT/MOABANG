package com.self.roomescape.controller;

import com.self.roomescape.config.JwtTokenProvider;
import com.self.roomescape.entity.*;
import com.self.roomescape.repository.*;
import com.self.roomescape.repository.mapping.CommentListMapping;
import com.self.roomescape.repository.mapping.CommunityMapping;
import com.self.roomescape.request.CommentRequest;
import com.self.roomescape.request.CommentUpdateRequest;
import com.self.roomescape.request.RecruitCreateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import response.CommunityRes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community")
@Api(tags = {"커뮤니티 및 댓글 Api"})
public class CommunityController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final CommunityRepository communityRepository;

    private final CommentRepository commentRepository;


    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "글 쓰기", notes = "글 쓰기")
    @PostMapping("/write")
    public ResponseEntity<?> createRecruit(@RequestBody RecruitCreateRequest recruitCreateRequest, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        User user = userRepository.findUserByEmail(useremail);
        Community community = new Community();
        community.setUser(user);
        community.setTitle(recruitCreateRequest.getTitle());
        community.setContent(recruitCreateRequest.getContent());
        community.setHeader(recruitCreateRequest.getHeader());
        communityRepository.save(community);
        if (community.getRid() != 0) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "글 수정", notes = "글 수정")
    @PutMapping("/update/{rid}")
    public ResponseEntity<?> updateRecruit(@RequestBody RecruitCreateRequest recruitCreateRequest, @PathVariable long rid, HttpServletRequest request) {
        Optional<Community> recruit = communityRepository.findById(rid);
        if (recruit.isPresent()) {
            String token = jwtTokenProvider.resolveToken(request);
            String useremail = jwtTokenProvider.getUserPk(token);
            User user = userRepository.findUserByEmail(useremail);
            if (recruit.get().getUser().getEmail().equals(user.getEmail())) {
                recruit.get().setContent(recruitCreateRequest.getContent());
                recruit.get().setTitle(recruitCreateRequest.getTitle());
                recruit.get().setHeader(recruitCreateRequest.getHeader());
                communityRepository.save(recruit.get());
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "글 삭제", notes = "글 삭제")
    @DeleteMapping("/delete/{rid}")
    public ResponseEntity<?> deleteRecruit(@PathVariable long rid, HttpServletRequest request) {
        Optional<Community> recruit = communityRepository.findById(rid);
        if (recruit.isPresent()) {
            String token = jwtTokenProvider.resolveToken(request);
            String useremail = jwtTokenProvider.getUserPk(token);
            User user = userRepository.findUserByEmail(useremail);
            if (recruit.get().getUser().getEmail().equals(user.getEmail())) {
                communityRepository.delete(recruit.get());
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "글 읽기", notes = "글 읽기")
    @GetMapping("/read/{rid}")
    public ResponseEntity<?> getRecruit(@PathVariable long rid) {
        Optional<Community> recruit = communityRepository.findById(rid);
        if (recruit.isPresent()) {
            return new ResponseEntity<>(recruit.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "글 리스트 읽기", notes = "글 리스트 읽기")
    @GetMapping("/read/list")
    public ResponseEntity<?> getRecruitList() {
//        List<Community> communityList = communityRepository.findAllByOrderByCreateDateDesc();
        List<CommunityMapping> communityList = communityRepository.findCustomList();
        return new ResponseEntity<>(communityList, HttpStatus.OK);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "작성일 기준 5개", notes = "작성일 기준 5개")
    @GetMapping("/new")
    public ResponseEntity<?> getRecruit5() {
        List<Community> communityList = communityRepository.findTop5ByOrderByCreateDateDesc();
        return new ResponseEntity<>(communityList, HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 목록 불러오기", notes = " community_id를 입력 시 해당 게시글의 댓글 목록을 반환, 성공시 200, 실패 시 에러 메세지와 401리턴")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/comment/list/{community_id}")
    public ResponseEntity<?> getCommentList(@PathVariable long community_id, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);

        if (!user.isPresent()) {
            return new ResponseEntity<>("회원 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<CommunityRes> communityRes = new ArrayList<>();
        List<CommentListMapping> commentList = commentRepository.findByCustomCommentList(community_id);

        for (CommentListMapping c : commentList
        ) {
            CommunityRes temp = new CommunityRes();
            temp.setCommunityId(c.getCommunityId());
            temp.setContent(c.getContent());
            temp.setCoid(c.getCoid());
            temp.setRegDate(c.getRegDate());
            temp.setUserName(c.getUserName());
            temp.setUserProfile(c.getUserProfile());
            temp.setUid(c.getUid());
            temp.setEmail(c.getEmail());
            temp.setReportCnt(c.getReportCnt());

            communityRes.add(temp);
        }

        return new ResponseEntity<>(communityRes, HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 생성", notes = " RequestBody에 해당되는 목록 입력 후, 성공시 200, 실패 시 에러 메세지와 401리턴")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/comment/create")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);

        if (!user.isPresent()) {
            return new ResponseEntity<>("회원 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        commentRepository.save(Comment.builder().content(commentRequest.getContent()).user(user.get()).communityId(commentRequest.getCommunityId()).build());

        return new ResponseEntity<>("댓글 달기 ok", HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 수정", notes = "해당 댓글의 uid와 token의 uid가 같으며 변경사항 반영, 성공시 200, 실패 시 에러 메세지와 401리턴")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PutMapping("/comment/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);

        if (!user.isPresent()) {
            return new ResponseEntity<>("회원 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Optional<Comment> comment = commentRepository.findByCoid(commentUpdateRequest.getCoid());
        if (!comment.isPresent()) {
            return new ResponseEntity<>("해당 coid가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if (comment.get().getUser().getUid() != user.get().getUid()) {
            return new ResponseEntity<>("댓글의 uid와 해당 유저의 uid가 다릅니다.", HttpStatus.BAD_REQUEST);
        }

        comment.get().setContent(commentUpdateRequest.getContent());
        commentRepository.save(comment.get());

        return new ResponseEntity<>("댓글 수정 ok", HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 삭제", notes = " 댓글 삭제 성공시 200, 실패 시 에러 메세지와 401리턴")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @DeleteMapping("/comment/delete/{coid}")
    @Transactional
    public ResponseEntity<?> deleteComment(@PathVariable long coid, HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);

        if (!user.isPresent()) {
            return new ResponseEntity<>("회원 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Optional<Comment> comment = commentRepository.findByCoid(coid);
        if (!comment.isPresent()) {
            return new ResponseEntity<>("해당 coid가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if (comment.get().getUser().getUid() != user.get().getUid()) {
            return new ResponseEntity<>("댓글의 uid와 해당 유저의 uid가 다릅니다.", HttpStatus.BAD_REQUEST);
        }

        commentRepository.delete(comment.get());

        return new ResponseEntity<>("댓글 삭제 성공", HttpStatus.OK);
    }

}
