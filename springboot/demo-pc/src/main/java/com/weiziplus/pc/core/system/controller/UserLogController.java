package com.weiziplus.pc.core.system.controller;

import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.common.interceptor.AdminAuthToken;
import com.weiziplus.pc.common.interceptor.SysUserLog;
import com.weiziplus.pc.core.system.service.UserLogService;
import com.weiziplus.pc.core.system.vo.UserLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanglongwei
 * @date 2020/06/01 09/53
 */
@RestController
@AdminAuthToken
@RequestMapping("/userLog")
@Api(tags = "用户日志")
public class UserLogController {

    @Autowired
    UserLogService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "操作", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ipAddress", value = "ip地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "borderName", value = "浏览器", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "osName", value = "操作系统", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "起始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "截止时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createTimeSort", value = "创建时间排序", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLog(description = "获取系统用户日志分页数据")
    public ResultUtils<PageUtils<UserLogVo>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username, Integer type, String description, String ipAddress,
            String borderName, String osName, String startTime, String endTime,
            @RequestParam(defaultValue = "DESC") String createTimeSort) {
        return service.getPageList(pageNum, pageSize,
                username, type, description, ipAddress, borderName, osName, startTime, endTime,
                createTimeSort);
    }

}
