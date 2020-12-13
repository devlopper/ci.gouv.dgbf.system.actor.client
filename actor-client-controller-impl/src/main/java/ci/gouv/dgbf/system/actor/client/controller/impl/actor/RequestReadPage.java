package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestReadPage extends AbstractPageContainerManagedImpl implements Serializable {
	
	private Request request;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		String identifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue());
		request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI, RequestQuerier.PARAMETER_NAME_IDENTIFIER, identifier);
		super.__listenPostConstruct__();
		buildLayout();
	}
	
	private void buildLayout() {
		if(request == null || request.getType() == null)
			return;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		addToolBar(cellsMaps, "requestEditView");
		Map<String,IdentificationAttribute> map = IdentificationForm.computeFieldsNames(request.getType().getForm(), Request.class);
		add(cellsMaps, "Type", request.getType().getName());
		map.forEach( (fieldName,attribute) -> {
			Object value = FieldHelper.read(request, fieldName);
			if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS.equals(fieldName)) {
				if(CollectionHelper.isNotEmpty(request.getBudgetariesScopeFunctionsAsStrings()))
					value = StringUtils.join(request.getBudgetariesScopeFunctionsAsStrings(),"<br/>");
			}else if(Request.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE.equals(fieldName)) {
				value = request.getActOfAppointmentSignatureDateAsString();
			}
			add(cellsMaps, attribute.getName(), value == null ? null : value.toString());
		});
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	private void addToolBar(Collection<Map<Object,Object>> cellsMaps,String editOutcome) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
				,Button.build(Button.FIELD_VALUE,"Modifier",Button.FIELD_ICON,"fa fa-edit",Button.FIELD_OUTCOME,editOutcome
				,Button.FIELD_PARAMETERS,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),request.getIdentifier()
						,ParameterName.ACTION_IDENTIFIER.getValue(),Action.UPDATE.name())
				),Cell.FIELD_WIDTH,1));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
				,CommandButton.build(CommandButton.FIELD_VALUE,"Imprimer",CommandButton.FIELD_ICON,"fa fa-print"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.OPEN_VIEW_IN_DIALOG
						//,CommandButton.FIELD___OUTCOME__,"requestPrintView"
						//,CommandButton.FIELD___PARAMETERS__,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),request.getIdentifier())
						,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							protected String getOutcome(AbstractAction action) {
								return "requestPrintView";
							}
							
							protected Map<String,List<String>> getViewParameters(AbstractAction action) {
								return Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier()));
							}
						}
				),Cell.FIELD_WIDTH,1));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
				,CommandButton.build(CommandButton.FIELD_VALUE,"Soumettre",CommandButton.FIELD_ICON,"fa fa-send"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
					protected Object __runExecuteFunction__(AbstractAction action) {
						EntitySaver.getInstance().save(Request.class, new Arguments<Request>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setActionIdentifier(RequestBusiness.SUBMIT)).addCreatablesOrUpdatables(request));		
						Redirector.getInstance().redirect("requestReadView",Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
						return null;
					}
				}
				),Cell.FIELD_WIDTH,10));
	}
	
	private void add(Collection<Map<Object,Object>> cellsMaps,String label,String value) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(label),Cell.FIELD_WIDTH,3));
		OutputText valueOutputText = OutputText.buildFromValue(StringHelper.isBlank(value) ? "---" : value);
		valueOutputText.setEscape(Boolean.FALSE);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,valueOutputText,Cell.FIELD_WIDTH,9));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(request == null)
			return super.__getWindowTitleValue__();
		return request.getTypeAsString();
	}
}