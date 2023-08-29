package fr.nablihatem3.costomermanager.utils;

import com.sarbacane.api.Authentication.AuthenticationManager;
import com.sarbacane.api.Messages.MessagesManager;
import com.sarbacane.api.Messages.SBSmsMessage;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.twilio.rest.api.v2010.account.Message.creator;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 27/08/2023
 */
@Slf4j
public class SmsUtils {
    public static final String SERVICE_SID = "MGb63fdf06b83311b5f5a58d3cbaf86aee";
    public static final String SID_KEY = "ACab6be134d129452852e17f88750fb701";
    public static final String TOKEN_KEY= "a76195146d64c5349d238d8006fc4d8c";
    public static final String MY_SMS_APIKEY = "48ed60af85c849cea4d9a218a3fce95d";

    public static void sendSMS(String to, String messageBody) {
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber(to), SERVICE_SID , messageBody).create();
        log.info(message.toString());
    }

    public  static void sendSMSBySarbacane(String to, String messageBody) throws IOException {
        AuthenticationManager.setSmsApikey(MY_SMS_APIKEY);
        SBSmsMessage message = new SBSmsMessage();
        message.setType("notification");
        message.setMessage(messageBody);
        message.setNumber(to);
        message.setSender("SBSMS");
        message.setCategory("confirmationCode");
        message.setCampaignName("Authentication confirmation code");
        MessagesManager.sendSmsMessage(message);
    }
}

