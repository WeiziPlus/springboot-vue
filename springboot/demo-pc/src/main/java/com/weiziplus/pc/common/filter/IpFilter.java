package com.weiziplus.pc.common.filter;

import com.alibaba.fastjson.JSON;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.core.datadictionary.DataDictionaryIpManagerService;
import com.weiziplus.common.util.HttpRequestUtils;
import com.weiziplus.common.util.Md5Utils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/05/29 09/03
 */
@Slf4j
@Component
public class IpFilter extends BaseService implements Filter {

    @Autowired
    DataDictionaryIpManagerService dataDictionaryIpManagerService;

    /**
     * redis的key
     */
    private static final String REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 10秒内多少次请求，暂时封ip
     */
    @Value("${global.ip-filter-max-num:77}")
    private Integer maxNum = 77;

    /**
     * 过滤
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ipAddress = HttpRequestUtils.getIpAddress(request);
        //获取白名单列表
        Set<String> ipValueListWhite = dataDictionaryIpManagerService.getPcIpFilterWhiteList();
        //如果当前ip是白名单---直接放过
        if (ipValueListWhite.contains(ipAddress)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //获取ip过滤规则
        String ipFilterRole = dataDictionaryIpManagerService.getPcIpFilterRole();
        //只允许白名单
        if (DataDictionaryIpManagerService.IpFilterRole.WHITE.getValue().equals(ipFilterRole)) {
            //当前ip不是白名单，返回403状态码
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("禁止访问")));
            return;
        }
        //只禁止黑名单
        if (DataDictionaryIpManagerService.IpFilterRole.BLACK.getValue().equals(ipFilterRole)) {
            Set<String> ipFilterBlackList = dataDictionaryIpManagerService.getPcIpFilterBlackList();
            //如果当前ip是黑名单，直接返回403状态码
            if (ipFilterBlackList.contains(ipAddress)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("禁止访问")));
                return;
            }
        }
        /////////将访问频率过快的ip设置为异常ip
        //查看ip是否被临时封号
        String warnRedisKey = createRedisKey(REDIS_KEY + "ipWarn:", ipAddress, Md5Utils.encode16(request.getHeader(HttpHeaders.USER_AGENT)));
        Object warnObject = RedisUtils.get(warnRedisKey);
        //如果ip异常，暂时返回403状态码
        if (null != warnObject) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("访问频率过快,请稍后重试")));
            return;
        }
        //将当前时间访问ip次数存到redis
        String redisKey = createRedisKey(REDIS_KEY + "ipInfo:", ipAddress, Md5Utils.encode16(request.getHeader(HttpHeaders.USER_AGENT)));
        int number = 0;
        Object numberObject = RedisUtils.get(redisKey);
        if (null != numberObject) {
            number = (int) numberObject;
        }
        number += 1;
        //如果访问频率过快超出限制
        if (number >= maxNum) {
            //暂时封号---3分钟后恢复
            RedisUtils.set(warnRedisKey, true, 3 * 60L);
            dataDictionaryIpManagerService.handlePcAbnormalIp(ipAddress);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("访问频率过快,请稍后重试")));
            return;
        }
        if (null != numberObject) {
            RedisUtils.setNotChangeTimeOut(redisKey, number);
        } else {
            RedisUtils.set(redisKey, number, 10L);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
