package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorEditScopesPage extends AbstractActorEditPrivilegesOrScopesPage<ActorScope> implements Serializable {

	private ScopeType scopeType;	
	private DataTable dataTable;
		
	@Override
	protected void addInputs(Collection<Map<?, ?>> cellsMaps) {
		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Assignation des privilèges";
	}
	
	@Override
	protected String getEditOutcome() {
		return "actorEditPrivilegesView";
	}
	
	@Override
	protected String getListOutcome() {
		return "actorListPrivilegesView";
	}
	
	@Override
	protected void save() {
		
	}
}