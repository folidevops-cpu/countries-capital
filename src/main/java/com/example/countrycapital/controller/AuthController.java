package com.example.countrycapital.controller;

import com.example.countrycapital.dto.JwtResponse;
import com.example.countrycapital.dto.LoginRequest;
import com.example.countrycapital.dto.MessageResponse;
import com.example.countrycapital.dto.SignupRequest;
import com.example.countrycapital.entity.Role;
import com.example.countrycapital.entity.User;
import com.example.countrycapital.repository.UserRepository;
import com.example.countrycapital.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    // Web endpoints for templates
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "auth/register";
    }

    @PostMapping("/signin")
    public String authenticateUser(@Valid @ModelAttribute LoginRequest loginRequest, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // For web applications, Spring Security will handle session management automatically
            
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password!");
            return "auth/login";
        }
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute SignupRequest signUpRequest, Model model) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            model.addAttribute("error", "Username is already taken!");
            return "auth/register";
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            model.addAttribute("error", "Email is already in use!");
            return "auth/register";
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                           signUpRequest.getEmail(),
                           encoder.encode(signUpRequest.getPassword()));

        user.setRole(Role.USER);
        userRepository.save(user);

        model.addAttribute("message", "User registered successfully!");
        return "auth/login";
    }

    // API endpoints for REST clients
    @PostMapping("/api/signin")
    @ResponseBody
    public ResponseEntity<?> authenticateUserApi(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);

        return ResponseEntity.ok(new JwtResponse(jwt,
                                               userDetails.getUsername(),
                                               user != null ? user.getEmail() : ""));
    }

    @PostMapping("/api/signup")
    @ResponseBody
    public ResponseEntity<?> registerUserApi(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                           signUpRequest.getEmail(),
                           encoder.encode(signUpRequest.getPassword()));

        user.setRole(Role.USER);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}