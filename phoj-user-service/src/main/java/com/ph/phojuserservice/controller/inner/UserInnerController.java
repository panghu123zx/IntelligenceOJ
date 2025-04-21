package com.ph.phojuserservice.controller.inner;

import com.ph.phojclient.service.UserClientService;
import com.ph.phojmodel.model.entity.User;
import com.ph.phojuserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 内部接口，只能内部使用
 */

@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserClientService {
    @Resource
    private UserService userService;


    /**
     * 根据ID获取用户信息
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据ID集合获取信息
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/list/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
