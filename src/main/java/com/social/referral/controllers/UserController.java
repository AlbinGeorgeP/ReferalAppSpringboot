package com.social.referral.controllers;
import com.social.referral.dto.RoleAddDTO;
import com.social.referral.dto.SearchQuery;
import com.social.referral.dto.UserDTO;
import com.social.referral.entities.UserCompanyView;
import com.social.referral.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/UserService")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping(value ="/users",produces = "application/json")
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(value ="/user/search",produces = "application/json",consumes = "application/json")
    public List<UserCompanyView> searchUsers(@RequestBody SearchQuery searchQuery){
        return userService.searchUsers(searchQuery);
    }

    @GetMapping(value ="/user/{id}",produces = "application/json")
    public UserDTO getAllUser(@PathVariable(value = "id") Integer id){
        return userService.getUser(id);
    }

    @PostMapping(value ="/user",consumes = "application/json")
    public ResponseEntity<String> AddUser(@RequestBody UserDTO user){
        return ResponseEntity.ok(userService.addUser(user));
    }
    @PutMapping(value="/user", consumes = "application/json")
    public ResponseEntity<String> UpdateUser(@RequestBody UserDTO user) throws Exception {return ResponseEntity.ok(userService.updateUser(user));}
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value ="/user/addrole",consumes = "application/json")
    public ResponseEntity<String> AddRole(@RequestBody RoleAddDTO request) throws Exception{return ResponseEntity.ok(userService.addRoleToUser(request));
    }

}
