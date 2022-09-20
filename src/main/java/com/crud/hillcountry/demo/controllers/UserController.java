package com.crud.hillcountry.demo.controllers;

import com.crud.hillcountry.demo.dao.UserRepository;
import com.crud.hillcountry.demo.model.User;
import com.crud.hillcountry.demo.model.UserPublic;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repository;


    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public ArrayList<UserPublic> getAllUsers() {
        ArrayList<User> list = (ArrayList<User>) this.repository.findAll();
        //List<USerPublic>
        ArrayList<UserPublic> publicList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UserPublic user = new UserPublic();
            user.setId(list.get(i).getId());
            user.setEmail(list.get(i).getEmail());
            publicList.add(user);
        }
        //loop through list of users -->copy everything except passwords

        return publicList;
    }

    @PostMapping("")
    public UserPublic postUser(@RequestBody User UserToAdd) {
        this.repository.save(UserToAdd);
        UserPublic AddUser = new UserPublic();
        AddUser.setId(UserToAdd.getId());
        AddUser.setEmail(this.repository.findById(AddUser.getId()).get().getEmail());

        return AddUser;
    }

    @GetMapping("/{id}")
    public UserPublic getUserById(@PathVariable Long id) {
        User userFound = this.repository.findById(id).get();

        UserPublic publicInfo = new UserPublic();
        publicInfo.setEmail(userFound.getEmail());
        publicInfo.setId(userFound.getId());
        return publicInfo;
    }

    @PatchMapping("/{id}")
    public UserPublic patchUser(@PathVariable Long id, @RequestBody Map<String, String> updatedInfo) {
        User userToUpdate = this.repository.getReferenceById(id);
        updatedInfo.forEach(
                (key, value) -> {
                    if (key.equals("email")) {
                        userToUpdate.setEmail(value);
                    } else if (key.equals("password")) {
                        userToUpdate.setPassword(value);
                    }
                }
        );
        this.repository.save(userToUpdate);
        UserPublic updatedUser = new UserPublic();
        updatedUser.setId(userToUpdate.getId());
        updatedUser.setEmail(this.repository.getReferenceById(userToUpdate.getId()).getEmail());
        return updatedUser;
    }
}
