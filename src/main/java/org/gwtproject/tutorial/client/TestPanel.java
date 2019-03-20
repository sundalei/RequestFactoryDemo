package org.gwtproject.tutorial.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class TestPanel extends Composite {

	private static TestPanelUiBinder uiBinder = GWT.create(TestPanelUiBinder.class);

	interface TestPanelUiBinder extends UiBinder<Widget, TestPanel> {
	}
	
	Logger log = Logger.getLogger("");
	Factory factory;

	public TestPanel() {
		factory = GWT.create(Factory.class);
		factory.initialize(new SimpleEventBus());
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("btnCount")
	public void count (ClickEvent event) {
		Receiver<Integer> rec = new Receiver<Integer>() {

			@Override
			public void onSuccess(Integer count) {
				GWT.log(count.toString());
				log.info(count.toString());
			}
			
		};
		
		factory.createContactRequest().count().fire(rec);
	}

}
