package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorCreatePage extends AbstractEntityEditPageContainerManagedImpl<Actor> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		action = Action.CREATE;
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.FIELD_ACTION, Action.CREATE);
		arguments.put(Form.FIELD_ENTITY, new Actor());
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(Actor.FIELD_FIRST_NAME,Actor.FIELD_LAST_NAMES,Actor.FIELD_ELECTRONIC_MAIL_ADDRESS,Actor.FIELD_FUNCTIONS
						,Actor.FIELD_PASSWORD,Actor.FIELD_PASSWORD_CONFIRMATION);
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> arguments = super.getInputArguments(form, fieldName);
				if(Actor.FIELD_FUNCTIONS.equals(fieldName))
					arguments.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Function>() {
						@Override
						public Collection<Function> computeChoices(AbstractInputChoice<Function> input) {
							Collection<Function> functions = EntityReader.getInstance().readMany(Function.class);
							return functions;
						}
					});
				return arguments;
			}
		});
		
		arguments.put(Form.FIELD_LISTENER, new Form.Listener.AbstractImpl() {
			@Override
			public void redirect(Form form, Object request) {
				JsfController.getInstance().redirect("actorListView");
			}
		});
		return arguments;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Création d'un compte utilisateur";
	}
}