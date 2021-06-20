package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped
@Getter @Setter @Accessors(chain=true)
public class AssignmentsReadPage extends AbstractAssignmentsReadPage implements Serializable {

}
