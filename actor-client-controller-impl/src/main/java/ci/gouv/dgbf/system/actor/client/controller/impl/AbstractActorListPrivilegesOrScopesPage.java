package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorListPrivilegesOrScopesPage<T> extends AbstractPageContainerManagedImpl implements Serializable {

	protected Layout layout;
	protected Actor actor;
	protected DataTable dataTable;
	protected AutoComplete actorAutoComplete;
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntity(Actor.class);
		if(actor != null) {			
			dataTable = instantiateDataTable();
			dataTable.set__parentElement__(actor);
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE, "Ajouter",CommandButton.FIELD_PROCESS, "@this",CommandButton.FIELD_LISTENER
			,new CommandButton.Listener.AbstractImpl() {
				@Override
				protected String getOutcome(AbstractAction action) {
					return getCreateOutcome();
				}
				
				@Override
				protected Map<String, List<String>> getViewParameters(AbstractAction action) {
					Map<String, List<String>> parameters = super.getViewParameters(action);
					Map<String, List<String>> __parameters__ = getCreateParameters();
					if(MapHelper.isNotEmpty(__parameters__)) {
						if(parameters == null)
							parameters = new HashMap<>();
						parameters.putAll(__parameters__);
					}					
					return parameters;
				}
			});
			dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		}
		super.__listenPostConstruct__();		
		
		actorAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<Actor>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				return new Filter.Dto().addField(ActorQuerier.PARAMETER_NAME_STRING, "%"+ValueHelper.defaultToIfBlank(autoComplete.get__queryString__(),ConstantEmpty.STRING)+"%");
			}
		},AutoComplete.FIELD_PLACEHOLDER,"rechercher par le nom d'utilisateur");
		
		actorAutoComplete.enableAjaxItemSelect();
		actorAutoComplete.getAjaxes().get("itemSelect").setListener(new Ajax.Listener.AbstractImpl() {
			@Override
			protected void run(AbstractAction action) {
				Actor actor = (Actor) FieldHelper.read(action.get__argument__(), "source.value");
				if(actor != null)
					JsfController.getInstance().redirect(getListOutcome(),Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(actor.getIdentifier())));
			}			
		});
		actorAutoComplete.getAjaxes().get("itemSelect").setDisabled(Boolean.FALSE);
		actorAutoComplete.setReaderUsable(Boolean.TRUE);
		actorAutoComplete.setReadQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_STRING);
		actorAutoComplete.setCountQueryIdentifier(ActorQuerier.QUERY_NAME_COUNT_BY_STRING);
		
		Collection<Map<?,?>> cellsMaps = new ArrayList<Map<?,?>>(); 
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Compte utilisateur"),Cell.FIELD_WIDTH,2));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorAutoComplete,Cell.FIELD_WIDTH,10));	
		if(actor == null) {
			
		}else {
			actorAutoComplete.setValue(actor);
			addDataComponent(cellsMaps);
		}
		layout = buildLayout(cellsMaps);
	}
	
	protected Layout buildLayout(Collection<Map<?,?>> cellsMaps) {
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	protected void addDataComponent(Collection<Map<?,?>> cellsMaps) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	protected abstract String getListOutcome();
	protected abstract String getCreateOutcome();
	protected Map<String, List<String>> getCreateParameters() {
		return null;
	}
	
	protected abstract DataTable instantiateDataTable();
}