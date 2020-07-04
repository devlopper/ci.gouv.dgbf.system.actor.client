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
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.value.ValueConverter;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
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
public abstract class AbstractActorEditPrivilegesOrScopesPage<T> extends AbstractPageContainerManagedImpl implements Serializable {

	protected AutoComplete actorAutoComplete;
	protected CommandButton saveCommandButton;
	protected Actor actor;
	protected Layout layout;
	protected Boolean isFromActorList;
	
	@Override
	protected void __listenPostConstruct__() {
		isFromActorList = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter("fal"));
		actor = WebController.getInstance().getRequestParameterEntity(Actor.class);
		if(actor != null) {			
			
		}
		super.__listenPostConstruct__();		
		if(actor == null || Boolean.TRUE.equals(isShowActorAutoCompleteInput())) {
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
						JsfController.getInstance().redirect(getEditOutcome(),Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(actor.getIdentifier())));
				}			
			});
			actorAutoComplete.getAjaxes().get("itemSelect").setDisabled(Boolean.FALSE);
			actorAutoComplete.setReaderUsable(Boolean.TRUE);
			actorAutoComplete.setReadQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_STRING);
			actorAutoComplete.setCountQueryIdentifier(ActorQuerier.QUERY_NAME_COUNT_BY_STRING);
		}
		
		Collection<Map<?,?>> cellsMaps = new ArrayList<Map<?,?>>(); 
		if(!Boolean.TRUE.equals(isRenderTypeDialog)) {
			if(actorAutoComplete != null) {
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Compte utilisateur"),Cell.FIELD_WIDTH,2));
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorAutoComplete,Cell.FIELD_WIDTH,10));		
			}			
		}		
		if(actor == null) {
			
		}else {
			if(actorAutoComplete != null) {
				actorAutoComplete.setValue(actor);	
			}			
			addInputs(cellsMaps);
			saveCommandButton = CommandButton.build(getSaveCommandButtonArguments());
			addSaveCommandButtonArgumentsCell(cellsMaps);
		}
		layout = buildLayout(cellsMaps);
	}
	
	protected Layout buildLayout(Collection<Map<?,?>> cellsMaps) {
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	protected void addSaveCommandButtonArgumentsCell(Collection<Map<?,?>> cellsMaps) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12));
	}
	
	protected Boolean isShowActorAutoCompleteInput() {
		return isFromActorList == null || !isFromActorList;
	}
	
	protected abstract String getEditOutcome();
	protected abstract String getListOutcome();
	protected abstract void addInputs(Collection<Map<?,?>> cellsMaps);
	protected Map<Object,Object> getSaveCommandButtonArguments() {
		Map<Object,Object> arguments = new HashMap<>();
		arguments.put(CommandButton.FIELD_VALUE,"Enregistrer");
		arguments.put(CommandButton.FIELD_ICON,"fa fa-floppy-o");
		arguments.put(CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION);
		arguments.put(CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				save();
				if(!Boolean.TRUE.equals(action.get__isWindowContainerRenderedAsDialog__())) {
					JsfController.getInstance().redirect(getListOutcome(),Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(actor.getIdentifier())));
				}				
				return null;
			}
		});
		arguments.put(CommandButton.FIELD_STYLE_CLASS,"cyk-float-right");
		return arguments;
	}
	protected abstract void save();
}