package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.api.AccountRequestController;
import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestResendAccessTokenPage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private Layout layout;
	private InputText inputText;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				Cell.build(Cell.FIELD_CONTROL,OutputText.buildFromValue("Email"),Cell.FIELD_WIDTH,1)
				,Cell.build(Cell.FIELD_CONTROL,inputText = InputText.build(InputText.FIELD_LISTENER,new AbstractInput.Listener.AbstractImpl() {
					@Override
					public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
						super.validate(context, component, value);
						Actor actor =__inject__(ActorController.class).readByElectronicMailAddress((String) value);
						throwValidatorExceptionIf(actor != null, "cette addresse est déja liée à un compte. Veuillez vous connecter avec votre nom d'utilisateur et votre mot de passe");
						AccountRequest accountRequest =__inject__(AccountRequestController.class).readByElectronicMailAddress((String) value);
						throwValidatorExceptionIf(accountRequest == null, "cette addresse n'est liée à aucune demande de compte");
					}
				}),Cell.FIELD_WIDTH,10)
				,Cell.build(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Renvoyer",CommandButton.FIELD_ICON,"fa fa-send"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_LISTENER
						,new AbstractAction.Listener.AbstractImpl() {
					protected Object __runExecuteFunction__(AbstractAction action) {
						__inject__(AccountRequestController.class).notifyAccessTokenByElectronicMailAddresses(inputText.getValue());
						JsfController.getInstance().redirect("accountRequestOpenView");
						return null;
					}
				} ),Cell.FIELD_WIDTH,1)
				));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ouvrir demande de compte";
	}
}