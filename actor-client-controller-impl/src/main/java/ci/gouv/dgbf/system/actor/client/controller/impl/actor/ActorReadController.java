package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActorReadController extends AbstractObject implements Serializable {

	private IdentityReadController identityReadController;
	private Layout layout,requestsLayout;
	
	public ActorReadController(Actor actor) {
		identityReadController = new IdentityReadController(actor);
		buildRequestsLayout(actor);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,identityReadController.getLayout(),Cell.FIELD_WIDTH,12)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,requestsLayout,Cell.FIELD_WIDTH,12)
					));
		
	}

	private void buildRequestsLayout(Actor actor) {
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildActorScopeRequestDataTable();
			}
		},Cell.FIELD_WIDTH,12));
		requestsLayout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	public DataTable buildActorScopeRequestDataTable() {
		ActorScopeRequestFilterController filterController = new ActorScopeRequestFilterController();
		filterController.setRenderType(AbstractFilterController.RenderType.NONE);
		DataTable dataTable = ActorScopeRequestListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new ActorScopeRequestListPage.LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
}