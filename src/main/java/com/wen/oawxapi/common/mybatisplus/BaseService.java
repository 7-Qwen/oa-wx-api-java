package com.wen.oawxapi.common.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author: 7wen
 * @Date: 2023-06-06 16:52
 * @description:
 */
public class BaseService<M extends BaseMapper<T>, T extends Model<T>> extends ServiceImpl<M, T> {
    /**
     * 获取请求包装对象
     *
     * @param iPage iPage子类
     * @return
     */
    public ReturnPage<T> getReturnPage(IPage<T> iPage) {
        return new ReturnPage<T>(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), iPage.getRecords());
    }

    /**
     * 快捷获取lambdaQueryWrapper
     *
     * @return
     */
    public LambdaQueryWrapper<T> getLambdaQueryWrapper() {
        return new LambdaQueryWrapper<T>();
    }
}
