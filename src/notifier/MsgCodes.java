package notifier;

import com.twilio.sdk.TwilioRestException;

public class MsgCodes {

	public static final String getErrorMessageForCode(TwilioRestException e) {
		int errorCode = e.getErrorCode();
		ErrorNotification errorNotif = ErrorNotification.getErrorNotificationViaErroCode(errorCode);
		return errorNotif.getMessageText();
	}
	
	
	
}
