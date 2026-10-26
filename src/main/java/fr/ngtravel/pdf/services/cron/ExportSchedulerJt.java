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
@Profile("jt")
public class ExportSchedulerJt extends ExportScheduler {
	private static final String FORMAT = "jt";
	
	public ExportSchedulerJt(
			@Qualifier("productExportService") ExportService productExportService, 
			@Qualifier("destinationExportService") ExportService destinationExportService,
			ExportConfigRepository exportConfigRepository) {
		    super(productExportService, destinationExportService, exportConfigRepository);		
	}
	
	@SchedulerLock(name = "resoneo.export.jettours", lockAtMostFor = "PT15M")
	@Scheduled(cron = "0 0 3 * * ?")
	@Override
	protected void exportScheduledTask() {
		execute(FORMAT);
	}
}
