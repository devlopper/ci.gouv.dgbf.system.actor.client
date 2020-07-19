package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.component.window.WindowBuilder;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.AccountRequestController;
import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestCreatePage extends AbstractEntityEditPageContainerManagedImpl<AccountRequest> implements IdentificationPage,Serializable {

	@Override
	protected void __listenPostConstruct__() {
		action = Action.CREATE;
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.FIELD_ACTION, Action.CREATE);
		arguments.put(Form.FIELD_ENTITY, new AccountRequest());
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(AccountRequest.FIELD_FIRST_NAME,AccountRequest.FIELD_LAST_NAMES,AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS);
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> map = super.getInputArguments(form, fieldName);
				if(AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
					map.put(InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
						@Override
						public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
							super.validate(context, component, value);
							Actor actor =__inject__(ActorController.class).readByElectronicMailAddress((String) value);
							throwValidatorExceptionIf(actor != null, "cette addresse est déja liée à un compte");
							AccountRequest accountRequest =__inject__(AccountRequestController.class).readByElectronicMailAddress((String) value);
							throwValidatorExceptionIf(accountRequest != null, "cette addresse est déja liée à une demande en cours");
						}
					});
				}
				return map;
			}
			
			@Override
			public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
				Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
				map.put(CommandButton.FIELD_VALUE, "Enregistrer");
				return map;
			}
		});
		
		arguments.put(Form.FIELD_LISTENER, new Form.Listener.AbstractImpl() {
			@Override
			public void redirect(Form form, Object request) {
				JsfController.getInstance().redirect("accountRequestNotifyAfterCreateView");
			}
		});
		return arguments;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Demande de compte";
	}
	
	@Override
	protected WindowBuilder __getWindowBuilder__(List<String> subDurations) {
		WindowBuilder windowBuilder = super.__getWindowBuilder__(subDurations);
		windowBuilder.getApplicationName(Boolean.TRUE).setValue("Identification");
		return windowBuilder;
	}
}