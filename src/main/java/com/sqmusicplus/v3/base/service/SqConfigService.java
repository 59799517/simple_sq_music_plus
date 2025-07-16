package com.sqmusicplus.v3.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqmusicplus.v3.base.entity.SqConfig;

/**
 * @Classname SqConfigService
 * @Description 设置service
 * @Version 1.0.0
 * @Date 2022/10/21 10:45
 * @Created by SQ
 */
public interface SqConfigService extends IService<SqConfig> {

    SqConfig selectByKeyAndValue(String key);

}
