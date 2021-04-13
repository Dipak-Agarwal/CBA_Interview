package myDemoProject;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class NewTest {

	By queryInput = By.xpath("//input[@type='text'][@id='search-input']");
	By searchButton = By.xpath("//button[@id='search-button']");
	By emptyQueryErrorMsg = By.xpath("//div[@id='error-empty-query']");
	By searchResultsList = By.xpath("//ul[@id='search-results']/li");
	By noResultsMsg = By.xpath("//div[@id='error-no-results']");	
	
	private WebDriver driver;
	
	@BeforeTest
	public void setup()
	{
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
		driver.get("https://codility-frontend-prod.s3.amazonaws.com/media/task_static/qa_csharp_search/862b0faa506b8487c25a3384cfde8af4/static/attachments/reference_page.html");
	}
	
	@Test(priority=0)
	public void test_QueryInputAndSearchButtonOnMainScreen() throws InterruptedException {
		System.out.println("");
		driver.findElement(queryInput).isDisplayed();
		driver.findElement(searchButton).isDisplayed();
	}
	
	@Test(priority=1)
	public void test_SearchWithEmptyQuery() throws InterruptedException {
		System.out.println("");
		driver.findElement(searchButton).click();
		String errorMsg = driver.findElement(emptyQueryErrorMsg).getText();
		Assert.assertEquals(errorMsg, "Provide some query", "validation of Error message when searching for empty query");
	}
	
	@Test(priority=2)
	public void test_SearchForIsland() throws InterruptedException {
		System.out.println("");
		driver.findElement(queryInput).sendKeys("isla");
		driver.findElement(searchButton).click();
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).pollingEvery(Duration.ofSeconds(1)).pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsList));
		List<WebElement> searchResults = driver.findElements(searchResultsList);
		System.out.println("Search Results : "+searchResults);
		Assert.assertTrue(searchResults.size()>0, "Validating Search Results is greater than 0 when searching for isla");
	}
	
	@Test(priority=3)
	public void test_SearchForCastle() throws InterruptedException {
		System.out.println("");
		driver.findElement(queryInput).clear();
		driver.findElement(queryInput).sendKeys("castle");
		driver.findElement(searchButton).click();
		String msg = driver.findElement(noResultsMsg).getText();
		Assert.assertEquals(msg, "No results", "validation of message when searching for castle which does not give any results");
	}
	
	@Test(priority=4)
	public void test_SearchForPort() throws InterruptedException {
		System.out.println("");
		driver.findElement(queryInput).clear();
		driver.findElement(queryInput).sendKeys("port");
		driver.findElement(searchButton).click();
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).pollingEvery(Duration.ofSeconds(1)).pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsList));
		List<WebElement> searchResults = driver.findElements(searchResultsList);
		Assert.assertTrue(searchResults.size()==1, "Validating Search Results is only 1 when searching for port");
		Assert.assertEquals(searchResults.get(0).getText(), "Port Royal", "Validating Search Results returns only Port Royal when searching for port");
	}
	
	@AfterClass
	public void tearDown() {
		if(driver!=null) {
			driver.quit();
		}
	}
	
}
