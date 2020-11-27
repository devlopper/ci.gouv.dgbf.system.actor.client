package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.Helper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorAccountProfileReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Actor actor;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		String identifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER);
		if(StringHelper.isNotBlank(identifier))
			actor = getActor(identifier);
		if(actor != null)
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
		@SuppressWarnings("unchecked")
		Collection<Map<Object,Object>> cellsMaps = (Collection<Map<Object, Object>>) MapHelper.readByKey(arguments, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS);
		CommandButton editProfileCommandButton = null;
		if(cellsMaps == null) {
			Actor actor = (Actor) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
			if(actor != null) {
				cellsMaps = new ArrayList<>();
				buildLayoutAddRow(cellsMaps, "Nom d'utilisateur", actor.getCode());
				buildLayoutAddRow(cellsMaps, "Civilité", actor.getCivilityAsString());
				buildLayoutAddRow(cellsMaps, "Nom et prénom(s)", actor.getNames());
				buildLayoutAddRow(cellsMaps, "Matricule", actor.getRegistrationNumber());
				
				buildLayoutAddRow(cellsMaps, "Numéro de téléphone mobile", actor.getMobilePhoneNumber());
				buildLayoutAddRow(cellsMaps, "Numéro de téléphone de bureau", actor.getOfficePhoneNumber());
				buildLayoutAddRow(cellsMaps, "Poste de téléphone de bureau", actor.getOfficePhoneExtension());
				buildLayoutAddRow(cellsMaps, "Email", actor.getElectronicMailAddress());
				buildLayoutAddRow(cellsMaps, "Adresse postale", actor.getPostalBoxAddress());
				
				buildLayoutAddRow(cellsMaps, "Section", actor.getSectionAsString());
				buildLayoutAddRow(cellsMaps, "Unité administrative", actor.getAdministrativeUnitAsString());
				buildLayoutAddRow(cellsMaps, "Fonction administrative", actor.getAdministrativeFunction());
				
				buildLayoutAddRow(cellsMaps, "Référence de l'acte de nomination", actor.getActOfAppointmentReference());
				buildLayoutAddRow(cellsMaps, "Date de signature de l'acte de nomination", actor.getActOfAppointmentSignatureDateAsString());
				buildLayoutAddRow(cellsMaps, "Signataire de l'acte de nomination", actor.getActOfAppointmentSignatory());
				
				buildLayoutAddRow(cellsMaps, "Fonction(s) budgétaire(s)", StringHelper.get(actor.getBudgetaryFunctionsAsStrings()));
				
				String editOutcome = ValueHelper.defaultToIfBlank((String) MapHelper.readByKey(arguments, FIELD_EDIT_OUTCOME),"actorAccountProfileEditView");
				editProfileCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Modifier",CommandButton.FIELD_ICON,"fa fa-edit"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.NAVIGATE_TO_VIEW
						,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
					@Override
					protected String getOutcome(AbstractAction action) {
						return editOutcome;
					}
					
					@Override
					protected Map<String, List<String>> getViewParameters(AbstractAction action) {
						Map<String, List<String>> map = new HashMap<>();
						map.put(ParameterName.ENTITY_IDENTIFIER.getValue(),CollectionHelper.listOf(actor.getIdentifier()));
						map.put(ParameterName.URL.getValue(),CollectionHelper.listOf(Helper.getRequestedUrl()));
						return map;
					}
				});
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,editProfileCommandButton,Cell.FIELD_WIDTH,12));
			}
			MapHelper.writeByKey(arguments, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
		}
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G);
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_NUMBER_OF_COLUMNS,2);
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_ROW_CELL_MODEL,Map.of(0,new Cell().setWidth(3),1,new Cell().setWidth(9)));
		Layout layout = Layout.build(arguments);
		if(editProfileCommandButton != null)
			editProfileCommandButton.addUpdates(":form:"+layout.getIdentifier());
		return layout;
	}
	
	private static void buildLayoutAddRow(Collection<Map<Object,Object>> cellsMaps,String label,String value) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(label)));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(value)));
	}
	
	public static Layout buildLayout(Object...objects) {
		return buildLayout(MapHelper.instantiate(objects));
	}
	
	public static Actor getActor(String identifier) {
		return EntityReader.getInstance().readOne(Actor.class, ActorQuerier.QUERY_IDENTIFIER_READ_PROFILE_INFORMATIONS_BY_IDENTIFIER_FOR_UI
				, ActorQuerier.PARAMETER_NAME_IDENTIFIER,identifier);
	}
	
	/**/
	
	public static final String FIELD_EDIT_OUTCOME = "editOutcome";
}