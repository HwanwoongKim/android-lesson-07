package kr.easw.lesson07.model.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserDataEntity {
    @Id
    @GeneratedValue
    private long id;

    @Getter
    @Setter
    private String userId;

    @Getter
    @Setter
    private String password;

    @Getter
    private boolean isAdmin;
}