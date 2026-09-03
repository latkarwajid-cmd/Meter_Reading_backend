package com.Meter.MeterReading.Service;


import com.Meter.MeterReading.Model.User;
import com.Meter.MeterReading.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public List<User> findAllUser() {

        return userRepo.findAll();
    }

    public User getById(Integer id) {

        return userRepo.findById(id).orElse(null);
    }

    public User addUser(User user) {

        return userRepo.save(user);
    }

    public User updateUser(User user, Integer id) {

        User updatedUser = userRepo.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with id: " + id)
        );

        updatedUser.setName(user.getName());
        updatedUser.setPhoneNumber(user.getPhoneNumber());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setAddress(user.getAddress());

        return userRepo.save(updatedUser);
    }

    public void deleteUser(Integer id) {
        userRepo.deleteById(id);
    }
}
