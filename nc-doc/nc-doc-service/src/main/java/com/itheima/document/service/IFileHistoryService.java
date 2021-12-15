package com.itheima.document.service;

import com.itheima.document.entity.File;
import com.itheima.document.entity.FileHistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
public interface IFileHistoryService extends IService<FileHistory> {

    void saveRecord(File file);
}
