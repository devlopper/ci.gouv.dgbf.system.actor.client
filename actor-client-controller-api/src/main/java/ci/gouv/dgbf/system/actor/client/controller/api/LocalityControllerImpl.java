package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Locality;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.LocalityQuerier;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

@ApplicationScoped
public class LocalityControllerImpl extends AbstractControllerEntityImpl<Locality> implements LocalityController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<Locality> readRegions() {
		return EntityReader.getInstance().readMany(Locality.class, new Arguments<Locality>().queryIdentifier(LocalityQuerier.QUERY_IDENTIFIER_READ_DYNAMIC)
				.filterFieldsValues(LocalityQuerier.PARAMETER_NAME_TYPE,ci.gouv.dgbf.system.actor.server.persistence.entities.Locality.Type.REGION.name()));
	}

	@Override
	public Collection<Locality> readByParents(Collection<Locality> parents) {
		if(CollectionHelper.isEmpty(parents))
			return null;
		return readByParentsIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(parents));
	}

	@Override
	public Collection<Locality> readByParentsIdentifiers(Collection<String> parentsIdentifiers) {
		if(CollectionHelper.isEmpty(parentsIdentifiers))
			return null;
		return null;
	}
	
	@Override
	public Collection<Locality> readByParent(Locality parent) {
		if(parent == null)
			return null;
		return readByParentIdentifier(parent.getIdentifier());
	}

	@Override
	public Collection<Locality> readByParentIdentifier(String parentIdentifier) {
		if(StringHelper.isBlank(parentIdentifier))
			return null;
		return EntityReader.getInstance().readMany(Locality.class, new Arguments<Locality>().queryIdentifier(LocalityQuerier.QUERY_IDENTIFIER_READ_DYNAMIC)
				.filterFieldsValues(LocalityQuerier.PARAMETER_NAME_PARENT_IDENTIFIER,parentIdentifier));
	}
}