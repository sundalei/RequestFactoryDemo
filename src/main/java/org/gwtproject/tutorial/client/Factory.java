package org.gwtproject.tutorial.client;

import java.util.List;

import org.gwtproject.tutorial.server.Contact;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;

public interface Factory extends RequestFactory {
	
	ContactRequest createContactRequest();
	
	@Service(value = Contact.class)
	public interface ContactRequest extends RequestContext {
		
		Request<Integer> count();
		Request<ContactProxy> find(Long id);
		Request<List<ContactProxy>> findAllContacts();
		
		InstanceRequest<ContactProxy, Void> persist();
		InstanceRequest<ContactProxy, Void> remove();
	}
}
