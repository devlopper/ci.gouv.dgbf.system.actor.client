package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
public abstract class AbstractActorListPage extends AbstractEntityListPageContainerManagedImpl<Actor> implements Serializable {

	protected Function function;
	
	@Override
	protected void __listenPostConstruct__() {
		//function = getFunctionFromRequestParameterEntity();
		super.__listenPostConstruct__();
	}
	
	protected Function getFunctionFromRequestParameterEntity() {
		return WebController.getInstance().getRequestParameterEntity(Function.class);
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(Function.class,function == null ? null : function.getCode());
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des comptes utilisateurs"+(function == null ? ConstantEmpty.STRING : " : "+function.getName());
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Actor.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(Actor.FIELD_CODE,Actor.FIELD_FIRST_NAME
				,Actor.FIELD_LAST_NAMES,Actor.FIELD_SERVICE,Actor.FIELD_PROFILES
				//,Actor.FIELD_VISIBLE_SECTIONS,Actor.FIELD_VISIBLE_MODULES
				));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl()
				.setProfileCode((String) MapHelper.readByKey(arguments, Profile.class))
				.setFunctionCode((String) MapHelper.readByKey(arguments, Function.class))
				.setVisibleSectionCode((String) MapHelper.readByKey(arguments, Section.class))
				);
		DataTable dataTable = DataTable.build(arguments);
		
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		
		Boolean usableAsSelectionOnly = (Boolean) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_USABLE_AS_SELECTION_ONLY);
		
		String assignIcon = "fa fa-lock";
		if(!Boolean.TRUE.equals(usableAsSelectionOnly)) {
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("assignPrivilegesToActorsByFunctionsView", MenuItem.FIELD_VALUE,"Assignation par fonction"
					,MenuItem.FIELD_ICON,assignIcon);
			
			/*
			dataTable.addRecordMenuItemByArgumentsNavigateToView(null,"actorListPrivilegesStaticView", MenuItem.FIELD_VALUE,"Assignation", MenuItem.FIELD_ICON,assignIcon
					,MenuItem.FIELD_PARAMETERS,Map.of(ParameterName.IS_STATIC.getValue(),"true"));
			*/
			dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("actorEditProfilesView", MenuItem.FIELD_VALUE,"Assignation", MenuItem.FIELD_ICON,assignIcon);
			
			dataTable.addRecordMenuItemByArgumentsNavigateToView(null,"actorListScopesStaticView", MenuItem.FIELD_VALUE,"Affectation", MenuItem.FIELD_ICON,"fa fa-eye"
					,MenuItem.FIELD_PARAMETERS,Map.of(ParameterName.IS_STATIC.getValue(),"true"));
			//dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("actorEditFunctionsView", MenuItem.FIELD_VALUE,"Fonctions", MenuItem.FIELD_ICON,"fa fa-flash");
			
			dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		}
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(Actor.FIELD_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom et prénom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_NAMES);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_FIRST_NAME);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prenom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_LAST_NAMES);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom d'utilisateur");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_CODE);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_PROFILES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Profile(s)");
				map.put(Column.FIELD_FILTER_BY, ActorQuerier.PARAMETER_NAME_PROFILE_CODE);
				/*map.put(Column.FIELD_FILTER_INPUT_TYPE, Column.FilterInputType.SELECT_ONE_MENU);
				Collection<Profile> profiles = EntityReader.getInstance().readMany(Profile.class, ProfileQuerier.QUERY_IDENTIFIER_READ);
				if(CollectionHelper.isNotEmpty(profiles)) {
					Collection<SelectItem> selectItems = new ArrayList<SelectItem>();
					selectItems.add(new SelectItem(null, "--- Aucune sélection ---"));
					selectItems.addAll(profiles.stream().map(profile -> new SelectItem(profile.getCode(), profile.getName())).collect(Collectors.toList()));
					map.put(Column.FIELD_FILTER_INPUT_SELECT_ITEMS, selectItems);
				}*/
			}else if(Actor.FIELD_FUNCTIONS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s)");
			}else if(Actor.FIELD_VISIBLE_SECTIONS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section(s) visible(s)");
			}else if(Actor.FIELD_VISIBLE_MODULES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Module(s) visible(s)");
			}else if(Actor.FIELD_SERVICE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Service");
				map.put(Column.FIELD_WIDTH, "300");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Actor.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_WIDTH, "50");
			}
			return map;
		}
		
		@Override
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(column != null && Actor.FIELD_FUNCTIONS.equals(column.getFieldName()))
				return CollectionHelper.isEmpty(((Actor)record).getFunctions()) ? null : ((Actor)record).getFunctions().stream().map(x -> x.getName()).collect(Collectors.joining(","));
			if(column != null && Actor.FIELD_PROFILES.equals(column.getFieldName()))
				return CollectionHelper.isEmpty(((Actor)record).getProfiles()) ? null : ((Actor)record).getProfiles().stream().map(x -> x.getName()).collect(Collectors.joining(","));
			if(column != null && Actor.FIELD_SERVICE.equals(column.getFieldName())) {
				Actor actor = (Actor) record;
				return Helper.formatService(actor.getAdministrativeUnitAsString(), actor.getAdministrativeFunction(), actor.getSectionAsString());
			}
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		@Override
		public String getTooltipByRecord(Object record, Integer recordIndex) {
			if(record instanceof Actor) {
				Actor actor = (Actor) record;			
				return String.format(ROW_TOOLTIP_FORMAT,actor.getNames(),ValueHelper.defaultToIfBlank(actor.getSectionAsString(),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(actor.getAdministrativeUnitAsString(),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(actor.getAdministrativeFunction(),ConstantEmpty.STRING)
						,CollectionHelper.isEmpty(actor.getProfiles()) ? ConstantEmpty.STRING : actor.getProfiles().stream().map(x -> x.getName()).collect(Collectors.toList()) 
						);
			}	
			return super.getTooltipByRecord(record, recordIndex);
		}
		
		private static final String ROW_TOOLTIP_FORMAT = "Nom et prénom(s) : %s<br/>Section : %s<br/>Unité administrative : %s<br/>Fonction administrative : %s"
				+ "<br/>Profile(s) : %s";
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Actor> implements Serializable {
		
		private String profileCode,functionCode,visibleSectionCode,visibleBudgetSpecializationUnitCode;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Actor> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Actor> lazyDataModel) {
			//return ActorQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER;
			//return ActorQuerier.QUERY_IDENTIFIER_READ_WITH_FUNCTIONS_WHERE_FILTER;
			return ActorQuerier.QUERY_IDENTIFIER_READ_WITH_ALL_WHERE_FILTER;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Actor> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);			
			if(StringHelper.isNotBlank(profileCode) || StringHelper.isNotBlank(functionCode) || StringHelper.isNotBlank(visibleSectionCode)) {
				if(filter == null)
					filter = new Filter.Dto();
				if(StringHelper.isNotBlank(profileCode))
					filter.addField(ActorQuerier.PARAMETER_NAME_PROFILE_CODE, profileCode);
				if(StringHelper.isNotBlank(functionCode))
					filter.addField(ActorQuerier.PARAMETER_NAME_FUNCTION_CODE, functionCode);				
				if(StringHelper.isNotBlank(visibleSectionCode))
					filter.addField(ActorQuerier.PARAMETER_NAME_VISIBLE_SECTION_CODE, visibleSectionCode);
				if(StringHelper.isNotBlank(visibleBudgetSpecializationUnitCode))
					filter.addField(ActorQuerier.PARAMETER_NAME_VISIBLE_SECTION_CODE, visibleBudgetSpecializationUnitCode);
			}
			return filter;
		}
	}
}