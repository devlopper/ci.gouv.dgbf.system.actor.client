package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneRadio;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.server.business.api.AccountRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AccountRequestQuerier;
import ci.gouv.dgbf.system.actor.server.representation.api.AccountRequestRepresentation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	private AccountRequest accountRequest;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		accountRequest = EntityReader.getInstance().readOne(AccountRequest.class, AccountRequestQuerier.QUERY_IDENTIFIER_READ_PROJECTION_01_BY_IDENTIFIER
				, AccountRequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));
		super.__listenPostConstruct__();		
		layout = buildLayout(AccountRequest.class,accountRequest); //buildLayout(AccountRequest.class,accountRequest);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Consultation de demande de compte : "+accountRequest.getElectronicMailAddress()+" | "+accountRequest.getNames();
	}
	
	/**/
	
	private static Object instantiateName(AccountRequest accountRequest,String name) {
		return OutputText.buildFromValue(name);
	}
	
	private static Object instantiateValue(AccountRequest accountRequest,String fieldName) {
		return OutputText.buildFromValue(StringHelper.get(FieldHelper.read(accountRequest, fieldName)));
	}
	
	private static void instantiateNameValue(Collection<Map<Object,Object>> cellsMaps,AccountRequest accountRequest,String fieldName,String name,Integer nameWidth,Integer valueWidth) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,instantiateName(accountRequest, name),Cell.FIELD_WIDTH,nameWidth));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,instantiateValue(accountRequest, fieldName),Cell.FIELD_WIDTH,valueWidth));
	}
	
	/**/
	
	public static Layout buildLayout(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		AccountRequest accountRequest = (AccountRequest) MapHelper.readByKey(map, AccountRequest.class);		
		MapHelper.writeByKeyDoNotOverride(map, Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G);
		CommandButton commandButton = null;
		@SuppressWarnings("unchecked")
		Collection<Map<Object,Object>> cellsMaps = (Collection<Map<Object, Object>>) MapHelper.readByKey(map, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS);
		if(cellsMaps == null) {
			cellsMaps = new ArrayList<>();
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_CIVILITY_AS_STRING, "Civilité", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_FIRST_NAME, "Nom", 2, 3);
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_LAST_NAMES, "Prénom(s)", 1, 6);
			
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_GROUP_AS_STRING, "Groupe", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_REGISTRATION_NUMBER, "Matricule", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS, "Mail", 2, 3);//Adresse de courrier électronique
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_POSTAL_BOX_ADDRESS, "Boite postale", 1, 6);
			
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_MOBILE_PHONE_NUMBER, "Tel. Mobile", 2, 3);
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_OFFICE_PHONE_NUMBER, "Tel. Bureau", 1, 3);
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_OFFICE_PHONE_EXTENSION, "Poste", 1, 2);
			
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_ADMINISTRATIVE_UNIT_AS_STRING, "Unité administrative", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, AccountRequest.FIELD_ADMINISTRATIVE_FUNCTION, "Fonction administrative", 2, 10);
			
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Traitement"),Cell.FIELD_WIDTH,2));			
			SelectOneRadio acceptOrRejectSelectOneRadio;
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,acceptOrRejectSelectOneRadio = 
					SelectOneRadio.build(SelectOneRadio.FIELD_CHOICES,List.of(ACCEPT,REJECT),SelectOneRadio.FIELD_REQUIRED,Boolean.TRUE),Cell.FIELD_WIDTH,10));
			
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Motif"),Cell.FIELD_WIDTH,2));
			InputText rejectReasonInput;
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,rejectReasonInput = InputText.build(InputText.FIELD_OBJECT,accountRequest,InputText.FIELD_FIELD,
					FieldHelper.getByName(AccountRequest.class, AccountRequest.FIELD_REJECT_REASON)
					,InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
				@Override
				public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
					super.validate(context, component, value);
					//throwValidatorExceptionIf(REJECT.equals(acceptOrRejectSelectOneRadio.getValue()) && StringHelper.isBlank(accountRequest.getRejectReason())
					//		, "Le motif de rejet est obligatoire");
				}
			},InputText.FIELD_PLACEHOLDER,"Motif de rejet"),Cell.FIELD_WIDTH,10));
			
			cellsMaps.stream().filter(x -> x.get(Cell.FIELD_CONTROL) instanceof AbstractInput).map(x -> x.get(Cell.FIELD_CONTROL)).collect(Collectors.toList());
			Collection<?> inputs = cellsMaps.stream().filter(x -> x.get(Cell.FIELD_CONTROL) instanceof AbstractInput)
					.map(x -> x.get(Cell.FIELD_CONTROL)).collect(Collectors.toList());
						
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,commandButton = CommandButton.build(MapHelper.instantiate(
					CommandButton.FIELD_VALUE,"Enregistrer"
					,CommandButton.FIELD_ICON,"fa fa-floppy-o"
					,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
					,CommandButton.ConfiguratorImpl.FIELD_INPUTS,inputs
					,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION					
					,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							if(REJECT.equals(acceptOrRejectSelectOneRadio.getValue()) && StringHelper.isBlank(rejectReasonInput.getValue()))
								throw new RuntimeException("Le motif de rejet est obligatoire");
							accountRequest.setRejectReason(rejectReasonInput.getValue());
							EntitySaver.getInstance().save(AccountRequest.class, new Arguments<AccountRequest>()
									.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setActionIdentifier(ACCEPT.equals(acceptOrRejectSelectOneRadio.getValue()) ? AccountRequestBusiness.ACCEPT : AccountRequestBusiness.REJECT))
									.setRepresentation(AccountRequestRepresentation.getProxy())
									.addCreatablesOrUpdatables(accountRequest));
							return null;
						}
					})
					),Cell.FIELD_WIDTH,12));	
			
			MapHelper.writeByKeyDoNotOverride(map, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
		}
		Layout layout = Layout.build(map);
		if(commandButton != null)
			commandButton.addUpdatables(layout);
		return layout;
	}
	
	public static Layout buildLayout(Object...arguments) {
		return buildLayout(MapHelper.instantiate(arguments));
	}
	
	/**/
	
	public static final String ACCEPT = "Accepter";
	public static final String REJECT = "Rejeter";
}