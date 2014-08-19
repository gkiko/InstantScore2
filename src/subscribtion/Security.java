package subscribtion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Utils;
import database.SubscriberData;

public class Security {
	static Security securityManager;
	private static final Logger LOGGER = LoggerFactory.getLogger(Security.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SubscriberData subscriberData;
	
	private Security(){
		subscriberData = new SubscriberData();
	}
	
	public static Security getInstance(){
		if(securityManager == null){
			securityManager = new Security();
		}
		return securityManager;
	}
	
	public Result tryGenerateCode(String phoneNum) {
		Result res = new Result();
		if(phoneNum.isEmpty()){
			res.setErrorMessage("phone number missing");
			return res;
		}
		
		String code = "";
		if(codeRequestTimeoutPassed(phoneNum)){
			code = Utils.generateCode(6);
			res.setResult(code);
		}else{
			res.setErrorMessage("repeatedly asking for code");
			LOGGER.debug("repeatedly asking for code");
		}
		return res;
	}
	
	private boolean codeRequestTimeoutPassed(String phoneNum){
		long hoursDiff;
		Date codeGenerationTime, currentTime;
		String timeStr;
		timeStr = subscriberData.getLastCodeRequestTime(phoneNum);
		try {
			codeGenerationTime = dateFormat.parse(timeStr);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			return false;
		}
		currentTime = new Date();
		
		hoursDiff = (currentTime.getTime() - codeGenerationTime.getTime())/3600000;
		return hoursDiff>23;
	}
	
	public Result eligibleForSubscription(String phoneNum, String code, String matchId) {
		Result res = new Result();
		if(!securityCodeValid(phoneNum, code)){
			LOGGER.debug("security code invalid");
			res.setErrorMessage("invalid security code");
		}
		
		return res;
	}
	
	private boolean securityCodeValid(String phoneNum, String code) {
		String codeFromDb;
		if(code == null) {
			return false;
		}
		codeFromDb = subscriberData.getCodeByUser(phoneNum);
		LOGGER.debug("code from db : "+codeFromDb+ " actual : "+code);
		return code.equals(codeFromDb);
	}
		
	public boolean alreadySubscribed(String phoneNum, String matchId){
		return subscriberData.userSubscribedTo(phoneNum, matchId);
	}
	
}
