package Day21;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import Day20.ScreenShotMethodsInSelenium ;


public class AmazonRepotCreated {
	public static WebDriver driver;
	public static int  Sellector=1;
	public static ExtentSparkReporter oSpark;
	public static ExtentReports Oreport;
	public static ExtentTest  Otest;
	public static String path="./Report/Amazon.html";
	public static void main(String[] args) throws IOException {
		reportSetUp();
		BrowserSellector();
		BrowserSetting();
		GetInformation();
		SearchBox("ponniyin selvan", "Books");
		PageFoundResult();
		Exist();
	}
	public static void reportSetUp() {
		oSpark=new ExtentSparkReporter(path);
		Oreport=new ExtentReports();
		Oreport.attachReporter(oSpark);
	}
	public static void BrowserSellector() {
		Otest= Oreport.createTest("BrowserSellection", "User invoke the broswer based on input.");//return type is ExtentTest. 
		Otest.assignAuthor("Logitha");
		Otest.assignCategory("Smoke Testing");
		switch (Sellector) {
		case 1:
			System.out.println("User option is : "+Sellector+", So invoking chrome browser.");
			driver=new ChromeDriver();
			Otest.info("\"User option is : "+Sellector+", So invoking chrome browser.");
			break;
		case 2:
			System.out.println("User option is : "+Sellector+", So invoking edge browser.");
			driver=new EdgeDriver();
			Otest.info("User option is : "+Sellector+", So invoking edge browser.");
			break;
		default:
			System.out.println("User Select Unknow Value: "+Sellector+" So Open default Chrome Browser.");
			driver=new ChromeDriver();
			Otest.info("User Select Unknow Value: "+Sellector+" So Open default Chrome Browser.");
			break;
		}
		
	}
   public static void BrowserSetting() {
	   Otest= Oreport.createTest("Browser Setting", "User can change the browser setting ");
	   Otest.assignAuthor("Yuvaraj");
	   Otest.assignCategory("Sanity");
			driver.manage().window().maximize();
			Otest.info("User Maximize the browser size");
			driver.navigate().to("https://www.amazon.in/");
			driver.manage().deleteAllCookies();
			Otest.info("User deleted all browser cookies");
			driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(40));
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		}
   public static void GetInformation() throws IOException {
	   Otest= Oreport.createTest("Browser Information", "User Collected broswer information.");
	   Otest.assignAuthor("Ragu");
	   Otest.assignCategory("Smoke");
			String Url=driver.getCurrentUrl();
			System.out.println("The current URL is: "+Url);
			Otest.info("use navigated the url: "+Url);
			@Nullable
			String title = driver.getTitle();
			System.out.println("The Current page title: "+title);
			Otest.info("Page title: "+title);
			String handle = driver.getWindowHandle();
			System.out.println("The window handle: "+handle);
			Otest.info("Window handle of current open browser: "+handle);
			if (title.equalsIgnoreCase("Amazon")) {
				Otest.pass("User landed correct page");
			} else {
				String imagePath=ScreenShotMethodsInSelenium.takeScreenShotAsFileWithDynamicFileName(driver, "Amaxon");
				Otest.fail("User landed to the wrong page", MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());


			}
		}
   public static void SearchBox(String ProductName,String ProCategories) {
	   Otest= Oreport.createTest("Search Product", "User can search for any product by providing the catagory.");
	   Otest.assignAuthor("Narmatha");
	   Otest.assignCategory("Sanity");
	   
	   WebElement SearchText, Categories,Botton;
	   SearchText=driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
	   SearchText.sendKeys(ProductName);
	   Otest.info("User search product name: "+ProductName);
	   
	   Categories=driver.findElement(By.xpath("//select[@id='searchDropdownBox']"));
	   Select Objsel=new Select(Categories);
	   Objsel.selectByVisibleText(ProCategories);
	   Otest.info("User selected categories: "+ProCategories);
	   
	   Botton=driver.findElement(By.xpath("//input[@id='nav-search-submit-button']"));
	   Botton.click();
	   Otest.info("User clicked on search button");
   }
   public static void PageFoundResult() throws IOException {
	   Otest= Oreport.createTest("Validate Search Result", "User can Validate the Search Results.");
	   Otest.assignAuthor("Chithra");
	   Otest.assignCategory("Sanity");
	   WebElement Oresult;
	   Oresult=driver.findElement(By.xpath("(//div[@class='sg-col-inner'][1]//h2)[1]"));
	   //to get the result value as using text
	   String ResultText=Oresult.getText();
	   System.out.println("After Regex: "+ResultText);
	   //next we check the value is greater than zero.you can't directly use because of those are string
	   String Result = ResultText.replaceAll("[^0-9]", "");
	   //still it has string to converted integer
	   int int1 = Integer.parseInt(Result);
	  // System.out.println("Extracted Number: " + Result);
	   if (int1>0) {
		System.out.println("search result is avaliable");
		List<WebElement> elements = driver.findElements(By.xpath("//span[@class='rush-component s-latency-cf-section']//div[@role='listitem']"));
		   for (WebElement Element : elements) {
			   String text = Element.findElement(By.xpath(".//div[@data-cy='title-recipe']//h2")).getText();
			System.out.println(text);
			Otest.pass("Product name is : "+text);
		   }  
	   }
		   else {
		System.out.println("search result is not avaliable");
		String fileName = ScreenShotMethodsInSelenium.takeScreenShotAsFileWithDynamicFileName(driver, "SearchResult");
		Otest.fail("search result is not avaliable.",MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
	}
	   
   }
  
   public static void Exist() {
	   Oreport.flush();
	   driver.quit();
   }

}
