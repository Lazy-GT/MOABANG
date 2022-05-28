package com.self.roomescape.controller;

import com.self.roomescape.entity.*;
import com.self.roomescape.repository.*;
import com.self.roomescape.repository.mapping.ThemeListMapping;

import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.ThemeDetailDTO;
import response.ThemeDetailResDTO;
import response.ThemeDetailResponse;

import response.ThemeListResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cafe")
@Api(tags = {"카페 및 테마 Api"})
public class CafeController {
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLikeRepository userLikeRepository;

    @Value("${spring.jwt.secret}")
    private String secretKey;


    @ApiOperation(value = "카페 전체 목록", notes = "카페 전체 목록을 반환.")
    @GetMapping("/list")
    public ResponseEntity<?> findList() {
        List<Cafe> cafeList = cafeRepository.findAll();


        return new ResponseEntity<>(cafeList, HttpStatus.OK);
    }

    @ApiOperation(value = "해당 카페의 테마 검색", notes = "해당 tid에 해당하는 테마 정보를 반환")
    @GetMapping("/theme/{cid}")
    public ResponseEntity<?> findCafeTheme(@PathVariable("cid") int cid) {
        List<Theme> themeList = themeRepository.findByCafeCid(cid);

        List<ThemeListResponse> themeListResponses = new ArrayList<>();

        for (Theme res : themeList
        ) {
            ThemeListResponse temp = new ThemeListResponse();
            temp.setTname(res.getTname());
            temp.setTime(res.getTime());
            temp.setTid(res.getTid());
            temp.setType(res.getType());
            temp.setGrade(res.getGrade());
            temp.setRplayer(res.getRplayer());
            temp.setImg(res.getImg());
            temp.setGenre(res.getGenre());
            temp.setDescription(res.getDescription());
            temp.setDifficulty(res.getDifficulty());
            temp.setCid(res.getCafe().getCid());
            temp.setActivity(res.getActivity());
            themeListResponses.add(temp);
        }


        return new ResponseEntity<>(themeListResponses, HttpStatus.OK);
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "테마 전체 목록", notes = "테마 전체 목록을 카페 이름 및 URL과 같이 반환")
    @GetMapping("/theme/list")
    public ResponseEntity<?> findAllTheme(HttpServletRequest request) {
        List<ThemeDetailDTO> themeDetailDTOList = cafeRepository.getthemeList();
        List<ThemeDetailResDTO> themeDetailResDTOList = new ArrayList<>();


        if (request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").substring(7);
            Optional<User> user = userRepository.findByEmail(this.getUserPk(token));
            if (!user.isPresent()) {
                return new ResponseEntity<>("회원 정보가 없음", HttpStatus.BAD_REQUEST);
            }


            // user 좋아요 정보 불러오는 부분

            // logic 1. 얻은 uid를 통해서 like 테이블에 좋아요한 tid가 있는지 확인.
            Optional<List<UserLike>> likeList = userLikeRepository.findUserLikeByUser(user.get());

            if (likeList.get().size() != 0) {
                for (int i = 0; i < themeDetailDTOList.size(); i++) {
                    ThemeDetailResDTO tmp = new ThemeDetailResDTO();
                    tmp.setActivity(themeDetailDTOList.get(i).getActivity());
                    tmp.setCid(themeDetailDTOList.get(i).getCid());
                    tmp.setCname(themeDetailDTOList.get(i).getCname());
                    tmp.setUrl(themeDetailDTOList.get(i).getUrl());
                    tmp.setDifficulty(themeDetailDTOList.get(i).getDifficulty());
                    tmp.setDescription(themeDetailDTOList.get(i).getDescription());
                    tmp.setGenre(themeDetailDTOList.get(i).getGenre());
                    tmp.setImg(themeDetailDTOList.get(i).getImg());
                    tmp.setIsland(themeDetailDTOList.get(i).getIsland());
                    tmp.setSi(themeDetailDTOList.get(i).getSi());
                    tmp.setGrade(themeDetailDTOList.get(i).getGrade());
                    tmp.setRplayer(themeDetailDTOList.get(i).getRplayer());
                    tmp.setTid(themeDetailDTOList.get(i).getTid());
                    tmp.setTime(themeDetailDTOList.get(i).getTime());
                    tmp.setType(themeDetailDTOList.get(i).getType());
                    tmp.setTname(themeDetailDTOList.get(i).getTname());
                    tmp.setCount(themeDetailDTOList.get(i).getCount());
                    tmp.setIslike(false);
                    String data = themeDetailDTOList.get(i).getRplayer();
                    String[] srr = data.split("~");
                    if (srr.length != 1) {
                        tmp.setMin_player(Integer.parseInt(srr[0]));
                        tmp.setMax_player(Integer.parseInt(srr[1]));
                    } else {
                        tmp.setMin_player(Integer.parseInt(srr[0]));
                        tmp.setMax_player(Integer.parseInt(srr[0]));
                    }


                    for (int j = 0; j < likeList.get().size(); j++) {
                        if (themeDetailDTOList.get(i).getTid() == likeList.get().get(j).getTheme().getTid()) {
                            tmp.setIslike(true);
                        }
                    }
                    themeDetailResDTOList.add(tmp);
                }
                return new ResponseEntity<>(themeDetailResDTOList, HttpStatus.OK);
            }
        }
        for (int i = 0; i < themeDetailDTOList.size(); i++) {
            ThemeDetailResDTO tmp = new ThemeDetailResDTO();
            tmp.setActivity(themeDetailDTOList.get(i).getActivity());
            tmp.setCid(themeDetailDTOList.get(i).getCid());
            tmp.setCname(themeDetailDTOList.get(i).getCname());
            tmp.setUrl(themeDetailDTOList.get(i).getUrl());
            tmp.setDifficulty(themeDetailDTOList.get(i).getDifficulty());
            tmp.setDescription(themeDetailDTOList.get(i).getDescription());
            tmp.setGenre(themeDetailDTOList.get(i).getGenre());
            tmp.setImg(themeDetailDTOList.get(i).getImg());
            tmp.setIsland(themeDetailDTOList.get(i).getIsland());
            tmp.setSi(themeDetailDTOList.get(i).getSi());
            tmp.setGrade(themeDetailDTOList.get(i).getGrade());
            tmp.setRplayer(themeDetailDTOList.get(i).getRplayer());
            tmp.setTid(themeDetailDTOList.get(i).getTid());
            tmp.setTime(themeDetailDTOList.get(i).getTime());
            tmp.setType(themeDetailDTOList.get(i).getType());
            tmp.setTname(themeDetailDTOList.get(i).getTname());
            tmp.setCount(themeDetailDTOList.get(i).getCount());
            tmp.setCompare(false);
            tmp.setIslike(false);

            String data = themeDetailDTOList.get(i).getRplayer();
            String[] srr = data.split("~");
            if (srr.length != 1) {
                tmp.setMin_player(Integer.parseInt(srr[0]));
                tmp.setMax_player(Integer.parseInt(srr[1]));
            } else {
                tmp.setMin_player(Integer.parseInt(srr[0]));
                tmp.setMax_player(Integer.parseInt(srr[0]));
            }
            themeDetailResDTOList.add(tmp);
        }
        return new ResponseEntity<>(themeDetailResDTOList, HttpStatus.OK);
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "테마 상세 정보", notes = "해당 Tid를 통해서 테마 상세 정보 및 댓글 목록을 반환한다.")
    @GetMapping("/theme/detail/{tid}")
    public ResponseEntity<?> findThemeDetail(@PathVariable int tid, HttpServletRequest request) throws Exception {

        ThemeDetailResponse themeDetailResponse = new ThemeDetailResponse();
        ThemeDetailDTO themeDetailDTO = cafeRepository.gettheme(tid);
        themeDetailResponse.setThemeDetailDTO(themeDetailDTO);
        Optional<List<Review>> rlist = reviewRepository.findByTid(tid);
        if (request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").substring(7);
            Optional<User> user = userRepository.findByEmail(this.getUserPk(token));
            if (user.isPresent()) {
                Optional<UserLike> userLike = userLikeRepository.findUserLikeByUserAndTheme(user.get(), themeRepository.findThemesByTid(tid));
                if (userLike.isPresent()) {
                    themeDetailResponse.setIslike(true);
                } else {
                    themeDetailResponse.setIslike(false);
                }
            }
        }

        if (!rlist.isPresent()) {
            return new ResponseEntity<>(themeDetailResponse, HttpStatus.OK);
        }

        themeDetailResponse.setReviewList(rlist.get());
        return new ResponseEntity<>(themeDetailResponse, HttpStatus.OK);
    }
}
