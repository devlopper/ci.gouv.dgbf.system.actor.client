package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestBoardPage extends AbstractPageContainerManagedImpl implements Serializable {

	private BarChartModel barModel;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		
		barModel = initBarModel();
		 
        //barModel.setTitle("Demandes");
        barModel.setLegendPosition("ne");
 
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Demandes");
 
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Nombre de demandes");
        yAxis.setMin(0);
        yAxis.setMax(200);
	}
	
	private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Demandes");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Acceptées");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
 
        return model;
    }
	
}