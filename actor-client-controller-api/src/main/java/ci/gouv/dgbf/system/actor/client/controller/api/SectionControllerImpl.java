package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.EntityReader;

@ApplicationScoped
public class SectionControllerImpl extends AbstractControllerEntityImpl<Section> implements SectionController,Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Collection<Section> read() {
		return EntityReader.getInstance().readMany(Section.class, SectionQuerier.QUERY_IDENTIFIER_READ_DYNAMIC);
	}
	
}