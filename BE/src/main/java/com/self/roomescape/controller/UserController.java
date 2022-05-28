package com.self.roomescape.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.self.roomescape.repository.UserRepository;
import com.self.roomescape.service.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.self.roomescape.config.JwtTokenProvider;
import com.self.roomescape.entity.User;
import response.UserInfoResponse;
//import com.self.roomescape.service.UserService;
//import com.self.roomescape.auth.dto.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/user")
@Api(tags = {"snsLogin Api"})
public class UserController {
    //    private final UserService userService;
    private final HttpSession httpSession;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    private final KakaoService kakaoService;

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiImplicitParams({@ApiImplicitParam(name = "code", value = "access_token 입력", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "구글 로그인", notes = "AccessToken을 입력 받을 시 JWT Header에 ")
    @GetMapping(value = "/glogin")
    public @ResponseBody
    ResponseEntity<?> googleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("code");
        final String gurl = "https://www.googleapis.com/oauth2/v2/userinfo" + "?access_token=" + token;
        URL url = new URL(gurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            String userid = element.getAsJsonObject().get("id").getAsString();
            String email = element.getAsJsonObject().get("email").getAsString();
            String nickname = element.getAsJsonObject().get("name").getAsString();
            String profile_img = element.getAsJsonObject().get("picture").getAsString();
            User user = User.builder() // 구글에서 받아온 정보
                    .nickname(nickname)
                    .email(email)
                    .pimg(profile_img)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            Optional<User> user1 = userRepository.findByEmail(user.getEmail()); // db에서 받아온 정보
            List<String> list = Arrays.asList("ROLE_USER");
            if (!user1.isPresent()) {
                userRepository.save(user);
                String jwtToken = jwtTokenProvider.createToken(String.valueOf(user.getEmail()), list);
                response.addHeader("Authorization", "Bearer " + jwtToken);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                String jwtToken = jwtTokenProvider.createToken(String.valueOf(user1.get().getEmail()), list);
                response.addHeader("Authorization", "Bearer " + jwtToken);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }


        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "카카오 로그인", notes = "Header에 AccessToken을 입력 받을 시 JWT를 Header에 담아서 보냄 ")
    @PostMapping(value = "/klogin")
    public ResponseEntity<?> login(@RequestHeader Map<String, String> requestHeader, HttpServletResponse response) {
        logger.info("read header : " + requestHeader);
        logger.info("controller access_token : " + requestHeader.get("token"));
        //토큰을 이용해 정보 받아오기
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(requestHeader.get("token"));
        System.out.println("login Controller : " + userInfo);

        if (!(boolean) userInfo.get("resCode")) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        Optional<User> isUser = userRepository.findByEmail((String) userInfo.get("email"));
        if (!isUser.isPresent()) { // 이메일 중복 확인.


            User user = User.builder()
//                    .birthday((String) userInfo.get("birthday"))
                    .nickname((String) userInfo.get("nickname"))
//                    .gender((String) userInfo.get("gender"))
                    .pimg((String) userInfo.get("profile_img"))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .email((String) userInfo.get("email"))
                    .build();
            userRepository.save(user);
        }

        // 로그인 처리(토큰 발급)
        List<String> list = Arrays.asList("ROLE_USER");
        String jwtToken = jwtTokenProvider.createToken(String.valueOf((String) userInfo.get("email")), list);
        response.addHeader("Authorization", "Bearer " + jwtToken);

        User user = userRepository.findByEmail((String) userInfo.get("email")).get();

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setUid(user.getUid());
        userInfoResponse.setNickname(user.getNickname());
        userInfoResponse.setP_img(user.getPimg());
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }


}
