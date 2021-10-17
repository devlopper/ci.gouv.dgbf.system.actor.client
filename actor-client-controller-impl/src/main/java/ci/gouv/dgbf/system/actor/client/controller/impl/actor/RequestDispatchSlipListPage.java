package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

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
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.api.RequestDispatchSlipController;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestDispatchSlipQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestDispatchSlipListPage extends AbstractEntityListPageContainerManagedImpl<RequestDispatchSlip> implements Serializable {

	private RequestDispatchSlipFilterController filterController;
	private TabMenu.Tab selectedTab;
	private Layout layout;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new RequestDispatchSlipFilterController().initialize();
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestDispatchSlip.LABEL);
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		
		RequestDispatchSlipFilterController filterController = null;		
		LazyDataModelListenerImpl lazyDataModelListenerImpl = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListenerImpl == null)
			arguments.put(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListenerImpl = new LazyDataModelListenerImpl());
		filterController = (RequestDispatchSlipFilterController) lazyDataModelListenerImpl.getFilterController();
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = (RequestDispatchSlipFilterController) MapHelper.readByKey(arguments, RequestDispatchSlipFilterController.class));
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = new RequestDispatchSlipFilterController());
		lazyDataModelListenerImpl.enableFilterController();
		if(StringHelper.isBlank(filterController.getOnSelectRedirectorArguments(Boolean.TRUE).getOutcome())) {
			String outcome = ValueHelper.defaultToIfBlank((String)MapHelper.readByKey(arguments,OUTCOME),OUTCOME);
			filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome);
		}
		
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl());
		dataTableListenerImpl.setFilterController(filterController);
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, RequestDispatchSlip.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_SORT_MODE, "multiple");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, filterController.generateColumnsNames());
		
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		
		Map<String,List<String>> parameters = filterController.asMap();
		dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Nouveau bordereau",MenuItem.FIELD_ICON,"fa fa-plus",MenuItem.FIELD___OUTCOME__
				,RequestDispatchSlipEditPage.OUTCOME,MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.NAVIGATE_TO_VIEW
				,MenuItem.FIELD___PARAMETERS__,parameters);
			
		dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestDispatchSlipReadPage.OUTCOME, MenuItem.FIELD_VALUE,"Ouvrir",MenuItem.FIELD_ICON,"fa fa-eye");
		
		/*
		LazyDataModelListenerImpl lazyDataModelListener = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListener == null)
			lazyDataModelListener = new LazyDataModelListenerImpl();
		//Class<?> pageClass = (Class<?>) MapHelper.readByKey(arguments, RequestListPage.class);
		List<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(RequestDispatchSlip.FIELD_CODE,RequestDispatchSlip.FIELD_SECTION_AS_STRING,RequestDispatchSlip.FIELD_FUNCTION_AS_STRING));
		//columnsFieldsNames.addAll(0, List.of(Request.FIELD_NAMES));
			//if(Boolean.TRUE.equals(lazyDataModelListener.getProcessingDateIsNotNullable()))
			//	columnsFieldsNames.addAll(List.of(Request.FIELD_PROCESSING_DATE_AS_STRING));
			
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, RequestDispatchSlip.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsFieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		
		Map<String,List<String>> parameters = new LinkedHashMap<>();
		Section section = (Section) MapHelper.readByKey(arguments, Section.class);
		if(section != null)
			parameters.put(ParameterName.stringify(Section.class),List.of(section.getIdentifier()));
		
		Function function = (Function) MapHelper.readByKey(arguments, Function.class);
		if(function != null)
			parameters.put(ParameterName.stringify(Function.class),List.of(function.getIdentifier()));
		
		dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Nouveau bordereau",MenuItem.FIELD_ICON,"fa fa-plus",MenuItem.FIELD___OUTCOME__
				,RequestDispatchSlipEditPage.OUTCOME,MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.NAVIGATE_TO_VIEW
				,MenuItem.FIELD___PARAMETERS__,parameters);
			
		dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestDispatchSlipReadPage.OUTCOME, MenuItem.FIELD_VALUE,"Ouvrir",MenuItem.FIELD_ICON,"fa fa-eye");
		*/		
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private RequestDispatchSlipFilterController filterController;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(RequestDispatchSlip.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Numéro");
				map.put(Column.FIELD_WIDTH, "120");
				map.put(Column.FIELD_SORT_BY, fieldName);
			}else if(RequestDispatchSlip.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(RequestDispatchSlip.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_WIDTH, "65");
			}else if(RequestDispatchSlip.FIELD_FUNCTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction");
				map.put(Column.FIELD_WIDTH, "70");
			}else if(RequestDispatchSlip.FIELD_COMMENT.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Commentaire");
			}else if(RequestDispatchSlip.FIELD_CREATION_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Créé le");
				map.put(Column.FIELD_WIDTH, "120");
				map.put(Column.FIELD_SORT_BY, ci.gouv.dgbf.system.actor.server.persistence.entities.RequestDispatchSlip.FIELD_CREATION_DATE);
			}else if(RequestDispatchSlip.FIELD_SENDING_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Transmis le");
				map.put(Column.FIELD_WIDTH, "120");
				if(filterController != null)
					map.put(Column.FIELD_VISIBLE, filterController.getSentInitial() == null || filterController.getSentInitial());
				map.put(Column.FIELD_SORT_BY, ci.gouv.dgbf.system.actor.server.persistence.entities.RequestDispatchSlip.FIELD_SENDING_DATE);
			}else if(RequestDispatchSlip.FIELD_PROCESSING_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Traité le");
				map.put(Column.FIELD_WIDTH, "120");
				if(filterController != null)
					map.put(Column.FIELD_VISIBLE, filterController.getProcessedInitial() == null || filterController.getProcessedInitial());
				map.put(Column.FIELD_SORT_BY, ci.gouv.dgbf.system.actor.server.persistence.entities.RequestDispatchSlip.FIELD_PROCESSING_DATE);
			}else if(RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "# Demandes");
				map.put(Column.FIELD_WIDTH, "120");
			}else if(RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_PROCESSED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "# D. traitées");
				map.put(Column.FIELD_WIDTH, "120");
			}else if(RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_ACCEPTED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "# D. acceptées");
				map.put(Column.FIELD_WIDTH, "120");
			}else if(RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_REJECTED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "# D. rejetées");
				map.put(Column.FIELD_WIDTH, "120");
			}else if(RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_NOT_PROCESSED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "# D. à traiter");
				map.put(Column.FIELD_WIDTH, "120");
			}
			return map;
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<RequestDispatchSlip> implements Serializable {
		private String sectionIdentifier;
		private String functionIdentifier;
		private Boolean sendingDateIsNullNullable,sendingDateIsNotNullNullable;
		private Boolean processingDateIsNullNullable,processingDateIsNotNullNullable;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			return RequestDispatchSlipQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		public LazyDataModelListenerImpl enableFilterController(){
			if(filterController == null)
				filterController = new RequestDispatchSlipFilterController();
			filterController.build();
			return this;
		}
		
		public Filter.Dto instantiateFilter(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			filter = RequestDispatchSlipFilterController.populateFilter(filter, (RequestDispatchSlipFilterController) filterController,Boolean.TRUE);
			return filter;
		}
		
		@Override
		public Arguments<RequestDispatchSlip> instantiateArguments(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			Arguments<RequestDispatchSlip> arguments = super.instantiateArguments(lazyDataModel);
			arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestDispatchSlip
					.FIELDS_SECTION_AS_CODE_FUNCTION_AS_CODE_ALL_DATES_ALL_NUMBERS_OF_REQUESTS_AS_STRING);
			
			Boolean processed = ((RequestDispatchSlipFilterController)filterController).getProcessedInitial();
			if(processed == null || processed) {

			}else {

			}
			return arguments;
		}
		
		/*public Filter.Dto instantiateFilter(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SECTION_IDENTIFIER, sectionIdentifier, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER, functionIdentifier, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SENDING_DATE_IS_NULL, sendingDateIsNullNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SENDING_DATE_IS_NOT_NULL, sendingDateIsNotNullNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NULL, processingDateIsNullNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NOT_NULL, processingDateIsNotNullNullable, filter);
			return filter;
		}*/
		
		public static final String FIELD_PROCESSING_DATE_IS_NULLABLE = "processingDateIsNullable";
		public static final String FIELD_PROCESSING_DATE_IS_NOT_NULLABLE = "processingDateIsNotNullable";
	}

	public static SelectOneCombo buildSelectOne(RequestDispatchSlip requestDispatchSlip,Object container,String sectionSelectOneFieldName,String functionSelectOneFieldName) {
		SelectOneCombo selectOne = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,requestDispatchSlip,SelectOneCombo.FIELD_CHOICE_CLASS,RequestDispatchSlip.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<RequestDispatchSlip>() {
			@Override
			public Collection<RequestDispatchSlip> computeChoices(AbstractInputChoice<RequestDispatchSlip> input) {
				String sectionIdentifier = null;
				String functionIdentifier = null;
				if(container != null) {
					if(StringHelper.isNotBlank(sectionSelectOneFieldName))
						sectionIdentifier = (String) FieldHelper.readSystemIdentifier(AbstractInput.getValue((AbstractInput<?>)FieldHelper.read(container, sectionSelectOneFieldName)));
					if(StringHelper.isNotBlank(functionSelectOneFieldName))
						functionIdentifier = (String) FieldHelper.readSystemIdentifier(AbstractInput.getValue((AbstractInput<?>)FieldHelper.read(container, functionSelectOneFieldName)));
				}
				Collection<RequestDispatchSlip> choices = __inject__(RequestDispatchSlipController.class).read(sectionIdentifier, functionIdentifier);
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, RequestDispatchSlip requestDispatchSlip) {
				super.select(input, requestDispatchSlip);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestDispatchSlip.LABEL);
		selectOne.updateChoices();
		selectOne.selectByValueSystemIdentifier();
		return selectOne;
	}
	
	public static SelectOneCombo buildExistsSelectOneCombo(Boolean exists,Object container,String childSelectOneFieldName) {
		return SelectOneCombo.buildUnknownYesNoOnly(exists, "Portée sur bordereau",new SelectOneCombo.Listener.AbstractImpl<Boolean>() {
			@Override
			public void select(AbstractInputChoiceOne input, Boolean value) {
				super.select(input, value);
				if(container != null && StringHelper.isNotBlank(childSelectOneFieldName)) {
					AbstractInputChoiceOne childInput = (AbstractInputChoiceOne)FieldHelper.read(container, childSelectOneFieldName);
					if(childInput != null) {
						childInput.setValue(null);
						childInput.updateChoices();
					}
				}
			}
		});
	}
	
	public static final String OUTCOME = "requestDispatchSlipListView";
}