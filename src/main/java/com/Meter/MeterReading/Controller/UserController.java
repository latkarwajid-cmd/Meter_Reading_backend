package com.Meter.MeterReading.Controller;


import com.Meter.MeterReading.Model.User;
import com.Meter.MeterReading.Service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://mymeterreading.netlify.app"
})public class UserController {

    private UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

     @GetMapping("/admin/allUser")
     public List<User> findAllUser()
     {
         return userService.findAllUser();
     }

     @GetMapping("/admin/getById/{id}")
    public User getUserById(@PathVariable Integer id)
     {
         return userService.getById(id);
     }

     @PostMapping("/admin/addUser")
    public User addUser(@RequestBody User user)
     {

         return userService.addUser(user);
     }


    @PutMapping("/admin/updateUser/{id}")
    public User updateUser(
            @RequestBody User user,
            @PathVariable Integer id
    ) {
        return userService.updateUser(user, id);
    }
    @DeleteMapping("/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {

        userService.deleteUser(id);

        return "User deleted successfully";
    }
}
