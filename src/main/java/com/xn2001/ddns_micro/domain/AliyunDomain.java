package com.xn2001.ddns_micro.domain;

import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2022/5/10 22:35
 */
@Component
public class AliyunDomain {

    @Value("${aliddns.access-key-id}")
    String ACCESS_KEY_ID;
    @Value("${aliddns.access-key-secret}")
    String ACCESS_KEY_SECRET;
    @Value("${aliddns.domain-name}")
    String DOMAIN_NAME;
    @Value("${aliddns.tow-domain-name}")
    String TWO_DOMAIN_NAME;

    // 初始化连接
    public com.aliyun.alidns20150109.Client initialization() throws Exception {
        Config config = new Config()
                .setAccessKeyId(ACCESS_KEY_ID)
                .setAccessKeySecret(ACCESS_KEY_SECRET);
        config.endpoint = "alidns.cn-hangzhou.aliyuncs.com"; // 一般不用修改
        return new com.aliyun.alidns20150109.Client(config);
    }

    // 修改阿里云域名解析记录
    public void updateDomainRecord(String ipv4) throws Exception {
        Client client = this.initialization();
        DescribeDomainRecordsRequest domainRecordsRequest = new DescribeDomainRecordsRequest();
        domainRecordsRequest.setDomainName(DOMAIN_NAME);
        // 解析记录列表
        DescribeDomainRecordsResponse domainRecordsResponse = client.describeDomainRecords(domainRecordsRequest);
        for (DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord record : domainRecordsResponse.body.domainRecords.record) {
            UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
            // 找到指定的二级域名
            if (record.RR.equals(TWO_DOMAIN_NAME)) {
                // ip变动则重新解析
                if (!record.value.equals(ipv4)) {
                    BeanUtils.copyProperties(record, updateDomainRecordRequest);
                    updateDomainRecordRequest.setValue(ipv4);
                    client.updateDomainRecord(updateDomainRecordRequest);
                    System.out.println(record.RR + "." + DOMAIN_NAME + "的主机记录更新为记录值: " + ipv4);
                }
                break;
            }
        }
    }
}
