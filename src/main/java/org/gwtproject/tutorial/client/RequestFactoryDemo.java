package org.gwtproject.tutorial.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class RequestFactoryDemo implements EntryPoint {
  
  public void onModuleLoad() {
    
    RootPanel.get().add(new TestPanel());
  }
}
