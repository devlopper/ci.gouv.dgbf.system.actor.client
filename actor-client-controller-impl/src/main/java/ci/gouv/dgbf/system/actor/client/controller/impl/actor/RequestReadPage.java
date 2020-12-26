package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

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
import org.cyk.utility.__kernel__.uri.UniformResourceIdentifierBuilder;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.FileServlet;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.Event;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.GraphicImage;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.javascript.OpenWindowScriptBuilder;
import org.cyk.utility.report.jasper.client.ReportServlet;

import ci.gouv.dgbf.system.actor.client.controller.api.RequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.FileServletListenerImpl;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import ci.gouv.dgbf.system.actor.server.representation.api.RequestRepresentation;
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
	
	public static Layout buildLeftLayout(Request request) {
		if(request == null || request.getType() == null)
			return null;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		Map<String,IdentificationAttribute> map = IdentificationForm.computeFieldsNames(request.getType().getForm(), Request.class);
		addLabelValue(cellsMaps, "Type", request.getType().getName());
		addLabelValue(cellsMaps, "Statut", request.getStatus().getName());
		
		request.setActOfAppointment(__inject__(RequestController.class).getActOfAppointmentByIdentifier(request.getIdentifier()));
		request.setPhoto(__inject__(RequestController.class).getPhotoByIdentifier(request.getIdentifier()));
		
		map.forEach( (fieldName,attribute) -> {
			Object value = FieldHelper.read(request, fieldName);
			if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS.equals(fieldName)) {
				if(CollectionHelper.isNotEmpty(request.getBudgetariesScopeFunctionsAsStrings()))
					value = StringUtils.join(request.getBudgetariesScopeFunctionsAsStrings(),"<br/>");
			}else if(Request.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE.equals(fieldName)) {
				value = request.getActOfAppointmentSignatureDateAsString();
			}
			addLabelValue(cellsMaps, attribute.getName(), value == null ? null : value.toString());
		});
	
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	public static Layout buildRightLayout(Request request,String editOutcome,String readOutcome,String printOutcome) {
		if(request == null || request.getType() == null)
			return null;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();		
		request.setActOfAppointment(__inject__(RequestController.class).getActOfAppointmentByIdentifier(request.getIdentifier()));
		request.setPhoto(__inject__(RequestController.class).getPhotoByIdentifier(request.getIdentifier()));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,GraphicImage.build(
				GraphicImage.ConfiguratorImpl.FIELD_LABEL_OUTPUT_TEXT_VALUE,"Photo"
				,GraphicImage.ConfiguratorImpl.FIELD_READ_URI
				,request.getPhoto() == null ? null : UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(FileServlet.buildRelativeURI(request, FileServletListenerImpl.REQUEST_FILE_TYPE_PHOTO),null)
				,GraphicImage.ConfiguratorImpl.FIELD_UPDATE_OUTCOME,RequestUpdatePhotoPage.OUTCOME
				,GraphicImage.ConfiguratorImpl.FIELD_ENTITY_IDENTIFIER,request.getIdentifier(),GraphicImage.ConfiguratorImpl.FIELD_DELETABLE,request.getPhoto() != null
				,GraphicImage.FIELD_LISTENER,new GraphicImage.Listener.AbstractImpl() {
					@Override
					public void listenDelete(GraphicImage graphicImage) {
						super.listenDelete(graphicImage);
						RequestRepresentation.getProxy().recordPhotoByIdentifier(request.getIdentifier(), null);		
						Redirector.getInstance().redirect(readOutcome,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
					}
				}),Cell.FIELD_WIDTH,12));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,GraphicImage.build(
				GraphicImage.ConfiguratorImpl.FIELD_LABEL_OUTPUT_TEXT_VALUE,"Acte de nomination"
				,GraphicImage.ConfiguratorImpl.FIELD_READ_URI
				,request.getActOfAppointment() == null ? null : UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(FileServlet.buildRelativeURI(request, FileServletListenerImpl.REQUEST_FILE_TYPE_ACT_OF_APPOINTMENT),null)
				,GraphicImage.FIELD_LIBRARY,"image",GraphicImage.FIELD_NAME,"icon/text.png"
				,GraphicImage.ConfiguratorImpl.FIELD_UPDATE_OUTCOME,RequestUpdateActOfAppointmentPage.OUTCOME
				,GraphicImage.ConfiguratorImpl.FIELD_ENTITY_IDENTIFIER,request.getIdentifier(),GraphicImage.ConfiguratorImpl.FIELD_DELETABLE,request.getActOfAppointment() != null
				,GraphicImage.FIELD_LISTENER,new GraphicImage.Listener.AbstractImpl() {
					@Override
					public void listenDelete(GraphicImage graphicImage) {
						super.listenDelete(graphicImage);
						RequestRepresentation.getProxy().recordActOfAppointmentByIdentifier(request.getIdentifier(), null);		
						Redirector.getInstance().redirect(readOutcome,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
					}
				}),Cell.FIELD_WIDTH,12));
		
		/*
		addLabelControl(cellsMaps, "Photo",  GraphicImage.build(
				GraphicImage.ConfiguratorImpl.FIELD_READ_URI
				,request.getPhoto() == null ? null : UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(FileServlet.buildRelativeURI(request, FileServletListenerImpl.REQUEST_FILE_TYPE_PHOTO),null)
				,GraphicImage.ConfiguratorImpl.FIELD_UPDATE_OUTCOME,RequestUpdatePhotoPage.OUTCOME
				,GraphicImage.ConfiguratorImpl.FIELD_ENTITY_IDENTIFIER,request.getIdentifier(),GraphicImage.ConfiguratorImpl.FIELD_DELETABLE,request.getPhoto() != null
				,GraphicImage.FIELD_LISTENER,new GraphicImage.Listener.AbstractImpl() {
					@Override
					public void listenDelete(GraphicImage graphicImage) {
						super.listenDelete(graphicImage);
						RequestRepresentation.getProxy().recordPhotoByIdentifier(request.getIdentifier(), null);		
						Redirector.getInstance().redirect(readOutcome,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
					}
				}));
		
		addLabelControl(cellsMaps, "A.N.",  GraphicImage.build(
				GraphicImage.ConfiguratorImpl.FIELD_READ_URI
				,request.getActOfAppointment() == null ? null : UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(FileServlet.buildRelativeURI(request, FileServletListenerImpl.REQUEST_FILE_TYPE_ACT_OF_APPOINTMENT),null)
				,GraphicImage.FIELD_LIBRARY,"image",GraphicImage.FIELD_NAME,"icon/text.png"
				,GraphicImage.ConfiguratorImpl.FIELD_UPDATE_OUTCOME,RequestUpdateActOfAppointmentPage.OUTCOME
				,GraphicImage.ConfiguratorImpl.FIELD_ENTITY_IDENTIFIER,request.getIdentifier(),GraphicImage.ConfiguratorImpl.FIELD_DELETABLE,request.getActOfAppointment() != null
				,GraphicImage.FIELD_LISTENER,new GraphicImage.Listener.AbstractImpl() {
					@Override
					public void listenDelete(GraphicImage graphicImage) {
						super.listenDelete(graphicImage);
						RequestRepresentation.getProxy().recordActOfAppointmentByIdentifier(request.getIdentifier(), null);		
						Redirector.getInstance().redirect(readOutcome,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
					}
				}));
		*/
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	public static Layout buildLayout(Request request,String editOutcome,String readOutcome,String printOutcome) {
		if(request == null || request.getType() == null)
			return null;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		addToolBar(request,cellsMaps, editOutcome,readOutcome,printOutcome);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildLeftLayout(request),Cell.FIELD_WIDTH,8));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildRightLayout(request, editOutcome, readOutcome, printOutcome),Cell.FIELD_WIDTH,4));
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	/*
	public static Layout buildLayout(Request request,String editOutcome,String readOutcome,String printOutcome) {
		if(request == null || request.getType() == null)
			return null;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		addToolBar(request,cellsMaps, editOutcome,readOutcome,printOutcome);
		Map<String,IdentificationAttribute> map = IdentificationForm.computeFieldsNames(request.getType().getForm(), Request.class);
		addLabelValue(cellsMaps, "Type", request.getType().getName());
		addLabelValue(cellsMaps, "Statut", request.getStatus().getName());
		
		request.setActOfAppointment(__inject__(RequestController.class).getActOfAppointmentByIdentifier(request.getIdentifier()));
		request.setPhoto(__inject__(RequestController.class).getPhotoByIdentifier(request.getIdentifier()));
		
		addLabelControl(cellsMaps, "Photo",  GraphicImage.build(
				GraphicImage.ConfiguratorImpl.FIELD_READ_URI
				,request.getPhoto() == null ? null : UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(FileServlet.buildRelativeURI(request, FileServletListenerImpl.REQUEST_FILE_TYPE_PHOTO),null)
				,GraphicImage.ConfiguratorImpl.FIELD_UPDATE_OUTCOME,RequestUpdatePhotoPage.OUTCOME
				,GraphicImage.ConfiguratorImpl.FIELD_ENTITY_IDENTIFIER,request.getIdentifier(),GraphicImage.ConfiguratorImpl.FIELD_DELETABLE,request.getPhoto() != null
				,GraphicImage.FIELD_LISTENER,new GraphicImage.Listener.AbstractImpl() {
					@Override
					public void listenDelete(GraphicImage graphicImage) {
						super.listenDelete(graphicImage);
						RequestRepresentation.getProxy().recordPhotoByIdentifier(request.getIdentifier(), null);		
						Redirector.getInstance().redirect(readOutcome,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
					}
				}));
		
		addLabelControl(cellsMaps, "A.N.",  GraphicImage.build(
				GraphicImage.ConfiguratorImpl.FIELD_READ_URI
				,request.getActOfAppointment() == null ? null : UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(FileServlet.buildRelativeURI(request, FileServletListenerImpl.REQUEST_FILE_TYPE_ACT_OF_APPOINTMENT),null)
				,GraphicImage.FIELD_LIBRARY,"image",GraphicImage.FIELD_NAME,"icon/text.png"
				,GraphicImage.ConfiguratorImpl.FIELD_UPDATE_OUTCOME,RequestUpdateActOfAppointmentPage.OUTCOME
				,GraphicImage.ConfiguratorImpl.FIELD_ENTITY_IDENTIFIER,request.getIdentifier(),GraphicImage.ConfiguratorImpl.FIELD_DELETABLE,request.getActOfAppointment() != null
				,GraphicImage.FIELD_LISTENER,new GraphicImage.Listener.AbstractImpl() {
					@Override
					public void listenDelete(GraphicImage graphicImage) {
						super.listenDelete(graphicImage);
						RequestRepresentation.getProxy().recordActOfAppointmentByIdentifier(request.getIdentifier(), null);		
						Redirector.getInstance().redirect(readOutcome,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(request.getIdentifier())));
					}
				}));
		
		map.forEach( (fieldName,attribute) -> {
			Object value = FieldHelper.read(request, fieldName);
			if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS.equals(fieldName)) {
				if(CollectionHelper.isNotEmpty(request.getBudgetariesScopeFunctionsAsStrings()))
					value = StringUtils.join(request.getBudgetariesScopeFunctionsAsStrings(),"<br/>");
			}else if(Request.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE.equals(fieldName)) {
				value = request.getActOfAppointmentSignatureDateAsString();
			}
			addLabelValue(cellsMaps, attribute.getName(), value == null ? null : value.toString());
		});
	
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	*/
	public static void addToolBar(Request request,Collection<Map<Object,Object>> cellsMaps,String editOutcome,String readOutcome,String printOutcome) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
				,Button.build(Button.FIELD_VALUE,"Modifier",Button.FIELD_ICON,"fa fa-edit",Button.FIELD_OUTCOME,editOutcome
				,Button.FIELD_PARAMETERS,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),request.getIdentifier()
						,ParameterName.ACTION_IDENTIFIER.getValue(),Action.UPDATE.name())
				,Button.FIELD_DISABLED,!ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.CODE_INITIALIZED.equals(request.getStatus().getCode())
				),Cell.FIELD_WIDTH,1));
				
		Button button = Button.build(Button.FIELD_VALUE,"Imprimer",Button.FIELD_ICON,"fa fa-print");
		/*
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
		*/
		String url = UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(ReportServlet.PATH, request.getReadReportURIQuery()).toString();
		button.setEventScript(Event.CLICK,OpenWindowScriptBuilder.getInstance().build(url, "Fichier etat de "+request.getTypeAsString()));
		/*
		button.setEventScript(Event.CLICK, String.format(scriptFormat, url,request.getIdentifier()
				,"location=no,menubar=no,resizable=no,status=no,titlebar=no,toolbar=no","Fichier etat de "+request.getTypeAsString()));
		*/
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
	
	public static void addLabelValue(Collection<Map<Object,Object>> cellsMaps,String label,String value) {
		OutputText valueOutputText = OutputText.buildFromValue(StringHelper.isBlank(value) ? "---" : value);
		valueOutputText.setEscape(Boolean.FALSE);
		addLabelControl(cellsMaps, label, valueOutputText);
	}
	
	public static void addLabelControl(Collection<Map<Object,Object>> cellsMaps,String label,Object control) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(label),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,control,Cell.FIELD_WIDTH,9));
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