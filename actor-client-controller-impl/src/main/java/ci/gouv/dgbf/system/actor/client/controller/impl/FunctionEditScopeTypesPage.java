package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectManyCheckbox;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeTypeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeTypeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class FunctionEditScopeTypesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Function function;
	private SelectManyCheckbox scopeTypesSelectManyCheckbox;
	private Collection<ScopeTypeFunction> scopeTypesFunctions;
	private Collection<ScopeType> scopeTypes,initialSelectedScopeTypes;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Champ(s) d'action de la fonction "+function.getName();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		function = WebController.getInstance().getRequestParameterEntity(Function.class);
		super.__listenPostConstruct__();		
		scopeTypes = EntityReader.getInstance().readMany(ScopeType.class, ScopeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING);
		if(CollectionHelper.isNotEmpty(scopeTypes)) {
			scopeTypesFunctions = EntityReader.getInstance().readMany(ScopeTypeFunction.class, ScopeTypeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_FUNCTIONS_IDENTIFIERS
					,ScopeTypeFunctionQuerier.PARAMETER_NAME_FUNCTIONS_IDENTIFIERS,List.of(function.getIdentifier()));
			if(CollectionHelper.isNotEmpty(scopeTypesFunctions))
				initialSelectedScopeTypes = scopeTypesFunctions.stream().map(x -> x.getScopeType()).collect(Collectors.toList());
		}
		
		scopeTypesSelectManyCheckbox = SelectManyCheckbox.build(SelectManyCheckbox.FIELD_CHOICES,scopeTypes,SelectManyCheckbox.FIELD_VALUE,initialSelectedScopeTypes);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypesSelectManyCheckbox,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Arguments<ScopeTypeFunction> arguments = new Arguments<ScopeTypeFunction>();
								arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ScopeTypeFunctionBusiness.SAVE));
								if(CollectionHelper.isNotEmpty(scopeTypes)) {
									Collection<ScopeType> selectedScopeTypes = CollectionHelper.cast(ScopeType.class, scopeTypesSelectManyCheckbox.getValue());
									arguments.addCreatablesOrUpdatables(scopeTypes.stream().filter(scopeType -> CollectionHelper
											.contains(selectedScopeTypes, scopeType) && !CollectionHelper.contains(initialSelectedScopeTypes, scopeType))
											.map(scopeType -> new ScopeTypeFunction().setScopeType(scopeType).setFunction(function))
										.collect(Collectors.toList()));
									if(CollectionHelper.isNotEmpty(scopeTypesFunctions))
										arguments.addDeletables(scopeTypesFunctions.stream().filter(scopeTypesFunction -> !selectedScopeTypes.contains(scopeTypesFunction.getScopeType())).collect(Collectors.toList()));
								}
								if(CollectionHelper.isNotEmpty(arguments.getCreatables()) || CollectionHelper.isNotEmpty(arguments.getDeletables())) {
									EntitySaver.getInstance().save(ScopeTypeFunction.class, arguments);	
								}
								return null;
							}
						},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"),Cell.FIELD_WIDTH,12)
				));
	}
}