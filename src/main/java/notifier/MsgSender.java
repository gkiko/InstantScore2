package notifier;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

public class MsgSender {
	static final Logger logger = LoggerFactory.getLogger(MsgSender.class);
	private final String authToken = "313a2ccaca5f1f2c013100a69106cc43";//System.getenv("AUTH_TOKEN");
	private final String accountSid = "AC7d19293c11f8c281ef3728f56308ced1";//System.getenv("ACCOUNT_SID");
	private final String myPhoneNum = "+12292562076";//System.getenv("SRC_PHONE_NUM");

	static MsgSender msgSender;
	private static TwilioRestClient client;

	public static MsgSender getInstance() {
		if (msgSender == null) {
			msgSender = new MsgSender();
		}
		return msgSender;
	}

	private MsgSender() {
		client = new TwilioRestClient(accountSid, authToken);
	}

	public Message sendMsgToUser(String txt, String phoneNum) throws TwilioRestException {
			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Body", txt));
			params.add(new BasicNameValuePair("To", phoneNum));
			params.add(new BasicNameValuePair("From", myPhoneNum));

			Message message = messageFactory.create(params);
			logger.debug("send success "+message.getSid()+" "+message.getBody());
			return message;
	}
}