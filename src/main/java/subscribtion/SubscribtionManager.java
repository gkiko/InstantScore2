package subscribtion;

import java.sql.SQLException;

import notifier.MsgSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.sdk.TwilioRestException;

import database.SubscriberData;

public class SubscribtionManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscribtionManager.class);
	private static SubscribtionManager subscribtionManager;
	private Security securityManger;
	private SubscriberData subscriberData;
	private MsgSender msgSender;
	
	private SubscribtionManager(){
		securityManger = Security.getInstance();
		subscriberData = new SubscriberData();
		msgSender = MsgSender.getInstance();
	}
	
	public static SubscribtionManager getInstance(){
		if(subscribtionManager == null){
			subscribtionManager = new SubscribtionManager();
		}
		return subscribtionManager;
	}
	
	public Result fulfilCodeRequest(String phoneNum) {
		Result res;
		String code;
		res = securityManger.tryGenerateCode(phoneNum);
		if(!res.isValid()){
			LOGGER.debug("code request by "+phoneNum +" wasn't satisfied");
			return res;
		}
		code = res.getResult();
		LOGGER.debug(phoneNum+" requested code: "+code);
		
		try {
			msgSender.sendMsgToUser("Your security code: "+code, phoneNum);
			subscriberData.saveCodeForUser(phoneNum, code);
		} catch (TwilioRestException e) {
			LOGGER.error("message error", e);
			res.setErrorMessage(MsgCodes.getErrorMessageForCode(e));
		} catch (SQLException e) {
			res.setErrorMessage("something went wrong");
		}
		return res;
	}
	
	public Result fulfilSubscribtionRequest(String phoneNum, String code, String matchId) {
		Result res;
		res = securityManger.eligibleForSubscription(phoneNum, code, matchId);
		if(!res.isValid()){
			LOGGER.debug("subscribtion request by "+phoneNum +" wasn't satisfied");
			return res;
		}
		if(securityManger.alreadySubscribed(phoneNum, matchId)){
			LOGGER.debug("already subscribed");
			subscriberData.removeMatchForUser(phoneNum, matchId);
			return res;
		}
		
		try {
			subscriberData.saveMatchForUser(phoneNum, matchId);
		} catch (SQLException e) {
			res.setErrorMessage("something went wrong");
		}
		return res;
	}
}
