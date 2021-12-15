package com.itheima.document.controller;

import com.itheima.common.vo.PageResult;
import com.itheima.common.vo.Result;
import com.itheima.document.dto.CollaborationsDTO;
import com.itheima.document.dto.UserCollaborationDTO;
import com.itheima.document.entity.File;
import com.itheima.document.entity.Folder;
import com.itheima.document.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    /**
     * 新建文件夹
     * POST/document/insertFolder
     */
    @PostMapping("/document/insertFolder")
    public Result insertFolder(@RequestBody Folder folder) {
        documentService.insertFolder(folder);
        return Result.success("创建文件夹成功");
    }

    /**
     * 新建文档
     * POST/document/insertFile
     */
    @PostMapping("/document/insertFile")
    public Result insertFile(@RequestBody File file) {
        documentService.insertFile(file);
        return Result.success("文档写入成功");
    }

    /**
     * 移动端-查询文件夹以及文件夹包含文档
     * GET/document/listFolderAndFile
     */
    @GetMapping("/document/listFolderAndFile")
    public Result queryAllFolderAndFile(
            @RequestParam(value = "parentFoldId", defaultValue = "0") Long parentFoldId) {
        return Result.success(documentService.queryAllFolderAndFile(parentFoldId));
    }

    /**
     * 通过文档id获得文档
     * 移动端
     */
    @GetMapping(value = "/document/getFileByFileId")
    public Result<File> getFileByFileId(@RequestParam Long id) throws Exception {
        return Result.success(documentService.queryFileById(id));
    }

    /**
     * 移动端
     * 查询指定文档的协作者列表
     * @param page
     * @param pageSize
     * @param fileId
     * @return
     */
    @GetMapping("/document/pagingCollaborations")
    public Result pagingCollaborations(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam("id") Long fileId
    ){
        return Result.success(documentService.pagingCollaborations(page, pageSize, fileId));
    }

    /**
     * 远程调用系统微服务获得所有员工记录信息 判断是是否是拥有者或者协作者
     * @param fileId 文档ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/document/pagingUsers")
    public Result<List<UserCollaborationDTO>> pagingCollaborationUsers(@RequestParam("id") Long fileId) throws Exception {
        return Result.success(documentService.pagingCollaborationUsers(fileId));
    }

    /**
     * 添加协作者
     * 移动端
     */
    @PostMapping(value = "/document/insertCollaboration")
    public Result insertCollaboration(@RequestBody CollaborationsDTO collaborationsDTO) throws Exception {
        documentService.insertCollaboration(collaborationsDTO);
        return Result.success();
    }

    /**
     * 删除协作者
     * 移动端
     */
    @PostMapping(value = "/document/deleteCollaboration")
    public Result deleteCollaboration(@RequestBody CollaborationsDTO collaborationsDTO) throws Exception {
        documentService.deleteCollaboration(collaborationsDTO);
        return Result.success();
    }

    /**
     * 分页查询企业文档列表-仅供导入索引库数据测试
     * GET/document/page
     */
    @GetMapping("/document/page")
    public PageResult<File> queryByPage(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return documentService.queryByPage(page, pageSize);
    }
}
