package daemon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import common.Message;
import common.beans.ConnectedObject;
import daemon.business.suspectBehavior.InactivityDetective;

public class InactivityDetectiveTest {

	private InactivityDetective inactivityDetective;
	private List<ConnectedObject> listObjects;
	private CacheReport report;

	@Before
	public void init() {

		report = new CacheReport(5);
		listObjects = new ArrayList<ConnectedObject>();
		List<Message> msgs = new ArrayList<Message>();

		//Construct the objects
		listObjects.add(new ConnectedObject(1, "", true, 1, "", 5, 10));
		listObjects.add(new ConnectedObject(2, "", false, 1, "", 5, 10));
		listObjects.add(new ConnectedObject(3, "", true, 1, "", 5, 10));
		listObjects.add(new ConnectedObject(4, "", false, 1, "", 5, 10));
		listObjects.add(new ConnectedObject(5, "", true, 1, "", 5, 10));
		listObjects.add(new ConnectedObject(6, "", false, 1, "", 5, 10));

		// Construct the reports
		msgs.add(new Message(null));
		msgs.get(0).setTime(new Timestamp(System.currentTimeMillis()-4*60*1000));

		msgs.add(new Message(null));
		msgs.get(1).setTime(new Timestamp(System.currentTimeMillis()-9*60*1000));

		msgs.add(new Message(null));
		msgs.get(2).setTime(new Timestamp(System.currentTimeMillis()-6*60*1000));

		msgs.add(new Message(null));
		msgs.get(3).setTime(new Timestamp(System.currentTimeMillis()-11*60*1000));

		// Add the reports
		report.addReport(1, msgs.get(0));
		report.addReport(2, msgs.get(1));
		report.addReport(3, msgs.get(2));
		report.addReport(4, msgs.get(3));

		// Construct the detective
		inactivityDetective = new InactivityDetective(report);
	}

	@Test 
	public void testInvestiguate() throws IOException {

		// The object is active and was seen in its intervalle.
		assertTrue(inactivityDetective.isActive(listObjects.get(0), false));
		// The object is inactive and was seen in its intervalle.
		assertTrue(inactivityDetective.isActive(listObjects.get(1), false));

		// The object is active and was not seen in its intervalle.
		assertFalse(inactivityDetective.isActive(listObjects.get(2), false));
		// The object is inactive and was not seen in its intervalle.
		assertFalse(inactivityDetective.isActive(listObjects.get(3), false));

		// The object was never seen and the cache was just launch
		report.setLaunchDate(new Timestamp(System.currentTimeMillis()-1*60*1000));
		assertTrue(inactivityDetective.isActive(listObjects.get(4), false));

		// The object was never seen and the cache was launch for upper than the tolerate value of the object
		// (with the state on)
		report.setLaunchDate(new Timestamp(System.currentTimeMillis()-6*60*1000));
		assertFalse(inactivityDetective.isActive(listObjects.get(4), false));

		// The object was never seen and the cache was launch for upper than the tolerate value of the object
		// (with the state off)
		report.setLaunchDate(new Timestamp(System.currentTimeMillis()-11*60*1000));
		assertFalse(inactivityDetective.isActive(listObjects.get(5), false));
	}

}
