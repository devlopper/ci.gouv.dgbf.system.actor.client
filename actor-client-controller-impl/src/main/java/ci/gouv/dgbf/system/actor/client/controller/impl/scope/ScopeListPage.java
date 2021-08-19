package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorListPageNEW;
import ci.gouv.dgbf.system.actor.client.controller.impl.myaccount.MyAccountScopeListPage;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ScopeListPage extends AbstractEntityListPageContainerManagedImpl<Scope> implements Serializable {

	private ScopeFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new ScopeFilterController();
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ScopeFilterController.class,filterController,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.Scope.LABEL);
	}
		
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Class<?> pageClass = (Class<?>) arguments.get(ScopeListPage.class);
		ScopeFilterController filterController = null;		
		LazyDataModelListenerImpl lazyDataModelListenerImpl = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListenerImpl == null)
			arguments.put(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListenerImpl = new LazyDataModelListenerImpl());
		filterController = (ScopeFilterController) lazyDataModelListenerImpl.getFilterController();
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = new ScopeFilterController());		
		lazyDataModelListenerImpl.enableFilterController();
		String outcome = ValueHelper.defaultToIfBlank((String)MapHelper.readByKey(arguments,OUTCOME),OUTCOME);
		filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome);
		
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl());
		dataTableListenerImpl.setFilterController(filterController);
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Scope.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, filterController.generateColumnsNames());	
		
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		dataTable.getOrderNumberColumn().setWidth("60");
		
		final ScopeFilterController finalFilterController = filterController;
		if(pageClass == null || ScopeListPage.class.equals(pageClass)) {
			ActorListPageNEW.addScopesRecordMenuItems(dataTable, finalFilterController.asMap());
			if(filterController.getVisible() != null) {
				if(filterController.getVisible()) {
					addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.TRUE);
				}else {
					addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.FALSE);
				}
			}else {
				addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.TRUE);
				addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.FALSE);
			}
		}else if(MyAccountScopeListPage.class.equals(pageClass)) {
			
		}
		
		return dataTable;
	}
	
	private static void addVisibleRecordMenuItem(DataTable dataTable,ScopeFilterController filterController,Boolean visible) {
		dataTable.addRecordMenuItemByArgumentsExecuteFunction(Boolean.TRUE.equals(visible) ? "Retirer" : "Assigner"
			,Boolean.TRUE.equals(visible) ? "fa fa-minus" : "fa fa-plus",new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				if(filterController.getActor() == null)
					throw new RuntimeException("Sélectionner un acteur");
				Scope scope = (Scope)action.readArgument();						
				if(scope == null)
					throw new RuntimeException("Sélectionner un domaine");												
				Arguments<ActorScope> arguments = new Arguments<ActorScope>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(new ActorScope()
						.setActorsIdentifiers(List.of(filterController.getActor().getIdentifier()))
						.setScopesIdentifiers(List.of(scope.getIdentifier())).setActorAsString(SessionManager.getInstance().getUserName()));
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
						.setActionIdentifier(Boolean.TRUE.equals(visible) ? ActorScopeBusiness.UNVISIBLE : ActorScopeBusiness.VISIBLE));					
				EntitySaver.getInstance().save(ActorScope.class, arguments);
				return arguments.get__responseEntity__();
			}
		});
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private ScopeFilterController filterController;
		private ScopeType scopeType;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(MapHelper.readByKey(map, DataTable.FIELD_SELECTION_MODE) == null) {
				/*
				if(ScopeType.isCodeEqualsSECTION(scopeType))
					dataTable.getOrderNumberColumn().setWidth("20");
				dataTable.getOrderNumberColumn().setWidth("100");
				*/
			}			
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(Scope.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, ScopeType.isCodeEqualsSECTION(scopeType) ? "70" : "100");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_CODE);
			}else if(Scope.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_NAME);
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
			}else if(Scope.FIELD_TYPE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_TYPE);
			}else if(Scope.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_SECTION_CODE_NAME);
			}else if(Scope.FIELD_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, scopeType == null ? "Domaine" : scopeType.getName());
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_THIS);
			}else if(Scope.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Programme/Dotation");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_CODE_NAME);
			}else if(Scope.FIELD_ACTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Action");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_ACTION_CODE_NAME);
			}else if(Scope.FIELD_ACTIVITY_CATEGORY_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Catégorie");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_CATEGORY_CODE_NAME);
			}else if(Scope.FIELD_ACTIVITY_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Activité");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_ACTIVITY_CODE_NAME);
			}else if(Scope.FIELD_ECONOMIC_NATURE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nature économique");
				//map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				//map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_ECONOMIC_NATURE_CODE_NAME);
			}else if(Scope.FIELD_VISIBLE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Visible");
				map.put(Column.FIELD_WIDTH, "70");
			}else if(Scope.FIELD_TYPE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type");
				map.put(Column.FIELD_WIDTH, "120");
			}
			return map;
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
		
		public String getStyleClassByRecord(Object record, Integer recordIndex) {
			if(record == null || recordIndex == null)
				return null;
			if(filterController.getVisible() == null && Boolean.TRUE.equals(((Scope)record).getVisible()))
				return "cyk-background-highlight";
			return null;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Scope> implements Serializable {
		protected ScopeType scopeType;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Scope> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Scope> lazyDataModel) {
			return ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Scope> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);			
			filter = ScopeFilterController.populateFilter(filter, (ScopeFilterController) filterController,Boolean.FALSE);
			return filter;
		}
		
		@Override
		public Arguments<Scope> instantiateArguments(LazyDataModel<Scope> lazyDataModel) {
			Arguments<Scope> arguments = super.instantiateArguments(lazyDataModel);
			if( ((ScopeFilterController)filterController).getVisible() == null )
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Scope.FIELDS_VISIBLE_AND_VISIBLE_AS_STRING);
			if(((ScopeFilterController)filterController).getScopeType() == null )
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Scope.FIELD_TYPE_AS_STRING);
			return arguments;
		}
	
		public LazyDataModelListenerImpl enableFilterController(){
			if(filterController == null)
				filterController = new ScopeFilterController();
			filterController.build();
			return this;
		}
	}

	/**/
	
	public static SelectOneCombo buildSectionSelectOne(Section section,Boolean isParent) {		
		SelectOneCombo sectionSelect = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Section>() {
			@Override
			public Collection<Section> computeChoices(AbstractInputChoice<Section> input) {
				Collection<Section> choices = __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, Section section) {
				super.select(input, section);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Section");
		sectionSelect.updateChoices();
		sectionSelect.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(section));
		if(Boolean.TRUE.equals(isParent)) {
			sectionSelect.enableValueChangeListener(List.of());
		}
		return sectionSelect;
	}
	
	public static final String OUTCOME = "scopeListView";
}