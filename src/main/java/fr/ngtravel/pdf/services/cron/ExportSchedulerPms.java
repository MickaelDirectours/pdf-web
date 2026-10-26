package fr.ngtravel.pdf.services.cron;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fr.ngtravel.resoneo.repository.jpa.ExportConfigRepository;
import fr.ngtravel.resoneo.services.ExportService;
import fr.ngtravel.resoneo.util.cron.ExportScheduler;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Component
@Profile("pms")
public class ExportSchedulerPms extends ExportScheduler {
	private static final String FORMAT = "pms";
	
	public ExportSchedulerPms(
			@Qualifier("productExportService") ExportService productExportService, 
			@Qualifier("destinationExportService") ExportService destinationExportService,
			ExportConfigRepository exportConfigRepository) {
		    super(productExportService, destinationExportService, exportConfigRepository);		
	}
	
	@SchedulerLock(name = "resoneo.export.promosejours", lockAtMostFor = "PT15M")
	@Scheduled(cron = "0 30 3 * * ?")
	@Override
	protected void exportScheduledTask() {
		execute(FORMAT);
	}
}
