package cn.porkchop.ebuy.sso.service.impl;

import cn.porkchop.ebuy.jedis.JedisClient;
import cn.porkchop.ebuy.mapper.TbUserMapper;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbUser;
import cn.porkchop.ebuy.pojo.TbUserExample;
import cn.porkchop.ebuy.sso.service.UserService;
import cn.porkchop.ebuy.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE_SECOND}")
    private int SESSION_EXPIRE_SECOND;

    @Override
    public E3Result checkRegisterForm(String param, int type) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return E3Result.build(400, "非法的参数");
        }
        //查询邮箱,手机号,用户名是否重复
        List<TbUser> list = userMapper.selectByExample(tbUserExample);
        if (list == null || list.size() == 0) {
            return E3Result.ok(true);
        }
        return E3Result.ok(false);
    }

    @Override
    public E3Result createUser(TbUser user) {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
            //没有电话,邮箱,用户名
            return E3Result.build(400, "用户信息不完整");
        }
        //验证用户名唯一性
        E3Result e3Result = checkRegisterForm(user.getUsername(), 1);
        if (!(boolean) e3Result.getData()) {
            return E3Result.build(400, "此用户名已注册");
        }
        //验证手机号唯一性
        e3Result = checkRegisterForm(user.getUsername(), 2);
        if (!(boolean) e3Result.getData()) {
            return E3Result.build(400, "此手机号已注册");
        }
        //补全其他属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insert(user);
        return E3Result.ok();
    }

    @Override
    public E3Result login(String username, String password) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes()));
        List<TbUser> list = userMapper.selectByExample(tbUserExample);
        if (list == null || list.size() == 0) {
            return E3Result.build(400, "用户名或密码错误");
        }
        TbUser tbUser = list.get(0);
        //设置密码为空,不保存到redis中
        tbUser.setPassword(null);
        //把token放到redis中,
        String token = UUID.randomUUID().toString();
        jedisClient.set("session:" + token, JsonUtils.objectToJson(tbUser));
        //设置token的过期时间
        jedisClient.expire("session" + token, SESSION_EXPIRE_SECOND);
        return E3Result.build(200, "登陆成功", token);
    }

    @Override
    public E3Result getUserByToken(String token) {
        String string = jedisClient.get("session:" + token);
        if (StringUtils.isBlank(string)) {
            return E3Result.build(400, "登陆已过期,请重新登陆");
        }
        TbUser tbUser = JsonUtils.jsonToPojo(string, TbUser.class);
        jedisClient.expire("session:" + token, SESSION_EXPIRE_SECOND);
        return E3Result.ok(tbUser);
    }
}
