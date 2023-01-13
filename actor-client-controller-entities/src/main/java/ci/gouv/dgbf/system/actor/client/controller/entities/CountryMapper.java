package ci.gouv.dgbf.system.actor.client.controller.entities;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

import ci.gouv.dgbf.system.actor.server.representation.entities.CountryDto;

@Mapper
public abstract class CountryMapper extends AbstractMapperSourceDestinationImpl<Country, CountryDto> {
	private static final long serialVersionUID = 1L;
    	
}