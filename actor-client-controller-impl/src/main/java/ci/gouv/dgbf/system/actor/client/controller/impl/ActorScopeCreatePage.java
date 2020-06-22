package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorScopeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorScopeCreatePage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Actor actor;
	private DataTable scopesDataTable;
	private CommandButton createCommandButton;
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntityAsParent(Actor.class);
		super.__listenPostConstruct__();
		scopesDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Scope.class,DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,List.of(Scope.FIELD_CODE,Scope.FIELD_NAME),DataTable.FIELD_LISTENER,new DataTable.Listener.AbstractImpl() {
			@Override
			public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
				Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
				map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
				if(Scope.FIELD_CODE.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Code");
					map.put(Column.FIELD_WIDTH, "150");
				}else if(Scope.FIELD_NAME.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				}
				return map;
			}
		});
		
		@SuppressWarnings("unchecked")
		LazyDataModel<Scope> lazyDataModel = (LazyDataModel<Scope>) scopesDataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		lazyDataModel.setReadQueryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES_NOT_ASSOCIATED_BY_TYPES_CODES);
		lazyDataModel.setListener(new LazyDataModel.Listener.AbstractImpl<Scope>() {
			@Override
			public Filter.Dto instantiateFilter(LazyDataModel<Scope> lazyDataModel) {
				return new Filter.Dto().addField(ScopeQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()))
						.addField(ScopeQuerier.PARAMETER_NAME_TYPES_CODES, List.of("SECTION"));
			}
		});
		
		createCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o",CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION);
		createCommandButton.setListener(new CommandButton.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				if(CollectionHelper.isNotEmpty(scopesDataTable.getSelection())) {
					@SuppressWarnings("unchecked")
					Collection<Scope> scopes = (Collection<Scope>) scopesDataTable.getSelection();
					__inject__(ActorScopeController.class).createMany(scopes.stream().map(scope -> new ActorScope().setActor(actor).setScope(scope))
							.collect(Collectors.toList()));
				}
				return null;
			}
		});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(MapHelper.instantiate(Cell.FIELD_CONTROL,scopesDataTable,Cell.FIELD_WIDTH,12)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,createCommandButton,Cell.FIELD_WIDTH,12)));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout de domaines au compte utilisateur de "+actor.getCode()+" - "+actor.getNames();
	}	
}