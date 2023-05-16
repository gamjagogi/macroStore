package com.hjson.macrostore.core.auth.session;

import com.hjson.macrostore.model.user.User;
import com.hjson.macrostore.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userPS = userRepository.findByUsername(username)
                .orElseThrow(()-> new InternalAuthenticationServiceException("인증 실패"));
        return new MyUserDetails(userPS);
    }
}
