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
		//We get a list of current sensors
		
		Message requestCaptorsList = new Message(MessageType.LISTOBJECTS);
		String answerCaptors = Connector.contactServer(ToolSerialize.messageToJSON(requestCaptorsList));
		List<List<String>> listObjects = ((MsgListResult) ToolSerialize.jsonToMessage(answerCaptors)).getListResult();
		System.out.println("The sensors list was loaded from the server!");

		//We aggregate objects by their referentiel types
		sortedCaptors = new ArrayList<List<String>>();
		//Init
		for(int i=0; i<listReferentiels.size();i++)
			sortedCaptors.add(new ArrayList<String>());
		int cursor;
		for(List<String> sensor : listObjects) {
			cursor = 0;
			for(List<String> reference : listReferentiels) {
				if(sensor.get(1).trim().equalsIgnoreCase(reference.get(0))) {
					if(sensor.get(1).equals("Bracelet RFID") || (!sensor.get(1).equals("Bracelet RFID") 
							&& sensor.get(7) != null)){
						sortedCaptors.get(cursor).add(sensor.get(0));
						continue;
					}
				}
				cursor++;
			}
		}
		System.out.println("The sensors list was sorted!");
		System.out.println("#####################################################\n");
	}

	private void menu() {
		System.out.println("##################### Main Menu #####################");
		System.out.println("Please choose which simulation you want to run : ");
		System.out.println("1 : Specify the sensors to operate (default choice)");
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
		System.out.println("Which type of sensor do you want to operate?");
		int i = 1;
		for(List<String> sensorType : listReferentiels) {
			if(i==1)
				System.out.println(i + " : " + sensorType.get(0) + " (" + sortedCaptors.get(i-1).size() +  " sensors) (default choice)");
			else
				System.out.println(i + " : " + sensorType.get(0) + " (" + sortedCaptors.get(i-1).size() +  " sensors)");
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

	private void launchAlert(String sensorType, List<String> listSensors) {
		final Integer maxNumberSensorsDisplayed = 15;
		System.out.println("############# Launch specific alerts (2) ############");
		System.out.println("With which sensor do you want to launch an alert?");
		System.out.println("(only the first " + maxNumberSensorsDisplayed + " sensors are displayed)");
		int i = 1;
		for(String sensor : listSensors) {
			if(i>maxNumberSensorsDisplayed)
				break;
			if(i==1)
				System.out.println(i++ + " : " + sensorType + " n°" + sensor +  " (default choice)");
			else
				System.out.println(i++ + " : " + sensorType + " n°" + sensor);
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
			if(!(0<answer && answer<=maxNumberSensorsDisplayed && answer<=listSensors.size()))
				System.out.print("Invalid! Try again : ");
		}while(!(0<answer && answer<=maxNumberSensorsDisplayed && answer<=listSensors.size()));
		CategoryObject category = null;
		if(sensorType.trim().equals("Capteur de fumée"))
			category = new CategorySmoke();
		if(sensorType.trim().equals("Capteur de présence"))
			category = new CategoryTemperature();
		Integer id = Integer.parseInt(listSensors.get(answer-1));
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
