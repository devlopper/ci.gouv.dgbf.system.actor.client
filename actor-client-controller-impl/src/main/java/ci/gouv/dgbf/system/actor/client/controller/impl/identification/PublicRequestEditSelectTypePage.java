package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestEditSelectTypePage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PublicRequestEditSelectTypePage extends RequestEditSelectTypePage implements IdentificationTheme,Serializable {

}