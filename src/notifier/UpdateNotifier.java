package notifier;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

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
	
	public UpdateNotifier(){
		diffFinder = new DiffFinder();
		msgTextGenerator = new MsgTextGenerator();
		subscriberData = new SubscriberData();
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
	
	private void checkIfUpdated() {
		String msgText;
		List<String> subscriberPhoneNumbers;
		List<DiffData> diffs = diffFinder.getDiffs(newList, oldList);
		for(DiffData diffData : diffs){
			msgText = msgTextGenerator.getMsgText(diffData);
			
			subscriberPhoneNumbers = subscriberData.getSubscriberPhoneNumbersForMatch(diffData.getNewMatch(), 30);
			for(final String phoneNum : subscriberPhoneNumbers){
				Queuer.queueJob(msgText, phoneNum, new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						subscriberData.countSentMessageForUser(phoneNum);
						return null;
					}
					
				});
			}
		}
//		Match m = new Match();
//		m.setTeam1("Fortuna Dusseldorf");
//		m.setTeam2("Braunschweig");
//		List<String> subscriberPhoneNumbers = subscriberData.getSubscriberPhoneNumbersForMatch(m, 2);
//		System.out.println(m.getMatchId()+" "+subscriberPhoneNumbers.size());
//		for(final String phoneNum : subscriberPhoneNumbers){
//			Queuer.queueJob("test", phoneNum, new Callable<Void>() {
//
//				@Override
//				public Void call() throws Exception {
//					subscriberData.countSentMessageForUser(phoneNum);
//					return null;
//				}
//				
//			});
//		}
	}
	
}
