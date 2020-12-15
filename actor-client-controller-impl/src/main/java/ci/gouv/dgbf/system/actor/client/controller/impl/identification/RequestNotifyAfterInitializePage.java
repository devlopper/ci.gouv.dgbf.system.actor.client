package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestNotifyAfterInitializePage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private String message;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		message = String.format(StringUtils.join(List.of(
				"Votre demande a été initiée."
				,"Une notification, contenant un jeton d'accès, vous a été envoyée à l'adresse mail %s"
				,"Veuillez accéder à votre demande afin de l'imprimer, la mettre à jour si besoin et la soumettre."
				,"Nous vous remercions pour l'intérèt porté au SIGOBE."), "<br/>")
				,WebController.getInstance().getRequestParameter(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS)
			);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Saisie de demande";
	}
}