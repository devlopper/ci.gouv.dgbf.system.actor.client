package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountListPage extends AbstractPageContainerManagedImpl implements Serializable {

	private String type;
	private TabMenu tabMenu;
	private DataTable dataTable;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		type = WebController.getInstance().getRequestParameter(TYPE);
		if(StringHelper.isBlank(type))
			type = TYPE_ACTOR;
		super.__listenPostConstruct__();
		Integer activeIndex;
		if(TYPE_ACTOR.equals(type))
			activeIndex = 0;
		else if(TYPE_ADMINISTRATEUR.equals(type))
			activeIndex = 1;
		else if(TYPE_REQUEST_TO_PROCESS.equals(type))
			activeIndex = 2;		
		else if(TYPE_REQUEST_REJECTED.equals(type))
			activeIndex = 3;		
		else
			activeIndex = 0;
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"accountListView",TabMenu.FIELD_ACTIVE_INDEX,activeIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,CollectionHelper.listOf(
					new MenuItem().setValue("Comptes").addParameter(TYPE, TYPE_ACTOR)
					,new MenuItem().setValue("Administrateurs").addParameter(TYPE, TYPE_ADMINISTRATEUR).addParameter(ParameterName.stringify(Function.class)
						, ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ADMINISTRATEUR)
					,new MenuItem().setValue("Demandes à traiter").addParameter(TYPE, TYPE_REQUEST_TO_PROCESS)				
					,new MenuItem().setValue("Demandes rejetées").addParameter(TYPE, TYPE_REQUEST_REJECTED)
				));
		
		if(TYPE_ACTOR.equals(type))
			dataTable = ActorListPage.buildDataTable();
		else if(TYPE_ADMINISTRATEUR.equals(type))
			dataTable = ActorListPage.buildDataTable(Function.class,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ADMINISTRATEUR);
		else if(TYPE_REQUEST_REJECTED.equals(type))
			dataTable = RejectedAccountRequestListPage.buildDataTable();
		else
			dataTable = AccountRequestListPage.buildDataTable();
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
			MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12)
			,MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)
		));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Comptes";
	}
	
	/**/
	
	public static final String TYPE = "type";
	public static final String TYPE_REQUEST_TO_PROCESS = "a_traiter";
	public static final String TYPE_REQUEST_REJECTED = "rejetees";
	public static final String TYPE_ACTOR = "acteur";
	public static final String TYPE_ADMINISTRATEUR = "administrateur";
}