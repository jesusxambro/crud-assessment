package com.crud.hillcountry.demo.controllers;

import com.crud.hillcountry.demo.dao.UserRepository;
import com.crud.hillcountry.demo.model.Authentication;
import com.crud.hillcountry.demo.model.User;
import com.crud.hillcountry.demo.model.UserPublic;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    @PostMapping("/authenticate")
    public Authentication authenticateUsers(@RequestBody Map<String, String> userToAuthMap) {
        Authentication authUser = new Authentication();

        UserPublic authenticatedUser = new UserPublic();
        User userInArray = new User();
        final User[] userToCheckArray = new User[1];
        userToCheckArray[0] = userInArray;
        final String[] password = new String[1];

        userToAuthMap.forEach(
                (key, value) -> {
                    if (key.equals("email")) { //value.equals(this.repository.findByEmail(value).get().getEmail())
                        userToCheckArray[0].setEmail(value);
                    }
                    if (key.equals("password")) {
                        password[0] = value;
                    }
                }
        );
//        //return repository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Record with id %d is not present", id)));**
//         *
//         */

        Authentication noneFound = new Authentication();
        boolean isPresent = this.repository.findByEmail(userToCheckArray[0].getEmail()).isPresent();
        User inDatabase;
        if (isPresent) {
            inDatabase = this.repository.findByEmail(userToCheckArray[0].getEmail()).get();
            if (inDatabase.getPassword().equals(password[0])) {

                authenticatedUser.setEmail(inDatabase.getEmail());
                authenticatedUser.setId(inDatabase.getId());
                authUser.setAuthenticated(true);
                authUser.setUser(authenticatedUser);
                return authUser;
            }
        } else {
            authUser.setAuthenticated(false);
            return authUser;
        }


//        User userToCheck = userToCheckArray[0];
        return authUser;

    }



    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Map<String, Integer> deleteUserById(@PathVariable Long id) {
        this.repository.deleteById(id);
        Map<String, Integer> count = new HashMap<>();
        count.put("count", (int) this.repository.count());
        return count;
    }
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NoSuchElementException.class)
//    public String handleElementNotFound(Exception e) {
//        return e.getMessage();
//    }


}
