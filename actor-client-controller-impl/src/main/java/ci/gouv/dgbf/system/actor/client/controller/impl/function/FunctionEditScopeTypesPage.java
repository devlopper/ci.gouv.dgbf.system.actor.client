package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputTextarea;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectBooleanButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeTypeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeTypeListPage;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeTypeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class FunctionEditScopeTypesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Function function;
	private DataTable scopeTypesDataTable;
	private SelectBooleanButton scopeTypeScopeFunctionDerivableSelectBooleanButton;
	private InputTextarea scopeTypeScopeFunctionCodeScriptInputTextarea;
	private InputTextarea scopeTypeScopeFunctionNameScriptInputTextarea;
	//private SelectManyCheckbox scopeTypesSelectManyCheckbox;
	private Collection<ScopeTypeFunction> scopeTypesFunctions;
	private List<ScopeType> scopeTypes,initialSelectedScopeTypes;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Champ(s) d'action de la fonction "+function.getName();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		function = WebController.getInstance().getRequestParameterEntity(Function.class);
		super.__listenPostConstruct__();		
		scopeTypes = (List<ScopeType>) EntityReader.getInstance().readMany(ScopeType.class, ScopeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING);
		if(CollectionHelper.isNotEmpty(scopeTypes)) {
			scopeTypesFunctions = EntityReader.getInstance().readMany(ScopeTypeFunction.class, ScopeTypeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_FUNCTIONS_IDENTIFIERS
					,ScopeTypeFunctionQuerier.PARAMETER_NAME_FUNCTIONS_IDENTIFIERS,List.of(function.getIdentifier()));
			if(CollectionHelper.isNotEmpty(scopeTypesFunctions)) {
				initialSelectedScopeTypes = scopeTypesFunctions.stream().map(x -> x.getScopeType()).collect(Collectors.toList());
				for(ScopeType scopeType : scopeTypes) {
					for(ScopeTypeFunction scopeTypesFunction : scopeTypesFunctions) {
						if(scopeType.equals(scopeTypesFunction.getScopeType())) {
							scopeType.setScopeFunctionDerivable(scopeTypesFunction.getScopeFunctionDerivable());
							scopeType.setScopeFunctionCodeScript(scopeTypesFunction.getScopeFunctionCodeScript());
							scopeType.setScopeFunctionNameScript(scopeTypesFunction.getScopeFunctionNameScript());
							break;
						}
					}
				}
				
				for(ScopeType initialSelectedScopeType : initialSelectedScopeTypes) {
					for(ScopeTypeFunction scopeTypesFunction : scopeTypesFunctions) {
						if(initialSelectedScopeType.equals(scopeTypesFunction.getScopeType())) {
							initialSelectedScopeType.setScopeFunctionDerivable(scopeTypesFunction.getScopeFunctionDerivable());
							break;
						}
					}
				}
			}
		}
		
		//scopeTypesSelectManyCheckbox = SelectManyCheckbox.build(SelectManyCheckbox.FIELD_CHOICES,scopeTypes,SelectManyCheckbox.FIELD_VALUE,initialSelectedScopeTypes);
		
		scopeTypesDataTable = ScopeTypeListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new ScopeTypeLazyDataModelListenerImpl()
				,DataTable.FIELD_SELECTION_AS_COLLECTION,CollectionHelper.isEmpty(initialSelectedScopeTypes) ? null : new ArrayList<>(initialSelectedScopeTypes)
				,DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(
						ScopeType.FIELD_CODE,ScopeType.FIELD_NAME,ScopeType.FIELD_SCOPE_FUNCTION_DERIVABLE,ScopeType.FIELD_SCOPE_FUNCTION_CODE_SCRIPT
						,ScopeType.FIELD_SCOPE_FUNCTION_NAME_SCRIPT
						));
		
		scopeTypeScopeFunctionDerivableSelectBooleanButton = SelectBooleanButton.build();
		scopeTypeScopeFunctionDerivableSelectBooleanButton.setBindingByDerivation("functionEditScopeTypesPage.scopeTypeScopeFunctionDerivableSelectBooleanButton", "record."+ScopeType.FIELD_SCOPE_FUNCTION_DERIVABLE);
		
		scopeTypeScopeFunctionCodeScriptInputTextarea = InputTextarea.build(InputTextarea.FIELD_STYLE,"color:black;");
		scopeTypeScopeFunctionCodeScriptInputTextarea.setBindingByDerivation("functionEditScopeTypesPage.scopeTypeScopeFunctionCodeScriptInputTextarea", "record."+ScopeType.FIELD_SCOPE_FUNCTION_CODE_SCRIPT);
		
		scopeTypeScopeFunctionNameScriptInputTextarea = InputTextarea.build(InputTextarea.FIELD_STYLE,"color:black;");
		scopeTypeScopeFunctionNameScriptInputTextarea.setBindingByDerivation("functionEditScopeTypesPage.scopeTypeScopeFunctionNameScriptInputTextarea", "record."+ScopeType.FIELD_SCOPE_FUNCTION_NAME_SCRIPT);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypesDataTable,Cell.FIELD_WIDTH,12)
				//,MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypesSelectManyCheckbox,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Arguments<ScopeTypeFunction> arguments = new Arguments<ScopeTypeFunction>();
								arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ScopeTypeFunctionBusiness.SAVE));
								if(CollectionHelper.isNotEmpty(scopeTypes)) {
									//Collection<ScopeType> selectedScopeTypes = CollectionHelper.cast(ScopeType.class, scopeTypesSelectManyCheckbox.getValue());
									Collection<ScopeType> selectedScopeTypes = CollectionHelper.cast(ScopeType.class, scopeTypesDataTable.getSelectionAsCollection());
									if(CollectionHelper.isNotEmpty(selectedScopeTypes)) {
										arguments.setCreatables(selectedScopeTypes.stream().filter(scopeType -> !CollectionHelper.contains(initialSelectedScopeTypes, scopeType))
												.map(scopeType -> new ScopeTypeFunction().setScopeType(scopeType).setFunction(function).setScopeFunctionDerivable(scopeType.getScopeFunctionDerivable()))
											.collect(Collectors.toList()));
										if(CollectionHelper.isNotEmpty(scopeTypesFunctions))
											arguments.setUpdatables(scopeTypesFunctions.stream().filter(scopeTypesFunction -> selectedScopeTypes
												.contains(scopeTypesFunction.getScopeType()) && isHasBeenEdited(scopeTypesFunction)).collect(Collectors.toList()));
										if(CollectionHelper.isNotEmpty(arguments.getUpdatables())) {
											for(ScopeTypeFunction scopeTypeFunction : arguments.getUpdatables()) {
												for(ScopeType selectedScopeType : selectedScopeTypes)
													if(selectedScopeType.equals(scopeTypeFunction.getScopeType())) {
														scopeTypeFunction.setScopeFunctionDerivable(selectedScopeType.getScopeFunctionDerivable());
														scopeTypeFunction.setScopeFunctionCodeScript(selectedScopeType.getScopeFunctionCodeScript());
														scopeTypeFunction.setScopeFunctionNameScript(selectedScopeType.getScopeFunctionNameScript());
														break;
													}
											}
										}
									}								
									/*arguments.addCreatablesOrUpdatables(scopeTypes.stream().filter(scopeType -> CollectionHelper
											.contains(selectedScopeTypes, scopeType) && !CollectionHelper.contains(initialSelectedScopeTypes, scopeType))
											.map(scopeType -> new ScopeTypeFunction().setScopeType(scopeType).setFunction(function))
										.collect(Collectors.toList()));
									*/
									if(CollectionHelper.isNotEmpty(scopeTypesFunctions))
										arguments.addDeletables(scopeTypesFunctions.stream().filter(scopeTypesFunction -> !selectedScopeTypes.contains(scopeTypesFunction.getScopeType())).collect(Collectors.toList()));
								}
								if(CollectionHelper.isNotEmpty(arguments.getCreatables()) || CollectionHelper.isNotEmpty(arguments.getUpdatables()) || CollectionHelper.isNotEmpty(arguments.getDeletables())) {
									EntitySaver.getInstance().save(ScopeTypeFunction.class, arguments);	
								}
								return null;
							}
						},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"),Cell.FIELD_WIDTH,12)
				));
	}
	
	private Boolean isHasBeenEdited(ScopeTypeFunction current) {
		if(current == null)
			return Boolean.FALSE;
		ScopeType initialScopeType = null;
		for(ScopeType index : initialSelectedScopeTypes)
			if(index.equals(current.getScopeType())) {
				initialScopeType = index;
				break;
			}
		if(initialScopeType == null)
			return Boolean.FALSE;
		ScopeType scopeType = null;
		for(ScopeType index : scopeTypes)
			if(index.equals(current.getScopeType())) {
				scopeType = index;
				break;
			}
		if(scopeType == null)
			return Boolean.FALSE;
		if(initialScopeType.getScopeFunctionDerivable() == null)
			return scopeType.getScopeFunctionDerivable() != null;
		if(!initialScopeType.getScopeFunctionDerivable().equals(scopeType.getScopeFunctionDerivable()))
			return Boolean.TRUE;
		if(!StringUtils.equals(initialScopeType.getScopeFunctionCodeScript(), scopeType.getScopeFunctionCodeScript()))
			return Boolean.TRUE;
		if(!StringUtils.equals(initialScopeType.getScopeFunctionNameScript(), scopeType.getScopeFunctionNameScript()))
			return Boolean.TRUE;
		return Boolean.FALSE;
	}
	
	@Getter @Setter @Accessors(chain=true)
	private class ScopeTypeLazyDataModelListenerImpl extends ScopeTypeListPage.LazyDataModelListenerImpl implements Serializable {		
		@Override
		public List<ScopeType> read(LazyDataModel<ScopeType> lazyDataModel) {
			return scopeTypes;
		}
		
		@Override
		public Integer getCount(LazyDataModel<ScopeType> lazyDataModel) {
			return CollectionHelper.getSize(scopeTypes);
		}
	}
}