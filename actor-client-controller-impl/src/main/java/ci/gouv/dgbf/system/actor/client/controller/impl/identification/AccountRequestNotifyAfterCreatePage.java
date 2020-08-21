package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestNotifyAfterCreatePage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private String message;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		message = "Votre demande a été enregistrée et sera traitée dans les plus bref délais.<br/>" + 
				"Nous vous remercions pour l'intérèt porté au SIGOBE.<br/>" + 
				"Après traitement de votre demande par la DGBF , une notification vous seras adressée par mail.";
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Demande de compte";
	}
	
}
