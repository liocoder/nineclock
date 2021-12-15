package com.itheima.document.service.impl;

import com.itheima.document.entity.Folder;
import com.itheima.document.mapper.FolderMapper;
import com.itheima.document.service.IFolderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件夹表 文档-文件夹表 服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements IFolderService {

}
