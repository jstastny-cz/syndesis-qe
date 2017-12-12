package io.syndesis.qe;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
	features = "classpath:features",
	format = {"pretty", "html:target/cucumber-report", "junit:target/cucumber-junit.html", "json:target/cucumber-report.json"})
public class CucumberTestsRunner {

	//we could have some setup here

}
