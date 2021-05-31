package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractSelectionController;

import ci.gouv.dgbf.system.actor.client.controller.entities.Activity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActivitySelectionController extends AbstractSelectionController<Activity> implements Serializable {

}