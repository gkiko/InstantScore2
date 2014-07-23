package subscribtion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Utils;
import database.DbManager;
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
		String code = "";
		if(codeRequestTimeoutPassed(phoneNum)){
			code = Utils.generateCode(6);
			res.setResult(code);
		}else{
			res.setErrorMessage("repeatedly asking for code");
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
	
	public Result eligibleForSubscription(String phoneNum, String code) {
		Result res = new Result();
		if(!securityCodeValid(phoneNum, code)){
			LOGGER.debug("security code invalid");
			res.setErrorMessage("invalid security code");
			return res;
		}
		if(messageLimitReached(phoneNum)){
			LOGGER.debug("message limit reached");
			res.setErrorMessage("message limit reached");
			return res;
		}
		
		return res;
	}
	
	private boolean securityCodeValid(String phoneNum, String code) {
		String codeFromDb;
		if(code == null) {
			return false;
		}
		codeFromDb = subscriberData.getCodeByUser(phoneNum);
		LOGGER.debug("code is : "+codeFromDb+ " "+(codeFromDb.equals(code)));
		return code.equals(codeFromDb);
	}
	
	private boolean messageLimitReached(String phoneNum){
		return DbManager.getInstance().isMessageLimitFullForUser(phoneNum);
	}
	
}
