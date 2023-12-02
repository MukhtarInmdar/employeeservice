package com.sumadhura.employeeservice.util;
import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.turo.pushy.apns.util.concurrent.PushNotificationResponseListener;

/**
 * ios news push
 */
public class IOSPush {

    private static final Logger logger = LoggerFactory.getLogger(IOSPush.class);

     private static ApnsClient apnsClient = null;

    private static final Semaphore semaphore = new Semaphore(10000);//Semaphore, also known as semaphore, is a concept in the operating system. In Java concurrent programming, semaphore controls the number of concurrent threads.

   /**
     *
     * ios push
     * @param deviceTokens
     * @param alertTitle
     * @param alertBody
     * @param contentAvailable true: indicates the product release push service false: indicates the product test push service
     * @param customProperty additional parameters
     * @param badge If the badge is less than 0, the corner badge in the upper right corner will not be pushed. It is mainly used to update this status when the message box is added or read.
     */
    @SuppressWarnings("rawtypes")
    public static void push(final List<String> deviceTokens, String alertTitle, String alertBody, boolean contentAvailable,
    		Map<String, Object> customProperty, int badge) {

         long startTime = System.currentTimeMillis();

        ApnsClient apnsClient = APNSConnect.getAPNSConnect(ApnsClientBuilder.PRODUCTION_APNS_HOST);

        long total = deviceTokens.size();

        final CountDownLatch latch = new CountDownLatch(deviceTokens.size());//Every time a task is completed (the thread does not necessarily need to be completed), the latch is reduced by 1, until all tasks are completed, the next stage of tasks can be executed Improve performance

        final AtomicLong successCnt = new AtomicLong(0);//Thread safe counter

        long startPushTime = System.currentTimeMillis();

        for (String deviceToken: deviceTokens) {

            ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

            if (alertBody != null && alertTitle != null) {
                payloadBuilder.setAlertBody(alertBody);
                payloadBuilder.setAlertTitle(alertTitle);
            }

           //If the badge is less than 0, the corner label in the upper right corner will not be pushed, mainly used to update this status when the message box is added or read
            if (badge> 0) {
                payloadBuilder.setBadgeNumber(badge);
            }

           //Put all the additional parameters in
            if (customProperty != null) {
                for (Map.Entry<String, Object> map: customProperty.entrySet()) {
                    payloadBuilder.addCustomProperty(map.getKey(), map.getValue());
                }
            }

           //true: indicates the product release push service false: indicates the product test push service
            payloadBuilder.setContentAvailable(contentAvailable);

            String payload = payloadBuilder.buildWithDefaultMaximumLength();
            final String token = TokenUtil.sanitizeTokenString(deviceToken);
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.sumadhura.sampleNotification", payload);

            try {
                semaphore.acquire();//Get an allowed opportunity from the semaphore
            } catch (Exception e) {
                logger.error("ios push get semaphore failed, deviceToken:{}", deviceToken);//There are too many threads, there is no extra semaphore to get
                e.printStackTrace();
            }

            final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                sendNotificationFuture = apnsClient.sendNotification(pushNotification);

           //------------------------------------------------ -------------------------------------------------- -
            try {
                final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
//System.out.println(sendNotificationFuture.isSuccess());
//System.out.println(pushNotificationResponse.isAccepted());

                sendNotificationFuture.addListener(new PushNotificationResponseListener<SimpleApnsPushNotification>() {

                    @Override
                    public void operationComplete(final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future) throws Exception {
                       //When using a listener, callers should check for a failure to send a
                       //notification by checking whether the future itself was successful
                       //since an exception will not be thrown.
                        if (future.isSuccess()) {
                            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                                    sendNotificationFuture.getNow();
                            if (pushNotificationResponse.isAccepted()) {
                                successCnt.incrementAndGet();
                            } else {
                                Date invalidTime = pushNotificationResponse.getTokenInvalidationTimestamp();
                                logger.info("Notification rejected by the APNs gateway: "+ pushNotificationResponse.getRejectionReason());
                                if (invalidTime != null) {
                                    logger.info("\t…and the token is invalid as of "+ pushNotificationResponse.getTokenInvalidationTimestamp());
                                }
                            }
                           //Handle the push notification response as before from here.
                        } else {
                           //Something went wrong when trying to send the notification to the
                           //APNs gateway. We can find the exception that caused the failure
                           //by getting future.cause().
                            future.cause().printStackTrace();
                        }
                        latch.countDown();
                        semaphore.release();//Release allowed, return the occupied semaphore
                    }
                });
               //------------------------------------------------ ------------------

               /*if (pushNotificationResponse.isAccepted()) {
                    System.out.println("Push notification accepted by APNs gateway.");
                } else {
                    System.out.println("Notification rejected by the APNs gateway: "+
                            pushNotificationResponse.getRejectionReason());

                    if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                        System.out.println("\t…and the token is invalid as of "+
                                pushNotificationResponse.getTokenInvalidationTimestamp());
                    }
                }*/
            } catch (final ExecutionException e) {
                System.err.println("Failed to send push notification.");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           //------------------------------------------------ -------------------------------------------------- -

        }

//try {
//latch.await(20, TimeUnit.SECONDS);
//} catch (Exception e) {
//logger.error("ios push latch await failed!");
//e.printStackTrace();
//}

        long endPushTime = System.currentTimeMillis();

        logger.info("test pushMessage success. [total push" + total + "pieces][success" + (successCnt.get()) + "pieces],totalcost= "+ (endPushTime-startTime) + ", pushCost=" + (endPushTime-startPushTime));
    }
}