package kr.easw.lesson07.service;

import kr.easw.lesson07.model.dto.UserAuthenticationDto;
import kr.easw.lesson07.model.dto.UserDataEntity;
import kr.easw.lesson07.model.repository.UserDataRepository;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDataService {
    private final UserDataRepository repository;

    private final BCryptPasswordEncoder encoder;

    private final JwtService jwtService;

    @PostConstruct
    public void init() {
        createUser(new UserDataEntity(0L, "admin", encoder.encode("admin"), true));
        createUser(new UserDataEntity(0L, "guest", encoder.encode("guest"), false));
    }

    public boolean isUserExists(String userId) {
        return repository.findUserDataEntityByUserId(userId).isPresent();
    }

    public void createUser(UserDataEntity entity) {
        repository.save(entity);
    }

    @Nullable
    public UserAuthenticationDto createTokenWith(UserDataEntity userDataEntity) {
        Optional<UserDataEntity> entity = repository.findUserDataEntityByUserId(userDataEntity.getUserId());
        if (entity.isEmpty()) throw new BadCredentialsException("Credentials invalid");

        UserDataEntity archivedEntity = entity.get();
        if (encoder.matches(userDataEntity.getPassword(), archivedEntity.getPassword()))
            return new UserAuthenticationDto(jwtService.generateToken(archivedEntity.getUserId()));
        throw new BadCredentialsException("Credentials invalid");
    }

    public List<String> listUsers() {
        List<UserDataEntity> userList = repository.findAll();

        return userList.stream()
                .map(UserDataEntity::getUserId)
                .toList();
    }

    public void deleteUsers(String userId) {
        Optional<UserDataEntity> entity = repository.findUserDataEntityByUserId(userId);
        if (entity.isEmpty()) throw new BadCredentialsException("Credentials invalid");
        UserDataEntity archivedEntity = entity.get();
        try {
            repository.delete(archivedEntity);
        } catch (Exception ex) {
            System.out.println("Error While Delete Users");
        }
    }
}