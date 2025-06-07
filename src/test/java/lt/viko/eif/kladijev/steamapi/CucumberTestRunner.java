package lt.viko.eif.kladijev.steamapi;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/lt/viko/eif/kladijev/steamapi/features",
        glue = "lt.viko.eif.kladijev.steamapi.stepdefs",      // пакет с шагами
        plugin = {"pretty", "html:target/cucumber-report.html"},
        publish = false
)
public class CucumberTestRunner
{

}
