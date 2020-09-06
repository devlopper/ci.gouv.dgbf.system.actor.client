package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	private AccountRequest accountRequest;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		accountRequest = WebController.getInstance().getRequestParameterEntity(AccountRequest.class);
		super.__listenPostConstruct__();		
		layout = buildLayout(AccountRequest.class,accountRequest);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Consultation de demande de compte : "+accountRequest.getElectronicMailAddress()+" | "+accountRequest.getNames();
	}
	
	/**/
	
	public static Layout buildLayout(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		AccountRequest accountRequest = (AccountRequest) MapHelper.readByKey(map, AccountRequest.class);
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>(); 
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Civilité"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("?"),Cell.FIELD_WIDTH,9));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Nom"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("?"),Cell.FIELD_WIDTH,9));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Prénom(s)"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("?"),Cell.FIELD_WIDTH,9));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Motif de rejet"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,InputText.build(),Cell.FIELD_WIDTH,9));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Accepter",CommandButton.FIELD_ICON,"fa fa-check")
				,Cell.FIELD_WIDTH,12));
		
		MapHelper.writeByKeyDoNotOverride(map,Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G);
		MapHelper.writeByKeyDoNotOverride(map,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
		return Layout.build(map);
	}
	
	public static Layout buildLayout(Object...arguments) {
		return buildLayout(MapHelper.instantiate(arguments));
	}
}