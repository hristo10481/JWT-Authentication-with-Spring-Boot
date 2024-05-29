package com.internship.mareshkiFinance.controller;


import com.cleverpine.template.api.UsersApi;
import com.cleverpine.template.model.*;
import com.internship.mareshkiFinance.dto.UserDTO;
import com.internship.mareshkiFinance.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final UserService userService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(new ServletWebRequest(request, response));
    }

    @Override
    public ResponseEntity<TokenResponse> register(UserRegistration userRegistration) {
        userService.registerUser(userService.parseInfoForRegistration(userRegistration));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TokenResponse> login(UserLogin userLogin) {

        var token = userService.loginToken(userLogin);
        var tokenResponse = new TokenResponse();
        tokenResponse.setData(token);

        return ResponseEntity.ok(tokenResponse);
    }

    @Override
    public ResponseEntity<UserProfileResponse> getUserProfile(Long userId) {
        UserDTO userDTO = userService.getProfile(userId);
        if (userDTO != null) {
            UserProfileResponse userProfileResponse = userService.getUserProfileResponse(userDTO);
            return ResponseEntity.ok(   userProfileResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<UserProfileResponse> updateUserProfile(Long userId, UserProfileUpdate userProfileUpdate) {

        UserDTO updatedUserDTO = userService.updateProfile(userId, userService.informationForUpdatedProfile(userProfileUpdate));

        if (updatedUserDTO != null) {
            UserProfileResponse userProfileResponse = userService.getUserProfileResponse(updatedUserDTO);
            return ResponseEntity.ok(userProfileResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<UploadUserAvatar200Response> uploadUserAvatar(@PathVariable Long userId,
                                                                        @RequestParam("avatar") MultipartFile avatar) {
        String avatarUrl = userService.uploadAvatar(userId, avatar);
        if (avatarUrl != null) {
            UploadUserAvatar200Response response = new UploadUserAvatar200Response();
            response.setAvatarUrl(avatarUrl);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
