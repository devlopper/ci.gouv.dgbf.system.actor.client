package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
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
import org.cyk.utility.__kernel__.identifier.resource.PathAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.QueryAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierAsFunctionParameter;
import org.cyk.utility.__kernel__.identifier.resource.UniformResourceIdentifierHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.Event;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.report.jasper.client.ReportServlet;

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
		request = loadRequest();
		super.__listenPostConstruct__();
		buildLayout(request,"requestEditView","requestReadView","requestPrintView");
	}
	
	public static Request loadRequest() {
		String identifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue());
		return EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI, RequestQuerier.PARAMETER_NAME_IDENTIFIER, identifier);
	}
	
	public static Layout buildLayout(Request request,String editOutcome,String readOutcome,String printOutcome) {
		if(request == null || request.getType() == null)
			return null;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		addToolBar(request,cellsMaps, editOutcome,readOutcome,printOutcome);
		Map<String,IdentificationAttribute> map = IdentificationForm.computeFieldsNames(request.getType().getForm(), Request.class);
		add(cellsMaps, "Type", request.getType().getName());
		add(cellsMaps, "Statut", request.getStatus().getName());
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
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	public static void addToolBar(Request request,Collection<Map<Object,Object>> cellsMaps,String editOutcome,String readOutcome,String printOutcome) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
				,Button.build(Button.FIELD_VALUE,"Modifier",Button.FIELD_ICON,"fa fa-edit",Button.FIELD_OUTCOME,editOutcome
				,Button.FIELD_PARAMETERS,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),request.getIdentifier()
						,ParameterName.ACTION_IDENTIFIER.getValue(),Action.UPDATE.name())
				,Button.FIELD_DISABLED,!ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.CODE_INITIALIZED.equals(request.getStatus().getCode())
				),Cell.FIELD_WIDTH,1));
				
		Button button = Button.build(Button.FIELD_VALUE,"Imprimer",Button.FIELD_ICON,"fa fa-print");
		String scriptFormat = "var w = window.open('%s','%s','%s');w.document.title = '%s';";
		
		UniformResourceIdentifierAsFunctionParameter p = new UniformResourceIdentifierAsFunctionParameter();
		p.setRequest(FacesContext.getCurrentInstance().getExternalContext().getRequest());
		p.setPath(new PathAsFunctionParameter());
		p.getPath().setValue("/acteur"+ReportServlet.PATH);
		if(StringHelper.isNotBlank(request.getReadReportURIQuery())) {
			p.setQuery(new QueryAsFunctionParameter());
			p.getQuery().setValue(request.getReadReportURIQuery());
		}		
		String url = UniformResourceIdentifierHelper.build(p);
		
		button.setEventScript(Event.CLICK, String.format(scriptFormat, url,request.getIdentifier()
				,"location=no,menubar=no,resizable=no,status=no,titlebar=no,toolbar=no","Fichier etat de "+request.getTypeAsString()));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,button,Cell.FIELD_WIDTH,1));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
			,CommandButton.build(CommandButton.FIELD_VALUE,"Soumettre",CommandButton.FIELD_ICON,"fa fa-send"
					,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,CommandButton.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE
					,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
				protected Object __runExecuteFunction__(AbstractAction action) {
					EntitySaver.getInstance().save(Request.class, new Arguments<Request>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
							.setActionIdentifier(RequestBusiness.SUBMIT)).addCreatablesOrUpdatables(request));		
					Redirector.getInstance().redirect(readOutcome,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
					return null;
				}
			}
			,CommandButton.FIELD_DISABLED,!ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.CODE_INITIALIZED.equals(request.getStatus().getCode())
			),Cell.FIELD_WIDTH,10));
		
	}
	
	public static void add(Collection<Map<Object,Object>> cellsMaps,String label,String value) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(label),Cell.FIELD_WIDTH,3));
		OutputText valueOutputText = OutputText.buildFromValue(StringHelper.isBlank(value) ? "---" : value);
		valueOutputText.setEscape(Boolean.FALSE);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,valueOutputText,Cell.FIELD_WIDTH,9));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return getWindowTitleValue(request, super.__getWindowTitleValue__());
	}
	
	public static String getWindowTitleValue(Request request,String default_) {
		if(request == null)
			return default_;
		return request.getTypeAsString();
	}
}