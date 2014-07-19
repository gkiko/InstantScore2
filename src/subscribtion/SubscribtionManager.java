package subscribtion;

import notifier.MsgSender;
import database.SubscriberData;

public class SubscribtionManager {
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
	
	public void fulfilCodeRequest(String phoneNum) {
		Result res;
		String code;
		res = securityManger.tryGenerateCode(phoneNum);
		if(!res.isValid()){
			// return error
		}
		code = res.getResult();
		subscriberData.saveCodeForUser(phoneNum, code);
		msgSender.sendMsgToUser("Your security code: "+code, phoneNum);
	}
	
	public void fulfilSubscribtionRequest(String phoneNum, String code, String matchId) throws Exception{
		Result res;
		res = securityManger.eligibleForSubscription(phoneNum, code);
		if(!res.isValid()){
			// return error
		}
		subscriberData.saveMatchForUser(phoneNum, matchId);
	}
}
