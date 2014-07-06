package notifier;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.League;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.SubscriberData;

/**
 * Is responsible for finding updates in matches and sending messages to the users
 * @author gkiko
 *
 */
public class UpdateNotifier implements Observer{
	final static Logger logger = LoggerFactory.getLogger(UpdateNotifier.class);
	private List<League> oldList, newList;
	private DiffFinder diffFinder;
	private MsgTextGenerator msgTextGenerator;
	private SubscriberData subscriberData;
	private MsgSender msgSender;
	
	public UpdateNotifier(){
		diffFinder = new DiffFinder();
		msgTextGenerator = new MsgTextGenerator();
		subscriberData = new SubscriberData();
		msgSender = MsgSender.init();
	}

	@Override
	public void update(Observable o, Object arg) {
		List<League> newList = (List<League>) arg;
		
		if(oldList == null){
			oldList = newList;
		}else{
			this.newList = newList;
			checkIfUpdated();
			oldList = newList;
		}
	}
	
	private void checkIfUpdated(){
		String msgText;
		List<String> subscriberPhoneNumbers;
		List<DiffData> diffs = diffFinder.getDiffs(newList, oldList);
		for(DiffData diffData : diffs){
			msgText = msgTextGenerator.getMsgText(diffData);
			
			subscriberPhoneNumbers = subscriberData.getSubscriberPhoneNumbersForMatch(diffData.getMatch());
			for(String phoneNum : subscriberPhoneNumbers){
				msgSender.sendMsgToUser(msgText, phoneNum);
			}
		}
	}
	
}
