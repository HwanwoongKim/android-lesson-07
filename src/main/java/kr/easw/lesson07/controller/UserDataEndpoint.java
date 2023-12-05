package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.UserDataEntity;
import kr.easw.lesson07.service.UserDataService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserDataEndpoint {
    private final UserDataService userDataService;
    @GetMapping("/list")
    public List<String> listUsers() {
        return userDataService.listUsers();
    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeUser(@RequestBody UserDataEntity user) {
        try {
            if (userDataService.isUserExists(user.getUserId())) {
                userDataService.deleteUsers(user.getUserId());
                return ResponseEntity.ok(user.getUserId());
            }
            else {
                throw new Exception("유저가 존재하지 않습니다.");
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}