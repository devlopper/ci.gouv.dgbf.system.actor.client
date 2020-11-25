package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribut;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.AbstractRequestReadPage.FormConfiguratorListener;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.AbstractRequestReadPage.FormListener;
import ci.gouv.dgbf.system.actor.client.controller.impl.user.RequestInitializePage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
public abstract class AbstractRequestReadPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	protected Request request;
	
	@Override
	protected void __listenPostConstruct__() {
		request = RequestInitializePage.getRequestFromParameter(Action.READ);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.READ;
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(request);
	}

	public static Form buildForm(Request request) {
		Form form = AbstractRequestEditPage.buildForm(Form.FIELD_ACTION,Action.READ,Form.FIELD_ENTITY,request,Form.FIELD_LISTENER,new FormListener(request)
				,Form.ConfiguratorImpl.FIELD_LISTENER,new FormConfiguratorListener(request));
		return form;
	}
	
	public static Form buildForm() {
		return buildForm(AbstractRequestEditPage.getRequestFromParameter(Action.READ));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return request.getType().getForm().getName();
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends AbstractRequestEditPage.FormListener {
		
		public FormListener(Request request) {
			super(request,null);
		}
		
		@Override
		public Boolean isSubmitButtonShowable(Form form) {
			return Boolean.FALSE;
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractRequestEditPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request,null);
		}
		
		/*@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			IdentificationAttribut attribut = fieldsNames == null ? null : fieldsNames.get(fieldName);
			if(attribut != null) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, attribut.getName());
				map.put(AbstractInput.FIELD_REQUIRED, attribut.getRequired());
			}
			if(Request.FIELD_ACTOR.equals(fieldName)) {
				
			}else if(Request.FIELD_ACTOR_CODE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Nom d'utilisateur");
			}else if(Request.FIELD_ACTOR_NAMES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Nom et prénoms");
			}else if(Request.FIELD_TYPE_AS_STRING.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Type");
			}else if(Request.FIELD_COMMENT.equals(fieldName)) {
				
			}else if(Request.FIELD_TYPE.equals(fieldName)) {
				
			}else if(Request.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, AdministrativeUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(Request.FIELD_BUDGET_SPECIALIZATION_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, BudgetSpecializationUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(Request.FIELD_SECTION.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, Section.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(Request.FIELD_ADMINISTRATIVE_FUNCTION.equals(fieldName)) {
				
			}else if(Request.FIELD_STATUS_AS_STRING.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Statut");
			}else if(Request.FIELD_REJECTION_REASON.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Motif de rejet");
			}
			return map;
		}*/
	}
}