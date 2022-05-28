package com.self.roomescape.controller;

import com.self.roomescape.config.JwtTokenProvider;
import com.self.roomescape.entity.Compare;
import com.self.roomescape.entity.Theme;
import com.self.roomescape.entity.User;
import com.self.roomescape.entity.UserLike;
import com.self.roomescape.repository.CompareRepository;
import com.self.roomescape.repository.ThemeRepository;
import com.self.roomescape.repository.UserRepository;
import com.self.roomescape.repository.mapping.ThemeListMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.CompareResDTO;
import response.ThemeDetailResDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = {"비교하기 api"})
@RequestMapping("/compare")
public class CompareController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private CompareRepository compareRepository;

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "유저 비교 리스트 추가 및 삭제", notes = "비교 리스트 목록 추가 및 비교 리스트 목록 삭제")
    @GetMapping("/{tid}")
    public ResponseEntity<?> addCompare(@PathVariable int tid, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        User user = userRepository.findUserByEmail(useremail);
        Theme theme = themeRepository.findThemesByTid(tid);
        Optional<Compare> compare = compareRepository.findByUserAndTheme(user, theme);
        if (compare.isPresent()) {
            compareRepository.delete(compare.get());
            return new ResponseEntity<>("해당 테마 비교 삭제 success", HttpStatus.OK);
        } else {
            if (user != null && theme != null) {
                Compare addCompare = Compare.builder().theme(theme).user(user).build();
                compareRepository.save(addCompare);
                return new ResponseEntity<>("해당 테마 비교 추가 success", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("fail", HttpStatus.OK);
            }
        }
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "string", paramType = "header")})
    @ApiOperation(value = "유저 비교 리스트 목록 반환", notes = "비교 리스트 목록 반환 유저 인증 실패시 403")
    @GetMapping("/list")
    public ResponseEntity<?> getCompareList(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String useremail = jwtTokenProvider.getUserPk(token);
        Optional<User> user = userRepository.findByEmail(useremail);
        if (!user.isPresent()) {
            return new ResponseEntity<>("해당 유저 없음", HttpStatus.FORBIDDEN);
        }
        List<CompareResDTO> compareResDTOS = new ArrayList<>();
        List<ThemeListMapping> themeList = compareRepository.findThemeFetch(user.get().getUid());
        for (int i = 0; i < themeList.size(); i++) {
            CompareResDTO tmp = new CompareResDTO();
            if (themeList.get(i).getActivity().equals("많음")) {
                tmp.setActivity(5);
            } else if (themeList.get(i).getActivity().equals("보통")) {
                tmp.setActivity(3);
            } else {
                tmp.setActivity(1);
            }
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
            tmp.setLat(themeList.get(i).getLat());
            tmp.setLon(themeList.get(i).getLon());
            tmp.setCompare(true);
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
            compareResDTOS.add(tmp);

        }
        return new ResponseEntity<>(compareResDTOS, HttpStatus.OK);
    }

}
