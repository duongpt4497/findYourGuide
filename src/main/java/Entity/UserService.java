//    /*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package entity;
//
//import model.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// *
// * @author dgdbp
// */
//@Service
//public class UserService {
//    private UserRepo repo;
//    private PasswordEncoder passwordEncoder;
//    private TokenService tokenService;
//    //add register user here
//    //public void register(){}
//    @Autowired
//    public UserService(UserRepo repo, PasswordEncoder passwordEncoder, TokenService tokenService) {
//        this.repo = repo;
//        this.passwordEncoder = passwordEncoder;
//        this.tokenService = tokenService;
//    }
//    
//    @Transactional
//    public String saveUser(String username, String password, String email) {
//        User user = repo.saveAndFlush();
//        return tokenService.genToken(user);
//    }
//    
//    
//}
