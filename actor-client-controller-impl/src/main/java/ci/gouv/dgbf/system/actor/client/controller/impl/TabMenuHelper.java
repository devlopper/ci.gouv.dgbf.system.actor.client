package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;

public interface TabMenuHelper {

	static <T> String addIdentifiablesTabMenu(Class<T> identifiableClass,Collection<Map<Object,Object>> cellsMaps,Collection<T> identifiables,String outcome
			,Collection<TabMenu.Tab> mastersTabs,String selectedMasterTab,String selectedIdentifier/*,AddIdentifiablesTabMenuListener listener*/) {		
		if(identifiableClass == null || CollectionHelper.isEmpty(identifiables))
			return null;
		if(StringHelper.isBlank(selectedIdentifier))
			selectedIdentifier = StringHelper.get(FieldHelper.readSystemIdentifier(CollectionHelper.getFirst(identifiables)));		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		Integer tabActiveIndex = null,index = 0;
		for(T identifiable : identifiables) {
			String identifier = StringHelper.get(FieldHelper.readSystemIdentifier(identifiable));
			String name = StringHelper.get(FieldHelper.readName(identifiable));
			if(StringHelper.isBlank(name))
				name = StringHelper.get(FieldHelper.readBusinessIdentifier(identifiable));
			tabMenuItems.add(new MenuItem().setValue(name)
				.addParameter(TabMenu.Tab.PARAMETER_NAME, TabMenu.Tab.getByParameterValue(mastersTabs, selectedMasterTab).getParameterValue())
				.addParameter(ParameterName.stringify(identifiableClass), identifier)
			);
			if(identifier.equals(selectedIdentifier))
				tabActiveIndex = index;
			else
				index++;
		}
		
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,outcome,TabMenu.FIELD_ACTIVE_INDEX,tabActiveIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		return selectedIdentifier;
	}
	
	static String addCreditManagersAuthorizingOfficersFinancialControllersAssistantsTabMenu(Collection<Map<Object,Object>> cellsMaps,String outcome
			,Collection<TabMenu.Tab> mastersTabs,String selectedMasterTab,String selectedFunctionIdentifier) {		
		return addIdentifiablesTabMenu(Function.class, cellsMaps, DependencyInjection.inject(FunctionController.class).readCreditManagersAuthorizingOfficersFinancialControllersAssistants()
				, outcome, mastersTabs, selectedMasterTab, selectedFunctionIdentifier);
	}
	
	/**/
	
	interface AddIdentifiablesTabMenuListener {
		
	}
}