package com.internship.mareshkiFinance.services;

import com.cleverpine.template.model.UserLogin;
import com.cleverpine.template.model.UserProfileResponse;
import com.cleverpine.template.model.UserProfileUpdate;
import com.cleverpine.template.model.UserRegistration;
import com.internship.mareshkiFinance.dto.UserDTO;
import com.internship.mareshkiFinance.entities.UserEntity;
import com.internship.mareshkiFinance.repositories.UserRepository;
import com.internship.mareshkiFinance.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static UserProfileResponse getUserProfileResponse(UserDTO updatedUserDTO) {
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setUserId(Math.toIntExact(updatedUserDTO.getId()));
        userProfileResponse.setFirstName(updatedUserDTO.getFirstName());
        userProfileResponse.setUsername(updatedUserDTO.getUsername());
        userProfileResponse.setEmail(updatedUserDTO.getEmail());
        userProfileResponse.setCity(updatedUserDTO.getCity());
        userProfileResponse.setCountry(updatedUserDTO.getCountry());
        return userProfileResponse;
    }

    public UserDTO getProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getCity(),
                userEntity.getCountry(),
                userEntity.getPassword()
        );
    }

    public UserDTO informationForUpdatedProfile(UserProfileUpdate userProfileUpdate){
        UserDTO updatedProfile = new UserDTO();
        updatedProfile.setFirstName(userProfileUpdate.getFirstName());
        updatedProfile.setUsername(userProfileUpdate.getUsername());
        updatedProfile.setEmail(userProfileUpdate.getEmail());
        updatedProfile.setCity(userProfileUpdate.getCity());
        updatedProfile.setCountry(userProfileUpdate.getCountry());
        return updatedProfile;
    }

    public UserDTO updateProfile(Long userId, UserDTO updatedProfile) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userEntity.setFirstName(updatedProfile.getFirstName());
            userEntity.setUsername(updatedProfile.getUsername());
            userEntity.setEmail(updatedProfile.getEmail());
            userEntity.setCity(updatedProfile.getCity());
            userEntity.setCountry(updatedProfile.getCountry());
            userRepository.save(userEntity);
            return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getCity(), userEntity.getCountry(), userEntity.getPassword());
        } else {
            return null;
        }
    }

    public String uploadAvatar(Long userId, MultipartFile avatar) {
        try {
            if (avatar != null && !avatar.isEmpty()) {
                return saveAvatar(userId, avatar);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save avatar", e);
        }
    }

    public String saveAvatar(Long userId, MultipartFile avatar) throws IOException {
        String fileName = userId + "_" + avatar.getOriginalFilename();

        String resourcesPath = getClass().getClassLoader().getResource("").getPath();

        File directory = new File(resourcesPath + "/ProfileAvatars");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = resourcesPath + "/ProfileAvatars/" + fileName;
        File dest = new File(filePath);
        avatar.transferTo(dest);

        return String.format("/ProfileAvatars/" + fileName);
    }

    public UserDTO parseInfoForRegistration(UserRegistration userRegistration){
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(userRegistration.getFirstName());
        userDTO.setEmail(userRegistration.getEmail());
        userDTO.setPassword(userRegistration.getPassword());
            return userDTO;
}

    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {

            return;
        }
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        UserEntity user = new UserEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(encryptedPassword);
        user.setFirstName(userDTO.getFirstName());
        user.setCountry("");
        user.setCity("");
        user.setUsername(userDTO.getFirstName() + "ToChange");
        userRepository.save(user);
    }

    public String loginToken(UserLogin userLogin) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userLogin.getEmail());

        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();

            UserDTO userDTO = convertToDTO(userEntity);

            if (passwordEncoder.matches(userLogin.getPassword(), userEntity.getPassword())) {
                return jwtUtil.generateToken(userDTO.getEmail());
            } else {
                throw new RuntimeException("Invalid password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        return userDTO;
    }


}