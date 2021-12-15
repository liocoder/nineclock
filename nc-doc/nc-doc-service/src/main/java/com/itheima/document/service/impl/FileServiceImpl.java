package com.itheima.document.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.document.entity.File;
import com.itheima.document.mapper.FileMapper;
import com.itheima.document.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文件表 文档-文件表 服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Override
    public List<File> queryFilesByStatus(Integer status) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getStatus, status);
        wrapper.last(" limit 10 ");
        return this.list(wrapper);
    }
}
