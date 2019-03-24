package org.gwtproject.tutorial.client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import org.gwtproject.tutorial.client.ContactProxy.PhoneProxy;
import org.gwtproject.tutorial.client.Factory.ContactRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class TestPanel extends Composite {

	private static TestPanelUiBinder uiBinder = GWT.create(TestPanelUiBinder.class);

	interface TestPanelUiBinder extends UiBinder<Widget, TestPanel> {
	}
	
	Logger log = Logger.getLogger("");
	Factory factory;
	
	@UiField
	TextArea txtArea;
	
	@UiField
	TextBox txtInput;

	public TestPanel() {
		factory = GWT.create(Factory.class);
		factory.initialize(new SimpleEventBus());
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("btnPersist")
	public void persist(ClickEvent event) {
		ContactRequest context = factory.createContactRequest();
		String rand = genRandomString();
		
		PhoneProxy phone = context.create(PhoneProxy.class);
		phone.setType("Home");
		phone.setNumber("555-" + rand);
		
		ContactProxy contact = context.create(ContactProxy.class);
		contact.setEmail(rand + "@example.com");
		contact.setName(rand);
		contact.setPhones(Arrays.asList(phone));
		contact.setNotes("Random notes for " + rand);
		
		context.persist().using(contact).fire();
		
	}
	
	private String genRandomString() {
		return Integer.toString((int) (Math.random() * 99999));
	}
	
	private Long txtInputAsLong() {
		return Long.parseLong(txtInput.getText());
	}

	@UiHandler("btnCount")
	public void count (ClickEvent event) {
		Receiver<Integer> rec = new Receiver<Integer>() {

			@Override
			public void onSuccess(Integer count) {
				String message = txtArea.getText();
				if(message != null && !"".equals(message)) {
					message += "\r\n";
				}
				txtArea.setText(message + "total account number: " + count.toString());
			}
			
		};
		
		factory.createContactRequest().count().fire(rec);
	}
	
	@UiHandler("btnList")
	public void list(ClickEvent event) {
		Receiver<List<ContactProxy>> rec = new Receiver<List<ContactProxy>> () {

			@Override
			public void onSuccess(List<ContactProxy> response) {
				for(ContactProxy c : response) {
					String message = txtArea.getText();
					if(message != null && !"".equals(message)) {
						message += "\r\n";
					}
					txtArea.setText(message + "Contact: " + c.getId() + "=" + c.getEmail());
				}
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			
		};
		
		factory.createContactRequest().findAllContacts().fire(rec);
	}
	
	@UiHandler("btnFetch")
	public void fetch(ClickEvent event) {
		Receiver<ContactProxy> rec = new Receiver<ContactProxy>() {

			@Override
			public void onSuccess(ContactProxy contact) {
				String message = txtArea.getText();
				if(message != null && !"".equals(message)) {
					message += "\r\n";
				}
				
				StringBuilder builder = new StringBuilder();
				builder.append("id: " + contact.getId()).append(" ");
				builder.append("name: " + contact.getName()).append(" ");
				builder.append("email: " + contact.getEmail()).append(" ");
				if (contact.getPhones() != null) {
					for (PhoneProxy p : contact.getPhones())
						builder.append("phone: " + p.getType() + "/" + p.getNumber()).append(" ");
				} else {
					builder.append("phone: null").append(" ");
				}
				builder.append("notes: " + contact.getNotes());
				txtArea.setText(message + builder.toString());
			}
		};

		factory.createContactRequest().find(((Long) Long.parseLong(txtInput.getText()))).with("phones").fire(rec);
	}
	
	@UiHandler("btnUpdate")
	public void update(ClickEvent event) {
		Receiver<ContactProxy> rec = new Receiver<ContactProxy> () {
			@Override
			public void onSuccess(ContactProxy contact) {
				ContactRequest ctx = factory.createContactRequest();
				ContactProxy editableContact = ctx.edit(contact);
				editableContact.setNotes("Late updated " + new Date());
				ctx.persist().using(editableContact).fire();
			}
		};
		
		factory.createContactRequest().find(txtInputAsLong()).fire(rec);
	}
	
	@UiHandler("btnDelete")
	public void delete(ClickEvent event) {
		Receiver<ContactProxy> rec = new Receiver<ContactProxy> () {
			@Override
			public void onSuccess(ContactProxy contact) {
				ContactRequest ctx = factory.createContactRequest();
				ContactProxy editableContact = ctx.edit(contact);
				ctx.remove().using(editableContact).fire();
			}
		};
		
		factory.createContactRequest().find(txtInputAsLong()).fire(rec);
	}
	
	@UiHandler("btnDeleteAll")
	public void deleteAll(ClickEvent event) {
		Receiver<List<ContactProxy>> rec = new Receiver<List<ContactProxy>>() {
			
			@Override
			public void onSuccess(List<ContactProxy> response) {
				ContactRequest ctx = factory.createContactRequest();
				for(ContactProxy contact : response) {
					ctx.remove().using(ctx.edit(contact));
				}
				ctx.fire();
			}
		};
		
		factory.createContactRequest().findAllContacts().fire(rec);
	}
	
	@UiHandler("btnPersistInvalid")
	public void persistInvalid(ClickEvent event) {
		ContactRequest context = factory.createContactRequest();
		
		ContactProxy contact = context.create(ContactProxy.class);
		
		contact.setEmail("invalid email");
		contact.setName("");
		
		String notes = "";
		for(int i = 0; i < 20; i++) {
			notes += "too-long";
		}
		contact.setNotes(notes);
		
		Receiver<Void> receiver = new Receiver<Void>() {
			
			@Override
			public void onSuccess(Void response) {
				String message = txtArea.getText();
				if(message != null && !"".equals(message)) {
					message += "\r\n";
				}
				txtArea.setText(message + "We passed validation");
			}
			
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
				for(ConstraintViolation<?> err : violations) {
					String message = txtArea.getText();
					if(message != null && !"".equals(message)) {
						message += "\r\n";
					}
					txtArea.setText(message + err.getPropertyPath() + " : " + err.getMessage());
				}
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				String message = txtArea.getText();
				if(message != null && !"".equals(message)) {
					message += "\r\n";
				}
				txtArea.setText(message + "Server failure: " + error.getMessage());
			}
		};
		
		context.persist().using(contact).fire(receiver);
	}

}
