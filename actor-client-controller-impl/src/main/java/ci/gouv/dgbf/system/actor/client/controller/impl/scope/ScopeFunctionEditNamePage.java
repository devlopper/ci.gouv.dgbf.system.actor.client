package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeFunctionBusiness;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionEditNamePage extends AbstractEntityEditPageContainerManagedImpl<ScopeFunction> implements Serializable {

	@Override
	protected Form __buildForm__() {		
		return buildForm();
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		return "Modification du libellé de "+((ScopeFunction)form.getEntity()).toString();
	}

	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION,Action.UPDATE);
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());		
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		Form form = ScopeFunctionEditPage.buildForm(map);
		return form;
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	/**/

	public static class FormListenerImpl extends ScopeFunctionEditPage.FormListenerImpl implements Serializable {
		@Override
		public void act(Form form) {
			Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables((ScopeFunction)form.getEntity());
			arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ScopeFunctionBusiness.SAVE));
			EntitySaver.getInstance().save(ScopeFunction.class, arguments);
		}
	}

	public static class FormConfiguratorListenerImpl extends ScopeFunctionEditPage.FormConfiguratorListenerImpl implements Serializable {
		@Override
		public Collection<String> getFieldsNames(Form form) {
			Collection<String> collection = new ArrayList<>();
			collection.add(ScopeFunction.FIELD_NAME);
			return collection;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			map.put(CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
	}
	
	public static final String OUTCOME = "scopeFunctionEditNameView";
}