package org.gwtproject.tutorial;

import org.gwtproject.tutorial.client.RequestFactoryDemoTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class RequestFactoryDemoSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for RequestFactoryDemo");
    suite.addTestSuite(RequestFactoryDemoTest.class);
    return suite;
  }
}
