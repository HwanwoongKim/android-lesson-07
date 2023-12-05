package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.UserDataEntity;
import kr.easw.lesson07.service.UserDataService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthEndpoint {
    private final UserDataService userDataService;
    private final BCryptPasswordEncoder encoder;


    // JWT 인증을 위해 사용되는 엔드포인트입니다.
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDataEntity entity) {
        try {
            // 로그인을 시도합니다.
            return ResponseEntity.ok(userDataService.createTokenWith(entity));
        } catch (Exception ex) {
            // 만약 로그인에 실패했다면, 400 Bad Request를 반환합니다.
            return ResponseEntity.badRequest().body("");
        }
    }


    @PostMapping("/register")
    public void register(@ModelAttribute UserDataEntity entity) {
        System.out.println(entity.getUserId());
        if (!userDataService.isUserExists(entity.getUserId())) {
            try {
                String pw = entity.getPassword();
                entity.setPassword(encoder.encode(pw));
                System.out.println(pw);
                System.out.println(entity.getPassword());
                userDataService.createUser(entity);
                System.out.println("Success");
            } catch (Exception ex) {
                System.out.println("Problem while create user.");
            }
        }
    }
}
