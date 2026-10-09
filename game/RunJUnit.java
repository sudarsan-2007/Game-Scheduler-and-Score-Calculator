import org.junit.platform.launcher.Launcher; 
import org.junit.platform.launcher.LauncherDiscoveryRequest; 
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder; 
import org.junit.platform.launcher.core.LauncherFactory; 
import org.junit.platform.launcher.listeners.SummaryGeneratingListener; 
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage; 

public class RunJUnit {
  public static void main(String[] args) {
    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
      .selectors(selectPackage("com.gamescheduler"))
      .build();
    Launcher launcher = LauncherFactory.create();
    SummaryGeneratingListener listener = new SummaryGeneratingListener();
    launcher.registerTestExecutionListeners(listener);
    launcher.execute(request);
    var summary = listener.getSummary();
    System.out.println("testsFound=" + summary.getTestsFoundCount());
    System.out.println("testsSucceeded=" + summary.getTestsSucceededCount());
    System.out.println("testsFailed=" + summary.getTestsFailedCount());
    summary.getFailures().forEach(f -> {
      System.out.println("FAILURE: " + f.getTestIdentifier().getDisplayName());
      f.getException().printStackTrace(System.out);
    });
    if (summary.getTestsFailedCount() > 0) System.exit(1);
  }
}
