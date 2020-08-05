package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;

import ci.gouv.dgbf.system.actor.client.controller.api.AccountRequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class AccountRequestOpenPage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private Form form;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		form = buildForm();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ouvrir demande de compte";
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class Data implements Serializable {
		@Input @org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputText @NotNull
		private String accessToken;
		
		public static final String FIELD_ACCESS_TOKEN = "accessToken";
	}
	
	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map, Form.FIELD_ENTITY_CLASS, Data.class);
		MapHelper.writeByKeyDoNotOverride(map, Form.ConfiguratorImpl.FIELD_CONTROLLER_ENTITY_INJECTABLE, Boolean.FALSE);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ENTITY, new Data());
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());		
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		return Form.build(map);
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	public static class FormListenerImpl extends Form.Listener.AbstractImpl implements Serializable {
		public void act(Form form) {
			Data data = (Data) form.getEntity();
			if(data == null || StringHelper.isBlank(data.getAccessToken()))
				throw new RuntimeException("Le jeton d'accès est obligatoire");
			JsfController.getInstance().redirect("accountRequestReadByAccessTokenView",Map.of("accesstoken",List.of(data.getAccessToken())));
			super.act(form);
		}
		
		@Override
		public void redirect(Form form, Object request) {
			JsfController.getInstance().redirect("accountRequestNotifyAfterCreateView");
		}
	}
	
	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		@Override
		public Collection<String> getFieldsNames(Form form) {
			return CollectionHelper.listOf(Data.FIELD_ACCESS_TOKEN);
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Data.FIELD_ACCESS_TOKEN.equals(fieldName)) {
				map.put(InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
					@Override
					public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
						super.validate(context, component, value);
						AccountRequest accountRequest =__inject__(AccountRequestController.class).readByAccessToken((String) value);
						throwValidatorExceptionIf(accountRequest == null, "Ce jeton d'accès n'est lié à aucune demande de compte");
					}
				});
			}
			return map;
		}
		/*
		@Override
		public Map<Object, Object> getLayoutArguments(Form form,Collection<Map<Object,Object>> cellsArguments) {
			Map<Object, Object> arguments = super.getLayoutArguments(form, cellsArguments);
			arguments.put(Layout.FIELD_NUMBER_OF_COLUMNS,3);
			arguments.put(Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(1),1,new Cell().setWidth(10),2,new Cell().setWidth(1)));
			return arguments;
		}
		*/
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			map.put(CommandButton.FIELD_VALUE, "Ouvrir");
			//map.put(Cell.FIELD_WIDTH, 1);
			return map;
		}
	}
}