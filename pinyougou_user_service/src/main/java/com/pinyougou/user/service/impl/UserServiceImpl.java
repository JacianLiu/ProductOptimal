package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.pojo.TbUserExample;
import com.pinyougou.pojo.TbUserExample.Criteria;
import com.pinyougou.user.service.UserService;
import entity.PageResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import utils.HttpClient;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbUser user) {
        // 加密密码
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setCreated(new Date());
        user.setUpdated(new Date());
        // 会员来源
        user.setSourceType("1");
        user.setStatus("1");
        user.setIsMobileCheck("1");
        userMapper.insert(user);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbUser user) {
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbUser findOne(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            userMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbUser user, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();

        if (user != null) {
            if (user.getUsername() != null && user.getUsername().length() > 0) {
                criteria.andUsernameLike("%" + user.getUsername() + "%");
            }
            if (user.getPassword() != null && user.getPassword().length() > 0) {
                criteria.andPasswordLike("%" + user.getPassword() + "%");
            }
            if (user.getPhone() != null && user.getPhone().length() > 0) {
                criteria.andPhoneLike("%" + user.getPhone() + "%");
            }
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                criteria.andEmailLike("%" + user.getEmail() + "%");
            }
            if (user.getSourceType() != null && user.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + user.getSourceType() + "%");
            }
            if (user.getNickName() != null && user.getNickName().length() > 0) {
                criteria.andNickNameLike("%" + user.getNickName() + "%");
            }
            if (user.getName() != null && user.getName().length() > 0) {
                criteria.andNameLike("%" + user.getName() + "%");
            }
            if (user.getStatus() != null && user.getStatus().length() > 0) {
                criteria.andStatusLike("%" + user.getStatus() + "%");
            }
            if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
                criteria.andHeadPicLike("%" + user.getHeadPic() + "%");
            }
            if (user.getQq() != null && user.getQq().length() > 0) {
                criteria.andQqLike("%" + user.getQq() + "%");
            }
            if (user.getIsMobileCheck() != null && user.getIsMobileCheck().length() > 0) {
                criteria.andIsMobileCheckLike("%" + user.getIsMobileCheck() + "%");
            }
            if (user.getIsEmailCheck() != null && user.getIsEmailCheck().length() > 0) {
                criteria.andIsEmailCheckLike("%" + user.getIsEmailCheck() + "%");
            }
            if (user.getSex() != null && user.getSex().length() > 0) {
                criteria.andSexLike("%" + user.getSex() + "%");
            }

        }

        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送的短信验证码
     *
     * @param phone 手机号码
     */
    @Override
    public void sendSmsCode(String phone) throws Exception {
        // 生成验证码 随机的 6 位数字
        String smsCode = RandomStringUtils.randomNumeric(6);
        int num = (int) (Math.random() * 9 + 1);
        smsCode = smsCode + num;
        // 将验证码存储到 redis 缓存 15 分钟
        redisTemplate.boundValueOps(phone).set(smsCode, 15L, TimeUnit.MINUTES);
        // 调用品优购短信平台发送短信 http://localhost:7788/sms/sendMsg.do
        HttpClient httpClient = new HttpClient("http://127.0.0.1:7788/sms/sendMsg.do");
        httpClient.addParameter("phoneNumbers", phone);
        httpClient.addParameter("signName", "品优购");
        httpClient.addParameter("templateCode", "SMS_123738164");
        httpClient.addParameter("templateParam", "{\"code\":" + smsCode + "}");
        httpClient.post();
        String content = httpClient.getContent();
        System.out.println(content);
        if (content == null) {
            throw new RuntimeException("发送短信失败~");
        }
    }

    /**
     * 验证验证码
     *
     * @param smsCode 用户输入的验证码
     * @param phone   手机号码
     * @return 验证结果
     */
    @Override
    public boolean checkSmsCode(String smsCode, String phone) {
        // 从redis中获取系统保存的验证码
        String sysSmsCode = (String) redisTemplate.boundValueOps(phone).get();
        if (smsCode == null) {
            return false;
        }
        return sysSmsCode.equals(smsCode);
    }
}
