package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorReadController;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountReadPage extends AbstractPageContainerManagedImpl implements MyAccountTheme,Serializable {

	private Actor actor;
	private Layout layout;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		Arguments<Actor> arguments = new Arguments<Actor>();
		arguments.queryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE).filterFieldsValues(ActorQuerier.PARAMETER_NAME_CODE,SessionHelper.getUserName())
		.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.FIELDS_REGISTRATION_NUMBER_FIRST_NAME_LAST_NAMES_ELECTRONIC_MAIL_ADDRESS_ADMINISTRATIVE_FUNCTION_CIVILITY_IDENTITY_GROUP_ADMINISTRATIVE_UNIT_SECTION);
		actor = EntityReader.getInstance().readOne(Actor.class, arguments);
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = buildLayout(Form.FIELD_ENTITY,actor);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(actor == null)
			return super.__getWindowTitleValue__();
		return "Profile de "+actor.getCode()+" - "+actor.getNames();
	}
	
	/**/
	
	public static Layout buildLayout(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Actor actor = (Actor) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		ActorReadController actorReadController = new ActorReadController(actor);
		@SuppressWarnings("unchecked")
		Collection<Map<Object,Object>> cellsMaps = (Collection<Map<Object, Object>>) MapHelper.readByKey(arguments, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS);
		if(cellsMaps == null)
			arguments.put(Layout.ConfiguratorImpl.FIELD_CELLS_MAPS, cellsMaps = new ArrayList<>());
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorReadController.getLayout(),Cell.FIELD_WIDTH,12));
		
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G);
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_NUMBER_OF_COLUMNS,2);
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(3),1,new Cell().setWidth(9)));
		Layout layout = Layout.build(arguments);
		return layout;
	}
	
	public static Layout buildLayout(Object...objects) {
		return buildLayout(MapHelper.instantiate(objects));
	}
}