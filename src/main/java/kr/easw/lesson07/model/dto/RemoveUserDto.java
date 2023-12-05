package kr.easw.lesson07.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RemoveUserDto {
    private final String userId;
}
