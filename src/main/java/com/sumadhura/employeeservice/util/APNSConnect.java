package com.sumadhura.employeeservice.util;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.auth.ApnsSigningKey;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
/**
 * 
 * @author Aniket Chavan
 * @Description used this class for creating ApnsClient obejct and send push notification to IOS devices,
 * and used p8 file
 * @date 21-07-2022 
 * @modifieddate 21-07-2022
 */
public class APNSConnect {

	private static final Logger logger = LoggerFactory.getLogger(APNSConnect.class);

	private static ApnsClient apnsClient = null;

    public static ApnsClient getAPNSConnect(String host) {
        
    	 /*if (apnsClient == null) {//use this code for.p12 file
             try {
                 EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
                 apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                         .setClientCredentials(Thread.currentThread().getContextClassLoader().getResourceAsStream("Certificates.p12"), "$CMD148")
                         .setConcurrentConnections(4).setEventLoopGroup(eventLoopGroup).build();

             } catch (Exception e) {
                 logger.error("ios get pushy apns client failed!");
                 e.printStackTrace();
             }
         }*/
    	
    	 if (apnsClient == null) {
             try {
                 //EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);//commented by ACP
 //apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
 //.setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(jpushConfig.getPath()),//"C:/Users/user/Desktop/AuthKey_XNQ237XNW7.p8"
 //jpushConfig.getTeamId(), jpushConfig.getKeyId()))//"8NM2C7228N", "XNQ237XNW7"
 //.setConcurrentConnections(4).setEventLoopGroup(eventLoopGroup).build();
                 apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                         .setSigningKey(ApnsSigningKey.loadFromInputStream(APNSConnect.class.getClassLoader()
                        		 .getResourceAsStream("AuthKey_ZGQZ2A7MLS.p8"),//"C:/Users/user/Desktop/AuthKey_XNQ237XNW7.p8"
                                 "YVJYZK8A9T", "ZGQZ2A7MLS"))//"8NM2C7228N", "XNQ237XNW7"
                         //.setConcurrentConnections(4).setEventLoopGroup(eventLoopGroup)//commented by ACP
                         .build();
             } catch (Exception e) {
                 logger.error("ios get pushy apns client failed!");
                 e.printStackTrace();
             }
    	 }
    	/*if (apnsClient == null) {
            try {
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
                apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File("path/the path where you placed the .p8 certificate"),
                                "Your teamid", "Your keyid"))
                        .setConcurrentConnections(4).setEventLoopGroup(eventLoopGroup).build();
            } catch (Exception e) {
                logger.error("ios get pushy apns client failed!");
                e.printStackTrace();
            }
        }*/
        return apnsClient;
    }

}
