package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.service.IFollowService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaopangshu6
 * @since 2023-12-22
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IFollowService followService;

    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id")Long followUserId,@PathVariable("isFollow")Boolean isFollow){
        return followService.follow(followUserId,isFollow);
    }

    @GetMapping("/or/not/{id}")
    public Result isFollow(@PathVariable("id")Long followUserId){
        return followService.isFollow(followUserId);
    }

    @GetMapping("/common/{id}")
    public Result followCommon(@PathVariable("id")Long id){
        return followService.followCommons(id);
    }

}
