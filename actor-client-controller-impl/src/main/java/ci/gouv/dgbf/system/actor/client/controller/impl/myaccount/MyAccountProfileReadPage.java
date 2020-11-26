package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountProfileReadPage extends AbstractLoggedInUserPage implements MyAccountTheme, Serializable {

	private Layout layout;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Profile";
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = buildLayout();
	}
	
	/**/
	
	public static Layout buildLayout(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		@SuppressWarnings("unchecked")
		Collection<Map<Object,Object>> cellsMaps = (Collection<Map<Object, Object>>) MapHelper.readByKey(arguments, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS);
		if(cellsMaps == null) {
			Actor actor = (Actor) MapHelper.readByKey(arguments, Actor.class);
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
			}
			MapHelper.writeByKey(arguments, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
		}
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G);
		return Layout.build(arguments);
	}
	
	private static void buildLayoutAddRow(Collection<Map<Object,Object>> cellsMaps,String label,String value) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(label)));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(value)));
	}
	
	public static Layout buildLayout(Object...objects) {
		return buildLayout(MapHelper.instantiate(objects));
	}
}