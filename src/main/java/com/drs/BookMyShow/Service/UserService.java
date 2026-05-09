package com.drs.BookMyShow.Service;

import com.drs.BookMyShow.Dto.UserDto;
import com.drs.BookMyShow.Exception.ResourcesNotFoundException;
import com.drs.BookMyShow.Model.User;
import com.drs.BookMyShow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(UserDto userDto){
        User user = mapToEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(user);
    }

    public UserDto getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourcesNotFoundException("User not found with this id :" + id));
        return mapToDto(user);
    }

    public UserDto getUserById(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourcesNotFoundException("User not found with this id :" + email));
        return mapToDto(user);
    }

    public List<UserDto> getAllUser(){
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public UserDto updateUser(Long id , UserDto userDto){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourcesNotFoundException("User not found with this id :" + id));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        User updateUser = userRepository.save(user);
        return mapToDto(updateUser);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourcesNotFoundException("User not found with this id :" + id));
        userRepository.delete(user);
    }

    private User mapToEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    private UserDto mapToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }
}
