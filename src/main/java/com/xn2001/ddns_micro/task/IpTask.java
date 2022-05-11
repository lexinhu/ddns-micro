package com.xn2001.ddns_micro.task;

import com.xn2001.ddns_micro.domain.AliyunDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2022/5/10 21:58
 */
@Component
public class IpTask {

    @Value("${pull-address}")
    private String pullAddress;

    @Autowired
    private AliyunDomain aliyunDomain;

    /**
     * 每半小时（1800000ms）获取公网ip去尝试更新域名解析记录
     */
    @Scheduled(fixedRate = 1800000)
    public void ipTask() {
        String ipv4 = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(pullAddress);
            // 打开和 URL 之间的连接
            URLConnection connection = realUrl.openConnection();
            // 定义 BufferedReader 输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                ipv4 += line;
            }
            // TODO: 2022/5/10 更新阿里云域名解析
            aliyunDomain.updateDomainRecord(ipv4);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
