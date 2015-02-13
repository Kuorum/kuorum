/*
        This is the Geb configuration file.

        See: http://www.gebish.org/manual/current/configuration.html
*//*



import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxProfile

waiting {
    timeout = 3
}

def os = 'linux64'
def driverFileName = 'chromedriver'
if (System.getProperty('os.name').toLowerCase().contains('windows')) {
    os = 'win'
    driverFileName += '.exe'
}

def chromeDriver = new File("test/drivers/chrome/${os}/${driverFileName}")
downloadDriver(chromeDriver, "https://chromedriver.googlecode.com/files/chromedriver_${os}_2.3.zip")
System.setProperty('webdriver.chrome.driver', chromeDriver.absolutePath)

def chromeBinary = System.getProperty('chrome.binary')
if (chromeBinary) {
    ChromeOptions options = new ChromeOptions()
    options.setBinary() // Modify with -Dchrome.binary=/path/to/chrome
    options.addArguments("--start-maximized")
    driver = { new ChromeDriver(options) }
} else {
    driver = { new ChromeDriver() }
}

environments {

    // See: http://code.google.com/p/selenium/wiki/FirefoxDriver
    firefox {
        def profile = new FirefoxProfile()
        profile.setPreference('intl.accept_languages', 'es-es,es')
        driver = { new FirefoxDriver(profile) }
    }

}

private void downloadDriver(File file, String path) {
    if (!file.exists()) {
        println "Downloading Chrome Driver"
        def ant = new AntBuilder()
        ant.get(src: path, dest: 'driver.zip')
        ant.unzip(src: 'driver.zip', dest: file.parent)
        ant.delete(file: 'driver.zip')
        ant.chmod(file: file, perm: '700')
    }
}*/
