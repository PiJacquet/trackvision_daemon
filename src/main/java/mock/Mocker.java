package mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.Message;
import common.MessageType;
import common.MsgListResult;
import common.ToolSerialize;

public class Mocker {

	protected Scanner sc;

	protected List<List<String>> listReferentiels;
	protected List<List<String>> sortedCaptors;

	public Mocker() {
		System.out.println("TrackVision Mock :  launched!\n");
		new ConfigurationMock();
		init();
		menu();
	}

	private void init() {
		sc = new Scanner(System.in);

		System.out.println("################### Initialisation ##################");
		//We get a list of current objects

		//We get the referentiel of objects
		Message requestReferentielList = new Message(MessageType.LISTREFERENTIELS);
		String answerReferentiel = Connector.contactServer(ToolSerialize.messageToJSON(requestReferentielList));
		listReferentiels = ((MsgListResult) ToolSerialize.jsonToMessage(answerReferentiel)).getListResult();
		System.out.println("The referential type list was loaded from the server!");

		Message requestCaptorsList = new Message(MessageType.LISTOBJECTS);
		String answerCaptors = Connector.contactServer(ToolSerialize.messageToJSON(requestCaptorsList));
		List<List<String>> listObjects = ((MsgListResult) ToolSerialize.jsonToMessage(answerCaptors)).getListResult();
		System.out.println("The objects list was loaded from the server!");

		//We aggregate objects by their referentiel types
		sortedCaptors = new ArrayList<List<String>>();
		//Init
		for(int i=0; i<listReferentiels.size();i++)
			sortedCaptors.add(new ArrayList<String>());
		int cursor;
		for(List<String> object : listObjects) {
			cursor = 0;
			for(List<String> reference : listReferentiels) {
				if(object.get(1).trim().equalsIgnoreCase(reference.get(0))) {
					sortedCaptors.get(cursor).add(object.get(0));
					continue;

				}
				cursor++;
			}
		}
		System.out.println("The objects list was sorted!");
		System.out.println("#####################################################\n");
	}

	private void menu() {
		System.out.println("##################### Main Menu #####################");
		System.out.println("Please choose which simulation you want to run : ");
		System.out.println("1 : Specify the objects to operate (default choice)");
		System.out.println("2 : Simulate random alerts");
		String answer;
		System.out.print("Your choice : ");
		do {
			answer = sc.nextLine();
			if(answer.equals(""))
				answer="1";
			if(!(answer.equals("1") || answer.equals("2")))
				System.out.print("Invalid! Try again : ");
		}while(!(answer.equals("1") || answer.equals("2")));
		System.out.println("#####################################################\n");
		if(answer.equals("1"))
			while(true)
				specificAlerts();
		else
			randomAlerts();
	}

	private void specificAlerts() {
		System.out.println("############# Launch specific alerts (1) ############");
		System.out.println("Which type of object do you want to operate?");
		int i = 1;
		for(List<String> objectType : listReferentiels) {
			if(i==1)
				System.out.println(i + " : " + objectType.get(0) + " (" + sortedCaptors.get(i-1).size() +  " objects) (default choice)");
			else
				System.out.println(i + " : " + objectType.get(0) + " (" + sortedCaptors.get(i-1).size() +  " objects)");
			i++;
		}
		String rawAnswer;
		int answer=0;
		System.out.print("Your choice : ");
		do {
			try {
				rawAnswer = sc.nextLine();
				if(rawAnswer.equals(""))
					answer=1;
				else
					answer=Integer.parseInt(rawAnswer);
			}catch(NumberFormatException e) {
				answer=0;
			}
			if(!(0<answer && answer<=sortedCaptors.size() && sortedCaptors.get(answer-1).size()>0))
				System.out.print("Invalid! Try again : ");
		}while(!(0<answer && answer<=sortedCaptors.size() && sortedCaptors.get(answer-1).size()>0));
		System.out.println("#####################################################\n");
		launchAlert(listReferentiels.get(answer-1).get(0), sortedCaptors.get(answer-1));
	}

	private void launchAlert(String objectType, List<String> listobjects) {
		final Integer maxNumberobjectsDisplayed = 15;
		System.out.println("############# Launch specific alerts (2) ############");
		System.out.println("With which object do you want to launch an alert?");
		System.out.println("(only the first " + maxNumberobjectsDisplayed + " objects are displayed)");
		int i = 1;
		for(String object : listobjects) {
			if(i>maxNumberobjectsDisplayed)
				break;
			if(i==1)
				System.out.println(i++ + " : " + objectType + " n°" + object +  " (default choice)");
			else
				System.out.println(i++ + " : " + objectType + " n°" + object);
		}
		String rawAnswer;
		int answer=0;
		System.out.print("Your choice : ");
		do {
			try {
				rawAnswer = sc.nextLine();
				if(rawAnswer.equals(""))
					answer=1;
				else
					answer=Integer.parseInt(rawAnswer);
			}catch(NumberFormatException e) {
				answer=0;
			}
			if(!(0<answer && answer<=maxNumberobjectsDisplayed && answer<=listobjects.size()))
				System.out.print("Invalid! Try again : ");
		}while(!(0<answer && answer<=maxNumberobjectsDisplayed && answer<=listobjects.size()));
		CategoryObject category = null;
		if(objectType.trim().equals("Capteur de fumée"))
			category = new CategorySmoke();
		if(objectType.trim().equals("Capteur de présence"))
			category = new CategoryTemperature();
		Integer id = Integer.parseInt(listobjects.get(answer-1));
		category.launchAlert(id);
		System.out.println("An alert was launched!");
		System.out.println("#####################################################\n");
	}

	private void randomAlerts() {
		System.out.println("################ Launch random alerts ###############");
		CategorySmoke categorySmoke = new CategorySmoke(listReferentiels.get(2).get(0), sortedCaptors.get(2));
		Thread threadSmoke = new Thread(categorySmoke);
		threadSmoke.start();

		CategoryTemperature categoryTemperature = new CategoryTemperature(listReferentiels.get(4).get(0), sortedCaptors.get(4));
		Thread threadTemperature = new Thread(categoryTemperature);
		threadTemperature.start();
	}

}
