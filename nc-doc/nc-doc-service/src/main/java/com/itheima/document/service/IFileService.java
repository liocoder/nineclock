package com.itheima.document.service;

import com.itheima.document.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文件表 文档-文件表 服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
public interface IFileService extends IService<File> {

    List<File> queryFilesByStatus(Integer status);
}
