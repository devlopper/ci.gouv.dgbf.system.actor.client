package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestUpdateSignedRequestSheetPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PublicRequestUpdateSignedRequestSheetPage extends RequestUpdateSignedRequestSheetPage implements Serializable {

	public static final String OUTCOME = "publicRequestUpdateSignedRequestSheetView";
}