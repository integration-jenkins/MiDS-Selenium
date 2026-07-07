package com.avendum.midsautomate.service;

import com.avendum.midsautomate.model.User;
import com.avendum.midsautomate.repository.TestUserRepository;
import com.avendum.midsautomate.repository.UserRepository;
import com.avendum.midsautomate.selenium.dto.RemoveUsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUserRepository testUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Transactional
    public boolean removeUsers(List<RemoveUsersDTO> usersDTOS){
        try {
            for(RemoveUsersDTO removeUsersDTO:usersDTOS){
                testUserRepository.deleteByNameAndDepartment(removeUsersDTO.getName(),removeUsersDTO.getDepartment());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
