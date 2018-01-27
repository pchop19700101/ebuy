package cn.porkchop.ebuy.sso.service;

import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.TbUser;

public interface UserService {
    /**
     * 验证注册表单数据是否是唯一的,并且可以注册
     *
     * @param param
     *         表单的值
     * @param type
     *         验证的值的类型,1表示用户名,2表示手机号,3表示邮箱
     * @return 可以注册, e3result的date为true, 否则为false
     * @date 2018/1/26 22:41
     * @author porkchop
     */
    E3Result checkRegisterForm(String param, int type);

    /**
     * 用户注册,需要在后端验证数据合法性
     *
     * @date 2018/1/26 23:27
     * @author porkchop
     */
    E3Result createUser(TbUser user);

    /**
     * 登陆,同时生成token,让controller保存到cookie中
     *
     * @date 2018/1/27 13:44
     * @author porkchop
     */
    E3Result login(String userName, String password);

    /**
     * 根据token获得用户信息,并且重新设置token的过期时间
     *
     * @date 2018/1/27 22:03
     * @author porkchop
     */
    E3Result getUserByToken(String token);
}
