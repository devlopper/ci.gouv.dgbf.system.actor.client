package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;

import ci.gouv.dgbf.system.actor.server.representation.api.ActorRepresentation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountEditPassPage extends AbstractLoggedInUserPage implements MyAccountTheme, Serializable {

	private CommandButton sendCommandButton;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		sendCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Envoyer",CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.FIELD_ICON,"fa fa-send",CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_NULLABLE,Boolean.TRUE
				,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl(){
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				ActorRepresentation.getProxy().sendUpdatePasswordEmail(actor.getCode());
				JsfController.getInstance().redirect("myAccountNotifyAfterEditPassView");
				return null;
			}
		});
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Modification de mon mot de passe";
	}
}