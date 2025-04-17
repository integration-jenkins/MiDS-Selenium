package avendum.com.midsauto;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

public class DPRDataGetters {


    //MAterial DPR Data getting
  //Material Mo data retriveing at Material Delivery pending as Start
    protected String getMaterialSiteMO(int pickerIndex) throws InterruptedException {
      Thread.sleep(500);
      WebDriver driver = Base.getDriver();
      SearchContext picker = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child("+pickerIndex+")")).getShadowRoot();
      Thread.sleep(1000);
      return picker.findElement(By.cssSelector("input[part='value']")).getAttribute("value");
  }

    protected String getMaterialTypeMo(int pickerIndex) throws InterruptedException {
        Thread.sleep(500);
        WebDriver driver = Base.getDriver();
        SearchContext picker = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child("+pickerIndex+")")).getShadowRoot();
        Thread.sleep(1000);
        SearchContext picker1 = picker.findElement(By.cssSelector("#input")).getShadowRoot();
        return picker1.findElement(By.cssSelector("input[role='combobox']")).getAttribute("value");
    }

    protected String getMaterialMoDate(int pickerIndex) throws InterruptedException {
        Thread.sleep(500);
        WebDriver driver = Base.getDriver();
        SearchContext picker = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child("+pickerIndex+")")).getShadowRoot();
        Thread.sleep(1000);
        SearchContext picker1 = picker.findElement(By.cssSelector("#input")).getShadowRoot();
        return picker1.findElement(By.cssSelector("input[part='value']")).getAttribute("value");
    }

    //Material Dispatch State data retriveing at Material Delivery stage




}
