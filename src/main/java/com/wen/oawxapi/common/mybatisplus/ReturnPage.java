package com.wen.oawxapi.common.mybatisplus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: 7wen
 * @Date: 2023-06-06 16:54
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnPage<T> {

    /**
     * 页面
     */
    private Long page;

    /**
     * 展示数据的大小
     */
    private Long size;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 数据
     */
    private List<T> data;
}
