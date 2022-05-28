package com.self.roomescape.controller;

import com.self.roomescape.config.JwtTokenProvider;
import com.self.roomescape.entity.Community;
import com.self.roomescape.entity.Review;
import com.self.roomescape.entity.User;
import com.self.roomescape.repository.CommunityRepository;
import com.self.roomescape.repository.ReviewRepository;
import com.self.roomescape.entity.User;
import com.self.roomescape.repository.ThemeRepository;
import com.self.roomescape.repository.UserRepository;
import com.self.roomescape.repository.mapping.MyPageReviewListMapping;
import com.self.roomescape.repository.mapping.MyPageTidMapping;
import com.self.roomescape.repository.mapping.ThemeListMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.MyCommuAndReviewResDTO;
import response.MyThemeDetailResDTO;
import response.ThemeDetailResDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@Api(tags = {"마이페이지 Api"})
@RequestMapping("/mypage")
public class UserPageController {
    /*
    1.
    보낼거:  없음(헤더에 토큰만)
    받을거: 내가 좋아요 한 테마 리스트
    - 리턴값은 /cafe/theme/list랑 동일하게
    */
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CommunityRepository communityRepository;

    @GetMapping("/theme/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "유저가 좋아요 한 테마 리스트", notes = " 리턴값은 /cafe/theme/list랑 동일하며 유저 인증 실패시 403Error 리턴 ")
    public ResponseEntity<?> UserLikeList(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);
        if (!user.isPresent()) {
            return new ResponseEntity<>("해당 유저 없음", HttpStatus.FORBIDDEN);
        }
        List<ThemeDetailResDTO> themeDetailResDTOList = new ArrayList<>();
        List<ThemeListMapping> themeList = themeRepository.findThemeFetch(user.get().getUid());
        for (int i = 0; i < themeList.size(); i++) {
            ThemeDetailResDTO tmp = new ThemeDetailResDTO();
            tmp.setActivity(themeList.get(i).getActivity());
            tmp.setCid(themeList.get(i).getCid());
            tmp.setCname(themeList.get(i).getCname());
            tmp.setUrl(themeList.get(i).getUrl());
            tmp.setDifficulty(themeList.get(i).getDifficulty());
            tmp.setDescription(themeList.get(i).getDescription());
            tmp.setGenre(themeList.get(i).getGenre());
            tmp.setImg(themeList.get(i).getImg());
            tmp.setIsland(themeList.get(i).getIsland());
            tmp.setSi(themeList.get(i).getSi());
            tmp.setGrade(themeList.get(i).getGrade());
            tmp.setRplayer(themeList.get(i).getRplayer());
            tmp.setTid(themeList.get(i).getTid());
            tmp.setTime(themeList.get(i).getTime());
            tmp.setType(themeList.get(i).getType());
            tmp.setTname(themeList.get(i).getTname());
            tmp.setCount(themeList.get(i).getCount());
            tmp.setIslike(true);

            String data = tmp.getRplayer();
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

    /*
    2.
    보낼거: 없음(헤더에 토큰만)
    받을거: 내가 리뷰 남긴 테마의 tid 리스트
    */
    @GetMapping("/theme/tlist")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "유저가 리뷰 남긴 테마의 tid 리스트", notes = " 리턴값은 tid들의 List로 보내지며 리뷰 남긴게 없다면 빈 배열이 반환 ")
    public ResponseEntity<?> UserTidList(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);
        if (!user.isPresent()) {
            return new ResponseEntity<>("해당 유저 없음", HttpStatus.BAD_REQUEST);
        }
        List<MyPageTidMapping> myPageTidResponses = themeRepository.findTidFetch(user.get().getUid());
        return new ResponseEntity<>(myPageTidResponses, HttpStatus.OK);
    }

    /*
    3.
    보낼거: 없음(헤더에 토큰만)
    받을거: 내가 리뷰 남긴 테마 리스트
- 리턴값은 /cafe/theme/list랑 동일하게
     */
    @GetMapping("/theme/review")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "유저가 리뷰 남긴 테마 리스트", notes = " 리턴값은 /cafe/theme/list랑 동일하며 유저 인증 실패시 403Error 리턴 ")
    public ResponseEntity<?> UserReviewList(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);
        if (!user.isPresent()) {
            return new ResponseEntity<>("해당 유저 없음", HttpStatus.BAD_REQUEST);
        }
        List<MyThemeDetailResDTO> myThemeDetailResDTOS = new ArrayList<>();
        List<ThemeListMapping> themeList = themeRepository.findThemeAndReviewFetch(user.get().getUid());
        for (int i = 0; i < themeList.size(); i++) {
            MyThemeDetailResDTO tmp = new MyThemeDetailResDTO();
            tmp.setCname(themeList.get(i).getCname());
            tmp.setImg(themeList.get(i).getImg());
            tmp.setTid(themeList.get(i).getTid());
            tmp.setTname(themeList.get(i).getTname());
            tmp.setPlayDate(themeList.get(i).getPlayDate());
            tmp.setIsSuccess(themeList.get(i).getIsSuccess());
            tmp.setPlayer(themeList.get(i).getPlayer());
            tmp.setRating(themeList.get(i).getGrade());


            myThemeDetailResDTOS.add(tmp);

        }
        return new ResponseEntity<>(myThemeDetailResDTOS, HttpStatus.OK);
    }

    @GetMapping("/reviewcommunity")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "유저가 남긴 리뷰, 글", notes = "유저가 남긴 리뷰, 글 ")
    public ResponseEntity<?> UserReviewCommunityList(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);
        if (!user.isPresent()) {
            return new ResponseEntity<>("해당 유저 없음", HttpStatus.BAD_REQUEST);
        }

        //커뮤니티, 리뷰 부분
        List<MyPageReviewListMapping> reviewList = reviewRepository.findByReviewData(user.get().getUid());
        List<Community> communityList = communityRepository.findAllByUser_Uid(user.get().getUid());
        Map<String, List<?>> map = new HashMap<>();
        map.put("reviewList", reviewList);
        map.put("communityList", communityList);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
