package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;

@Named @RequestScoped @Getter @Setter
public class PublicRequestScopeFunctionListInputElectronicMailAddressNotifyPage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private String message;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		message = StringHelper.concatenate(List.of(
				"Un lien permettant d'accéder aux spécimens de signature a été envoyé a l'adresse mail spécifiée."
				,"Veuillez vous connecter à la boite de messagerie et cliquer sur ce lien."
				), "<br/>");
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Saisie de mail pour récupération de spécimen de signature";
	}
	
	public static final String OUTCOME = "publicRequestScopeFunctionListInputElectronicMailAddressNotifyView";
}