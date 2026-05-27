package com.property.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.entity.LiveUser;
import com.property.entity.SysUser;
import com.property.mapper.LiveUserMapper;
import com.property.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LiveUserMapper liveUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("请使用指定的登录方式");
    }

    public UserDetails loadSysUser(String username) {
        SysUser sysUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (sysUser.getStatus() == 0) {
            throw new UsernameNotFoundException("账号已被禁用");
        }
        List<String> permissions = sysUserMapper.selectPermissionsByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new LoginUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(),
                "sys", sysUser.getRealName(), authorities);
    }

    public UserDetails loadLiveUser(String username) {
        LiveUser liveUser = liveUserMapper.selectOne(
                new LambdaQueryWrapper<LiveUser>().eq(LiveUser::getUsername, username));
        if (liveUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (liveUser.getStatus() == 0) {
            throw new UsernameNotFoundException("账号已被禁用");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
        return new LoginUser(liveUser.getId(), liveUser.getUsername(), liveUser.getPassword(),
                "live", liveUser.getRealName(), authorities);
    }
}
