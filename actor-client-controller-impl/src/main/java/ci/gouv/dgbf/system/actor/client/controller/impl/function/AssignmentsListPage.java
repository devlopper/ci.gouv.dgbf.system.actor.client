package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Action;
import ci.gouv.dgbf.system.actor.client.controller.entities.Activity;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.business.api.AssignmentsBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AssignmentsQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.entities.Profile;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class AssignmentsListPage extends AbstractEntityListPageContainerManagedImpl<Assignments> implements Serializable {

	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,Helper.buildFunctionListPageTabMenu(null),Cell.FIELD_WIDTH,12)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)					
						));
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(AssignmentsListPage.class,Boolean.TRUE);		
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Affectations";
	}
	
	public static String buildWindowTitleValue(String prefix,PageArguments pageArguments) {
		return buildWindowTitleValue(prefix, pageArguments.section, pageArguments.administrativeUnit, pageArguments.budgetSpecializationUnit, pageArguments.action
				, pageArguments.activity, pageArguments.expenditureNature, pageArguments.activityCategory,pageArguments.scopeFunction);
	}
	
	public static String buildWindowTitleValue(String prefix,Section section,AdministrativeUnit administrativeUnit,BudgetSpecializationUnit budgetSpecializationUnit,Action action,Activity activity
			,ExpenditureNature expenditureNature,ActivityCategory activityCategory,ScopeFunction scopeFunction) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(section != null) {
			if(administrativeUnit == null && budgetSpecializationUnit == null)
				strings.add(section.toString());
			else
				strings.add("Section "+section.getCode());
		}
		if(administrativeUnit != null) {
			strings.add(administrativeUnit.toString());
		}
		if(budgetSpecializationUnit != null) {
			if(action == null && activity == null)
				strings.add(budgetSpecializationUnit.toString());
			else
				strings.add((budgetSpecializationUnit.getCode().startsWith("1") ? "Dotation":"Programme")+" "+budgetSpecializationUnit.getCode());
		}
		if(action != null) {
			if(activity == null)
				strings.add(action.toString());
			else
				strings.add("Action "+action.getCode());
		}
		
		if(activity == null) {
			if(expenditureNature != null)
				strings.add("Nature de dépense : "+expenditureNature.toString());
			if(activityCategory != null)
				strings.add("Catégorie d'activité : "+activityCategory.toString());	
		}else {
			strings.add(activity.toString());
		}
		
		if(scopeFunction == null) {
				
		}else {
			strings.add(scopeFunction.toString());
		}
		return StringHelper.concatenate(strings, " | ");
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Assignments.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES
				, CollectionHelper.listOf(Assignments.FIELD_SECTION_AS_STRING,Assignments.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING
						,Assignments.FIELD_ACTION_AS_STRING,Assignments.FIELD_ACTIVITY_AS_STRING,Assignments.FIELD_ECONOMIC_NATURE_AS_STRING
						,Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING,Assignments.FIELD_ACTIVITY_CATEGORY_AS_STRING
						,Assignments.FIELD_EXPENDITURE_NATURE_AS_STRING
						,Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING//,Assignments.FIELD_CREDIT_MANAGER_ASSISTANT_AS_STRING
						,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING//,Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT_AS_STRING
						,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING//,Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AS_STRING
						,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING//,Assignments.FIELD_ACCOUNTING_ASSISTANT_AS_STRING
						));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		if(Boolean.TRUE.equals(MapHelper.readByKey(arguments, AssignmentsListPage.class))) {
			Boolean isAdministrationActionsVisible = !Boolean.TRUE.equals(SessionManager.getInstance().isUserLogged()) 
					|| Boolean.TRUE.equals(SessionManager.getInstance().isUserHasOneOfRoles(Profile.CODE_ADMINISTRATEUR));
			if(isAdministrationActionsVisible) {
				/*
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Initialiser",MenuItem.FIELD_USER_INTERFACE_ACTION
						,UserInterfaceAction.EXECUTE_FUNCTION,MenuItem.FIELD_ICON,"fa fa-download"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Assignments assignments = new Assignments();
								assignments.setOverridable(Boolean.FALSE);
								Arguments<Assignments> arguments = new Arguments<Assignments>().addCreatablesOrUpdatables(assignments);
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(AssignmentsBusiness.IMPORT));
								EntitySaver.getInstance().save(Assignments.class, arguments);
								return null;
							}
						});
				
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Dériver les valeurs",MenuItem.FIELD_USER_INTERFACE_ACTION
						,UserInterfaceAction.EXECUTE_FUNCTION,MenuItem.FIELD_ICON,"fa fa-gear"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Assignments assignments = new Assignments();
								assignments.setHoldersSettable(Boolean.TRUE);
								assignments.setAssistantsSettable(Boolean.TRUE);
								assignments.setOverridable(Boolean.FALSE);
								Arguments<Assignments> arguments = new Arguments<Assignments>().addCreatablesOrUpdatables(assignments);
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(AssignmentsBusiness.DERIVE_ALL_VALUES));
								EntitySaver.getInstance().save(Assignments.class, arguments);
								return null;
							}
						});
				
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Dériver les assistants",MenuItem.FIELD_USER_INTERFACE_ACTION
						,UserInterfaceAction.EXECUTE_FUNCTION,MenuItem.FIELD_ICON,"fa fa-gear"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Assignments assignments = new Assignments();
								assignments.setHoldersSettable(Boolean.FALSE);
								assignments.setAssistantsSettable(Boolean.TRUE);
								assignments.setOverridable(Boolean.FALSE);
								Arguments<Assignments> arguments = new Arguments<Assignments>().addCreatablesOrUpdatables(assignments);
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(AssignmentsBusiness.DERIVE_ALL_VALUES));					
								EntitySaver.getInstance().save(Assignments.class, arguments);
								return null;
							}
						});	
				*/
			}
				
			/*
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-pencil",MenuItem.FIELD_USER_INTERFACE_ACTION
					,UserInterfaceAction.NAVIGATE_TO_VIEW,MenuItem.FIELD_LISTENER,new MenuItem.Listener.AbstractImpl() {
						@Override
						protected String getOutcome(AbstractAction action) {
							return "executionImputationEditManyScopeFunctionsView";
						}
					});
			*/
			Map<String, List<String>> parameters = new HashMap<>();
			if(MapHelper.readByKey(arguments, Section.class) != null)
				parameters.put(ParameterName.stringify(Section.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, Section.class))));
			if(MapHelper.readByKey(arguments, AdministrativeUnit.class) != null)
				parameters.put(ParameterName.stringify(AdministrativeUnit.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, AdministrativeUnit.class))));
			if(MapHelper.readByKey(arguments, BudgetSpecializationUnit.class) != null)
				parameters.put(ParameterName.stringify(BudgetSpecializationUnit.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, BudgetSpecializationUnit.class))));
			if(MapHelper.readByKey(arguments, Action.class) != null)
				parameters.put(ParameterName.stringify(Action.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, Action.class))));
			if(MapHelper.readByKey(arguments, Activity.class) != null)
				parameters.put(ParameterName.stringify(Activity.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, Activity.class))));
			if(MapHelper.readByKey(arguments, ExpenditureNature.class) != null)
				parameters.put(ParameterName.stringify(ExpenditureNature.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, ExpenditureNature.class))));
			if(MapHelper.readByKey(arguments, ActivityCategory.class) != null)
				parameters.put(ParameterName.stringify(ActivityCategory.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, ActivityCategory.class))));
			if(MapHelper.readByKey(arguments, ScopeFunction.class) != null)
				parameters.put(ParameterName.stringify(ScopeFunction.class), List.of((String)FieldHelper.readSystemIdentifier(MapHelper.readByKey(arguments, ScopeFunction.class))));
			
			
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("assignmentsEditManyView",MenuItem.FIELD___PARAMETERS__,parameters
					, MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-pencil",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.OPEN_VIEW_IN_DIALOG);
	
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("assignmentsEditManyByModelView",MenuItem.FIELD___PARAMETERS__,parameters
					, MenuItem.FIELD_VALUE,"Modifier par modèle",MenuItem.FIELD_ICON,"fa fa-cubes",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.OPEN_VIEW_IN_DIALOG);
			/*
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Initialiser",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.FIELD_ICON,"fa fa-database"
					,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
					,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>();
							arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
									.setActionIdentifier(AssignmentsBusiness.INITIALIZE));					
							EntitySaver.getInstance().save(ScopeFunction.class, arguments);
							return null;
						}
					});
			*/
					
			if(isAdministrationActionsVisible) {
				/*dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Éffacer les fonctions",MenuItem.FIELD_USER_INTERFACE_ACTION
						,UserInterfaceAction.EXECUTE_FUNCTION,MenuItem.FIELD_ICON,"fa fa-trash"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Assignments assignments = new Assignments();
								assignments.setOverridable(Boolean.FALSE);
								Arguments<Assignments> arguments = new Arguments<Assignments>().addCreatablesOrUpdatables(assignments);
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(AssignmentsBusiness.CLEAN));
								EntitySaver.getInstance().save(Assignments.class, arguments);
								return null;
							}
						});
				*/
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Appliquer",MenuItem.FIELD_USER_INTERFACE_ACTION
						,UserInterfaceAction.EXECUTE_FUNCTION,MenuItem.FIELD_ICON,"fa fa-upload"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Assignments assignments = new Assignments();
								assignments.setOverridable(Boolean.FALSE);
								Arguments<Assignments> arguments = new Arguments<Assignments>().addCreatablesOrUpdatables(assignments);
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(AssignmentsBusiness.EXPORT));
								EntitySaver.getInstance().save(Assignments.class, arguments);
								return null;
							}
						});
			}
			
			if(isAdministrationActionsVisible) {	
				/*
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Supprimer toutes les lignes",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,MenuItem.FIELD_ICON,"fa fa-trash"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>();
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(AssignmentsBusiness.DELETE_ALL));					
								EntitySaver.getInstance().save(ScopeFunction.class, arguments);
								return null;
							}
						});
				*/
			}

			dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("assignmentsEditView", CommandButton.FIELD_VALUE,"Modifier",CommandButton.FIELD_ICON,"fa fa-pencil");
		}
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}

	/**/
	
	public static class PageArguments implements Serializable{
		public Section section;
		public AdministrativeUnit administrativeUnit;
		public BudgetSpecializationUnit budgetSpecializationUnit;
		public Action action;
		public Activity activity;
		public ExpenditureNature expenditureNature;
		public ActivityCategory activityCategory;
		private ScopeFunction scopeFunction;
		
		public void initialize() {
			if(activity == null) {
				activity = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(Activity.class
						,ActivityQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);
				if(activity != null) {
					section = activity.getSection();
					budgetSpecializationUnit = activity.getBudgetSpecializationUnit();
					administrativeUnit = activity.getAdministrativeUnit();
					expenditureNature = activity.getExpenditureNature();
					activityCategory = activity.getCategory();
				}
			}
			
			if(budgetSpecializationUnit == null) {
				budgetSpecializationUnit = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(BudgetSpecializationUnit.class
						,BudgetSpecializationUnitQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);
				if(budgetSpecializationUnit != null) {
					section = budgetSpecializationUnit.getSection();
				}
			}
			
			if(administrativeUnit == null) {
				administrativeUnit = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(AdministrativeUnit.class
						,AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);
				if(administrativeUnit != null && section == null) {
					section = administrativeUnit.getSection();
				}
			}
			
			if(section == null) {
				section = WebController.getInstance().getRequestParameterEntityAsParent(Section.class);
			}
			
			if(expenditureNature == null) {
				expenditureNature = WebController.getInstance().getRequestParameterEntityAsParent(ExpenditureNature.class);
			}
			
			if(activityCategory == null) {
				activityCategory = WebController.getInstance().getRequestParameterEntityAsParent(ActivityCategory.class);
			}
			
			if(scopeFunction == null)
				scopeFunction = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(ScopeFunction.class,
						ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI);
		}
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		private Function creditManagerHolder,creditManagerAssistant,authorizingOfficerHolder,authorizingOfficerAssistant
			,financialControllerHolder,financialControllerAssistant,accountingHolder,accountingAssistant;
		private String tooltipFormat;
		private Boolean showTooltip;
		
		public DataTableListenerImpl() {
			creditManagerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER);
			creditManagerAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT);
			authorizingOfficerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER);
			authorizingOfficerAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT);
			financialControllerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER);
			financialControllerAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT);
			accountingHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER);
			accountingAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT);
			if(showTooltip == null || Boolean.TRUE.equals(showTooltip))
				tooltipFormat = StringUtils.join(new String[] {
					 "Section              : %s"
					,"USB                  : %s"
					,"Action               : %s"
					,"Activité             : %s"
					,"Nature économique    : %s"
					,"Unité administrative : %s"
					,"Catégorie activité   : %s"
					,"Nature dépense       : %s"
					,FieldHelper.readName(creditManagerHolder)+"       : %s"
					,FieldHelper.readName(creditManagerAssistant)+"       : %s"
					,FieldHelper.readName(authorizingOfficerHolder)+"  : %s"
					,FieldHelper.readName(authorizingOfficerAssistant)+"  : %s"
					,FieldHelper.readName(financialControllerHolder)+" : %s"
					,FieldHelper.readName(financialControllerAssistant)+" : %s"
					,FieldHelper.readName(accountingHolder)+"          : %s"
					,FieldHelper.readName(accountingAssistant)+"          : %s"
				},"<br/>");
		}
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);			
			if(Assignments.FIELD_ACTIVITY_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Activité");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_ACTIVITY);
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Assignments.FIELD_ECONOMIC_NATURE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "NE");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_ECONOMIC_NATURE);
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(creditManagerHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_CREDIT_MANAGER_HOLDER);
			}else if(Assignments.FIELD_CREDIT_MANAGER_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(creditManagerAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_CREDIT_MANAGER_ASSISTANT);
			}else if(Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(authorizingOfficerHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_AUTHORIZING_OFFICER_HOLDER);
			}else if(Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(authorizingOfficerAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_AUTHORIZING_OFFICER_ASSISTANT);
			}else if(Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(financialControllerHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_FINANCIAL_CONTROLLER_HOLDER);
			}else if(Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(financialControllerAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_FINANCIAL_CONTROLLER_ASSISTANT);
			}else if(Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(accountingHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_ACCOUNTING_HOLDER);
			}else if(Assignments.FIELD_ACCOUNTING_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(accountingAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_ACCOUNTING_ASSISTANT);
			}else if(Assignments.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_WIDTH, "80");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_SECTION);
			}else if(Assignments.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "USB");
				map.put(Column.FIELD_WIDTH, "100");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT);
			}else if(Assignments.FIELD_ACTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Action");
				map.put(Column.FIELD_WIDTH, "120");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_ACTION);
			}else if(Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "UA");
				map.put(Column.FIELD_WIDTH, "100");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT);
			}else if(Assignments.FIELD_ACTIVITY_CATEGORY_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "CA");
				map.put(Column.FIELD_WIDTH, "100");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_ACTIVITY_CATEGORY);
			}else if(Assignments.FIELD_EXPENDITURE_NATURE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "ND");
				map.put(Column.FIELD_WIDTH, "80");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, AssignmentsQuerier.PARAMETER_NAME_EXPENDITURE_NATURE);
			}
			return map;
		}
		
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
			Object value = super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);		
			if(value instanceof String)
				return StringHelper.getFirstWord((String)value);			
			return value;
		}
		
		@Override
		public String getTooltipByRecord(Object record, Integer recordIndex) {
			if(showTooltip == null || Boolean.TRUE.equals(showTooltip)) {
				Assignments assignments = (Assignments) record;
				return String.format(tooltipFormat,assignments.getSectionAsString(),assignments.getBudgetSpecializationUnitAsString()
						,assignments.getActionAsString(),assignments.getActivityAsString(),assignments.getEconomicNatureAsString()
						,assignments.getAdministrativeUnitAsString(),assignments.getActivityCategoryAsString(),assignments.getExpenditureNatureAsString()
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getCreditManagerHolderAsString()),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getCreditManagerAssistantAsString()),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getAuthorizingOfficerHolderAsString()),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getAuthorizingOfficerAssistantAsString()),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getFinancialControllerHolderAsString()),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getFinancialControllerAssistantAsString()),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getAccountingHolderAsString()),ConstantEmpty.STRING)
						,ValueHelper.defaultToIfBlank(StringHelper.get(assignments.getAccountingAssistantAsString()),ConstantEmpty.STRING)
					);	
			}
			return super.getTooltipByRecord(record, recordIndex);
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	
		public static Collection<String> buildColumnsNames(PageArguments pageArguments) {
			return buildColumnsNames(pageArguments.section, pageArguments.administrativeUnit, pageArguments.budgetSpecializationUnit, pageArguments.activity
					, pageArguments.expenditureNature, pageArguments.activityCategory,pageArguments.scopeFunction);
		}
		
		public static Collection<String> buildColumnsNames(Section section,AdministrativeUnit administrativeUnit,BudgetSpecializationUnit budgetSpecializationUnit,Activity activity
				,ExpenditureNature expenditureNature,ActivityCategory activityCategory,ScopeFunction scopeFunction) {
			Collection<String> columnsFieldsNames = new ArrayList<>();
			if(activity == null && budgetSpecializationUnit == null && section == null)
				columnsFieldsNames.add(Assignments.FIELD_SECTION_AS_STRING);
			if(activity == null && administrativeUnit == null)
				columnsFieldsNames.add(Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING);	
			if(activity == null && budgetSpecializationUnit == null)
				columnsFieldsNames.add(Assignments.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING);			
			if(activity == null)
				columnsFieldsNames.add(Assignments.FIELD_ACTIVITY_AS_STRING);
			columnsFieldsNames.add(Assignments.FIELD_ECONOMIC_NATURE_AS_STRING);	
			//if(activity == null)
			//	columnsFieldsNames.addAll(List.of(Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING));
			if(activity == null && expenditureNature == null)
				columnsFieldsNames.add(Assignments.FIELD_EXPENDITURE_NATURE_AS_STRING);
			if(activity == null && activityCategory == null)
				columnsFieldsNames.add(Assignments.FIELD_ACTIVITY_CATEGORY_AS_STRING);			
			
			if(scopeFunction == null || StringHelper.isBlank(scopeFunction.getFunctionCode())) {
				columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
						,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
			}else {
				if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER.equals(scopeFunction.getFunctionCode()))
					columnsFieldsNames.addAll(List.of(Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
							,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER.equals(scopeFunction.getFunctionCode()))
					columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING
							,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER.equals(scopeFunction.getFunctionCode()))
					columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
							,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER.equals(scopeFunction.getFunctionCode()))
					columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
							,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING));					
			}		
			return columnsFieldsNames;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Assignments> implements Serializable {		
		private String sectionCode,budgetSpecializationUnitCode,actionCode,expenditureNatureCode,activityCategoryCode,activityCode,economicNatureCode,administrativeUnitCode;
		private Boolean allHoldersDefined,someHoldersNotDefined;
		private HolderAndAssistant creditManager = new HolderAndAssistant()
				,authorizingOfficer = new HolderAndAssistant()
				,financialController = new HolderAndAssistant()
				,accounting = new HolderAndAssistant();
		
		public LazyDataModelListenerImpl applyPageArguments(PageArguments pageArguments){
			sectionCode(pageArguments.section);
			administrativeUnitCode(pageArguments.administrativeUnit);
			budgetSpecializationUnitCode(pageArguments.budgetSpecializationUnit);
			activityCode(pageArguments.activity);
			activityCategoryCode(pageArguments.activityCategory);
			expenditureNatureCode(pageArguments.expenditureNature);
			scopeFunctionCode(pageArguments.scopeFunction);
			return this;
		}
		
		public LazyDataModelListenerImpl sectionCode(Section section) {
			if(section == null)
				return this;
			setSectionCode(section.getCode());
			return this;
		}
		
		public LazyDataModelListenerImpl administrativeUnitCode(AdministrativeUnit administrativeUnit) {
			if(administrativeUnit == null)
				return this;
			setAdministrativeUnitCode(administrativeUnit.getCode());
			return this;
		}
		
		public LazyDataModelListenerImpl budgetSpecializationUnitCode(BudgetSpecializationUnit budgetSpecializationUnit) {
			if(budgetSpecializationUnit == null)
				return this;
			setBudgetSpecializationUnitCode(budgetSpecializationUnit.getCode());
			return this;
		}
		
		public LazyDataModelListenerImpl activityCode(Activity activity) {
			if(activity == null)
				return this;
			setActivityCode(activity.getCode());
			return this;
		}
		
		public LazyDataModelListenerImpl activityCategoryCode(ActivityCategory activityCategory) {
			if(activityCategory == null)
				return this;
			setActivityCategoryCode(activityCategory.getCode());
			return this;
		}
		
		public LazyDataModelListenerImpl expenditureNatureCode(ExpenditureNature expenditureNature) {
			if(expenditureNature == null)
				return this;
			setExpenditureNatureCode(expenditureNature.getCode());
			return this;
		}
		
		public LazyDataModelListenerImpl scopeFunctionCode(ScopeFunction scopeFunction) {
			if(scopeFunction == null)
				return this;
			if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER.equals(scopeFunction.getFunctionCode()))
				getCreditManager().getHolder().setFilter(scopeFunction.getCode());
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER.equals(scopeFunction.getFunctionCode()))
				getAuthorizingOfficer().getHolder().setFilter(scopeFunction.getCode());
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER.equals(scopeFunction.getFunctionCode()))
				getFinancialController().getHolder().setFilter(scopeFunction.getCode());
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER.equals(scopeFunction.getFunctionCode()))
				getAccounting().getHolder().setFilter(scopeFunction.getCode());
			return this;
		}
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Assignments> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Assignments> lazyDataModel) {
			return AssignmentsQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Assignments> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_CREDIT_MANAGER_HOLDER, creditManager.holder.filter, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_CREDIT_MANAGER_HOLDER_IS_NULL, creditManager.holder.isNull, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_CREDIT_MANAGER_HOLDER_IS_NOT_NULL, creditManager.holder.isNotNull, filter);
			
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_AUTHORIZING_OFFICER_HOLDER, authorizingOfficer.holder.filter, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_AUTHORIZING_OFFICER_HOLDER_IS_NULL, authorizingOfficer.holder.isNull, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_AUTHORIZING_OFFICER_HOLDER_IS_NOT_NULL, authorizingOfficer.holder.isNotNull, filter);
			
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_FINANCIAL_CONTROLLER_HOLDER, financialController.holder.filter, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_FINANCIAL_CONTROLLER_HOLDER_IS_NULL, financialController.holder.isNull, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_FINANCIAL_CONTROLLER_HOLDER_IS_NOT_NULL, financialController.holder.isNotNull, filter);
			
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_ACCOUNTING_HOLDER, accounting.holder.filter, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_ACCOUNTING_HOLDER_IS_NULL, accounting.holder.isNull, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_ACCOUNTING_HOLDER_IS_NOT_NULL, accounting.holder.isNotNull, filter);
			
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_ALL_HOLDERS_DEFINED, allHoldersDefined, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_SOME_HOLDERS_NOT_DEFINED, someHoldersNotDefined, filter);
			
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_SECTION, sectionCode, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT, administrativeUnitCode, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT, budgetSpecializationUnitCode, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_EXPENDITURE_NATURE, expenditureNatureCode, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_ACTIVITY_CATEGORY, activityCategoryCode, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(AssignmentsQuerier.PARAMETER_NAME_ACTIVITY, activityCode, filter);
			
			return filter;
		}
		
		@Getter @Setter @Accessors(chain=true)
		public static class ScopeFunctionData implements Serializable {
			private String filter;
			private Boolean isNull;
			private Boolean isNotNull;
		}
		
		@Getter @Setter @Accessors(chain=true)
		public static class HolderAndAssistant implements Serializable {
			private ScopeFunctionData holder = new ScopeFunctionData();
			private ScopeFunctionData assistant = new ScopeFunctionData();
		}
	}
}