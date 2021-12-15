package com.itheima.document.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.document.entity.File;
import com.itheima.document.entity.FileHistory;
import com.itheima.document.mapper.FileHistoryMapper;
import com.itheima.document.service.IFileHistoryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
@Service
public class FileHistoryServiceImpl extends ServiceImpl<FileHistoryMapper, FileHistory> implements IFileHistoryService {

    @Override
    @Async("taskExecutor")
    public void saveRecord(File file) {
        System.out.println("子线程处理："+Thread.currentThread());
        FileHistory fileHistory = new FileHistory();
        fileHistory.setFileId(file.getId());
        fileHistory.setFileName(file.getName());
        fileHistory.setCompanyUserId(file.getCompanyUserId());
        fileHistory.setIfCreate(true);
        fileHistory.setContent(file.getContent());
        this.save(fileHistory);
    }
}
