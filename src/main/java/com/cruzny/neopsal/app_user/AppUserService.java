package com.cruzny.neopsal.app_user;

import com.cruzny.neopsal.registration.Token.ConfirmationToken;
import com.cruzny.neopsal.registration.Token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepo appUserRepository;
    private final static String USER_NOT_FOUND = "User with email %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format(USER_NOT_FOUND, email)
        ));
    }

    public AppUser loadUserByEmail(String email)throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format(USER_NOT_FOUND, email)
        ));
    }

    public boolean userExists(String email){
        boolean userExists = appUserRepository
                .findByEmail(email)
                .isPresent();
        return userExists;
    }
    public String signUpUser(AppUser appUser){
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
//        Player player = new Player(appUser);
//        appUser.setPlayer(player);
        appUserRepository.save(appUser);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationTokenoken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationTokenoken);
        return token;
    }
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
