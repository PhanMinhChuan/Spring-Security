package zuka.cloud.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.ClientProtocolException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import zuka.cloud.demo.common.FacebookUtils;
import zuka.cloud.demo.common.GoogleUtils;
import zuka.cloud.demo.common.LinkedInUtils;
import zuka.cloud.demo.jwt.JwtTokenProvider;
import zuka.cloud.demo.model.*;
import zuka.cloud.demo.model.pojos.GooglePojo;
import zuka.cloud.demo.model.pojos.LinkedInPojo;
import zuka.cloud.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private GoogleUtils googleUtils;

    @Autowired
    private FacebookUtils facebookUtils;

    @Autowired
    private LinkedInUtils linkedInUtils;

    @Autowired
    private UserService customUserDetailsService;

    @RequestMapping(value = { "/", "/login" })
    public String login() {
        return "login";
    }

    @PostMapping("/login-handle")
    public String authenticateUser(HttpServletRequest request, HttpSession Session) {
        // Xác thực thông tin người dùng Request lên
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getParameter("username"),
                        request.getParameter("password")
                )
        );
        String jwt = tokenProvider.generateToken((CustomUserDetails) auth.getPrincipal());
        UserDetails userDetails = null;
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            int userId = tokenProvider.getUserIdFromJWT(jwt);
            userDetails = customUserDetailsService.loadUserById(userId);
            if(userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            return "redirect:/login?google=error";
        }
        System.out.println("Token user login:" + jwt);
        Session.setAttribute("name", userDetails.getUsername());
        return "redirect:/user";
    }

    @RequestMapping("/login-google")
    public String loginGoogle(HttpServletRequest request, HttpSession Session) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return "redirect:/login?google=error";
        }

        String accessToken = googleUtils.getToken(code);
        System.out.println("Token: " + accessToken);
        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        UserDetails userDetail = googleUtils.buildUser(googlePojo);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Session.setAttribute("name", googlePojo.getEmail());
        return "redirect:/user";
    }

    @RequestMapping("/login-facebook")
    public String loginFacebook(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return "redirect:/login?facebook=error";
        }

        String accessToken = facebookUtils.getToken(code);
        com.restfb.types.User user = facebookUtils.getUserInfo(accessToken);
        UserDetails userDetail = facebookUtils.buildUser(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/user";
    }

    @RequestMapping("/login-linkedin")
    public String loginLinkedIn(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return "redirect:/login?message=linkedin_error";
        }

        String accessToken = linkedInUtils.getToken(code);
        LinkedInPojo linkedInPojo = linkedInUtils.getUserInfo(accessToken);
        UserDetails userDetail = linkedInUtils.buildUser(linkedInPojo);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/user";
    }

    @RequestMapping("/user")
    public String user() {
        return "user";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
