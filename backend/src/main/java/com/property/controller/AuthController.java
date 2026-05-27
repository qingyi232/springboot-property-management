package com.property.controller;

import com.property.dto.LoginDto;
import com.property.entity.LiveUser;
import com.property.entity.SysMenu;
import com.property.entity.SysUser;
import com.property.security.LoginUser;
import com.property.security.UserDetailServiceImpl;
import com.property.service.SysMenuService;
import com.property.utils.JwtUtils;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDto loginDto) {
        LoginUser loginUser;
        if ("sys".equals(loginDto.getUserType())) {
            loginUser = (LoginUser) userDetailService.loadSysUser(loginDto.getUsername());
        } else {
            loginUser = (LoginUser) userDetailService.loadLiveUser(loginDto.getUsername());
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), loginUser.getPassword())) {
            return Result.fail("用户名或密码错误");
        }

        String token = jwtUtils.generateToken(loginUser.getUsername(), loginDto.getUserType());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", loginUser.getUserId());
        data.put("username", loginUser.getUsername());
        data.put("realName", loginUser.getRealName());
        data.put("userType", loginDto.getUserType());
        return Result.ok("登录成功", data);
    }

    @GetMapping("/info")
    public Result<?> getUserInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", loginUser.getUserId());
        data.put("username", loginUser.getUsername());
        data.put("realName", loginUser.getRealName());
        data.put("userType", loginUser.getUserType());
        data.put("permissions", loginUser.getAuthorities());

        if ("sys".equals(loginUser.getUserType())) {
            List<SysMenu> menus = sysMenuService.getMenuTreeByUserId(loginUser.getUserId());
            data.put("menus", menus);
        }

        return Result.ok(data);
    }
}
