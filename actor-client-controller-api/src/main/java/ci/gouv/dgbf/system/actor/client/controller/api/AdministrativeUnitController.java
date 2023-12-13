package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;

public interface AdministrativeUnitController extends ControllerEntity<AdministrativeUnit> {

	default Collection<AdministrativeUnit> readVisiblesBySectionIdentifierByActorCodeForUI(String sectionIdentifier,String actorCode) {
		if(StringHelper.isBlank(actorCode))
			return null;
		return readVisiblesByLoggedInActorCodeForUI().stream().filter(administrativeUnit -> administrativeUnit.getSectionIdentifier().equals(sectionIdentifier)).collect(Collectors.toList());
	}
	
	default Collection<AdministrativeUnit> readVisiblesByLoggedInActorCodeForUI() {
		@SuppressWarnings("unchecked")
		Collection<AdministrativeUnit> administrativeUnits = (Collection<AdministrativeUnit>) SessionHelper.getAttributeValue("visibleAdministrativeUnits");
		if(administrativeUnits == null) {
			administrativeUnits = EntityReader.getInstance().readMany(AdministrativeUnit.class
					, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_VISIBLES_BY_ACTOR_CODE_FOR_UI
					,ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, SessionHelper.getUserName());
			if (administrativeUnits == null) 
				administrativeUnits = new ArrayList<>();
			SessionHelper.setAttributeValue("visibleAdministrativeUnits", administrativeUnits);
		}
		return administrativeUnits;
	}
	
	default Collection<AdministrativeUnit> readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(String sectionIdentifier) {
		return readVisiblesBySectionIdentifierByActorCodeForUI(sectionIdentifier,SessionHelper.getUserName());
	}
	
	default Collection<AdministrativeUnit> readBySectionIdentifier(String sectionIdentifier) {
		if(StringHelper.isBlank(sectionIdentifier))
			return null;
		return EntityReader.getInstance().readMany(AdministrativeUnit.class
				,AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI,ScopeQuerier.PARAMETER_NAME_SECTION_IDENTIFIER, sectionIdentifier);
	}
	
	default Collection<AdministrativeUnit> readBySectionIdentifierByServiceGroupCodeStartsWith(String sectionIdentifier,String serviceGroupCode) {
		if(StringHelper.isBlank(sectionIdentifier) || StringHelper.isBlank(serviceGroupCode))
			return null;
		return EntityReader.getInstance().readMany(AdministrativeUnit.class
				,AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_BY_SERVICE_GROUP_CODE_STARTS_WITH_FOR_UI,ScopeQuerier.PARAMETER_NAME_SECTION_IDENTIFIER, sectionIdentifier
				,AdministrativeUnitQuerier.PARAMETER_NAME_SERVICE_GROUP_CODE, serviceGroupCode);
	}
}
