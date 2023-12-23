package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaopangshu6
 * @since 2023-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Result queryAndByAsc() {
        String key=CACHE_SHOP_TYPE_KEY;
        // 1、从redis查看商铺类型缓存
        String shopTypeJson = stringRedisTemplate.opsForValue().get(key);

        // 2、判断是否存在
        if(StrUtil.isNotBlank(shopTypeJson)){
            // 3、存在，转换回List类型直接返回
            List<ShopType> shopTypes= JSONUtil.toList(shopTypeJson,ShopType.class);
            return Result.ok(shopTypes);
        }

        // 4、不存在，根据id查看数据库并按照升序排序
        List<ShopType> typeList = query().orderByAsc("sort").list();

        // 5、不存在，返回错误
        if(typeList==null){
            return Result.fail("无店铺类型！");
        }

        // 6、存在,转换成String类型，并写入redis
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(typeList),CACHE_SHOP_TYPE_TTL, TimeUnit.MINUTES);

        // 7、返回
        return Result.ok(typeList);

    }
}
