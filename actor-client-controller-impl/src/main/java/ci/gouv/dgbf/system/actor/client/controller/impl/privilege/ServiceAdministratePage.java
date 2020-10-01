package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;

import ci.gouv.dgbf.system.actor.client.controller.entities.Menu;
import ci.gouv.dgbf.system.actor.client.controller.entities.Service;
import ci.gouv.dgbf.system.actor.client.controller.impl.privilege.MenuListPage.LazyDataModelListenerImpl;
import ci.gouv.dgbf.system.actor.server.business.api.ServiceBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ServiceQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ServiceAdministratePage extends AbstractPageContainerManagedImpl implements Serializable {

	private SelectOneCombo serviceSelectOneCombo;
	private CommandButton deriveAuthorizationsCommandButton,deleteAuthorizationsCommandButton;
	private DataTable menuDataTable;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		Collection<Service> services = EntityReader.getInstance().readMany(Service.class, ServiceQuerier.QUERY_IDENTIFIER_READ);
		Service service = CollectionHelper.getFirst(services);
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		if(service != null) {
			menuDataTable = MenuListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl()
					.setServiceIdentifier(service == null ? null : service.getIdentifier())
					,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,CollectionHelper.listOf(Menu.FIELD_CODE,Menu.FIELD_NAME
							,Menu.FIELD_UNIFORM_RESOURCE_IDENTIFIER,Menu.FIELD_PROFILES_AS_STRING,Menu.FIELD_DEFINED));
			
			menuDataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Dériver les autorisations",MenuItem.FIELD_ICON,"fa fa-plus"
					,MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL)
					,MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							Arguments<Service> arguments = new Arguments<Service>().addCreatablesOrUpdatables((Service)serviceSelectOneCombo.getValue());
							arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATIONS));					
							EntitySaver.getInstance().save(Service.class, arguments);
							return null;
						}
					});
			
			menuDataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Supprimer les autorisations",MenuItem.FIELD_ICON,"fa fa-minus"
					,MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL)
					,MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							Arguments<Service> arguments = new Arguments<Service>().addCreatablesOrUpdatables((Service)serviceSelectOneCombo.getValue());
							arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ServiceBusiness.DELETE_KEYCLOAK_AUTHORIZATIONS));					
							EntitySaver.getInstance().save(Service.class, arguments);
							return null;
						}
					});
			
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildServiceSelectOneCombo(services,service),Cell.FIELD_WIDTH,12));
			
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,menuDataTable,Cell.FIELD_WIDTH,12));	
		}		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@SuppressWarnings("unchecked")
	public SelectOneCombo buildServiceSelectOneCombo(Collection<Service> services,Service selected) {
		if(CollectionHelper.isEmpty(services))
			return null;
		serviceSelectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICES,services);
		serviceSelectOneCombo.select(selected);
		serviceSelectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@Override
			protected void select(AbstractAction action, Object value) {
				((MenuListPage.LazyDataModelListenerImpl)((LazyDataModel<Menu>)menuDataTable.getValue()).getListener())
					.setServiceIdentifier((String)FieldHelper.readSystemIdentifier(value));
			}
		}, List.of(menuDataTable));
		return serviceSelectOneCombo;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Administration de service";
	}
}