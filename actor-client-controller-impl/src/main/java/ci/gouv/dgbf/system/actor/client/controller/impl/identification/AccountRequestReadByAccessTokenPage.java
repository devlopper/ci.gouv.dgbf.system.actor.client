package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.api.AccountRequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestReadByAccessTokenPage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private AccountRequest accountRequest;
	private Layout layout;
	private String staticMessageSeverity,staticMessageSummary,staticMessageDetail;
	private CommandButton editCommandButton,deleteCommandButton,sendCommandButton,printCommandButton;
	
	@Override
	protected void __listenPostConstruct__() {
		String accessToken = WebController.getInstance().getRequestParameter("accesstoken");
		if(StringHelper.isNotBlank(accessToken))
			accountRequest = __inject__(AccountRequestController.class).readProjection01WithBudgetaryFunctionsAndFunctionsByAccessToken(accessToken);
		if(accountRequest == null)
			JsfController.getInstance().redirect("accountRequestOpenView");
		super.__listenPostConstruct__();
		if(accountRequest != null) {
			if(StringHelper.isBlank(accountRequest.getSubmissionDateAsString())) {
				editCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Modifier",CommandButton.FIELD_ICON,"fa fa-edit",CommandButton.FIELD_USER_INTERFACE_ACTION
						,UserInterfaceAction.NAVIGATE_TO_VIEW,CommandButton.FIELD___OUTCOME__,"accountRequestEditView"
						,CommandButton.FIELD___PARAMETERS__,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(accountRequest.getIdentifier())
								,ParameterName.ACTION_IDENTIFIER.getValue(),List.of(Action.UPDATE.name())));
				deleteCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Supprimer",CommandButton.FIELD_ICON,"fa fa-trash");
				sendCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Transmettre",CommandButton.FIELD_ICON,"fa fa-send");	
				printCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Imprimer",CommandButton.FIELD_ICON,"fa fa-print");	
			}else {
				
			}	
		}		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(accountRequest == null)
			return super.__getWindowTitleValue__();
		return "Demande de compte de "+accountRequest.getElectronicMailAddress()+" | "+accountRequest.getNames();
	}
}