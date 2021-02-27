package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.BudgetSpecializationUnitDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class BudgetSpecializationUnitMapper extends AbstractMapperSourceDestinationImpl<BudgetSpecializationUnit, BudgetSpecializationUnitDto> {
	private static final long serialVersionUID = 1L;
    	
}