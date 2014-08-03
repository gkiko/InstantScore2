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
	static final String authToken = "0c863eb8b4328910e1c657ec7988b4ef";
	static final String accountSid = "AC5a22a858703af1d045951b8649d65a82";
	static final String myPhoneNum = "+15005550006";

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

	public void sendMsgToUser(String txt, String phoneNum) throws TwilioRestException {
			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Body", txt));
			params.add(new BasicNameValuePair("To", phoneNum));
			params.add(new BasicNameValuePair("From", myPhoneNum));

			Message message = messageFactory.create(params);
			logger.debug("send success "+message.getSid()+" "+message.getBody());
	}
}