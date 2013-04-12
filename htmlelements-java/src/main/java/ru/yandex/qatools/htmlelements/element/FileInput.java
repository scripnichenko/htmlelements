package ru.yandex.qatools.htmlelements.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.File;
import java.net.URL;

/**
 * @author Alexander Tolmachev starlight@yandex-team.ru
 *         Date: 11.04.13
 */
// TODO Add JavaDoc
public class FileInput extends TypifiedElement {

    public FileInput(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void setFileToUpload(final String fileName) {
        // Proxy can't be used to check the element class, so find real WebElement
        WebElement fileInputElement = getNotProxiedInputElement();
        // Set local file detector in case of remote driver usage
        if (isRemoteWebElement(fileInputElement)) {
            setLocalFileDetector((RemoteWebElement) fileInputElement);
        }

        String filePath = getFilePath(fileName);
        fileInputElement.sendKeys(filePath);
    }

    public void submitFile() {
        getWrappedElement().submit();
    }

    private WebElement getNotProxiedInputElement() {
        return getWrappedElement().findElement(By.xpath("."));
    }

    private void setLocalFileDetector(RemoteWebElement element) {
        element.setFileDetector(new LocalFileDetector());
    }

    private String getFilePath(final String fileName) {
        if (existsInClasspath(fileName)) {
            return getPathForResource(fileName);
        }
        return getPathForSystemFile(fileName);
    }

    private String getPathForResource(final String fileName) {
        return getResourceFromClasspath(fileName).getPath();
    }

    private String getPathForSystemFile(final String fileName) {
        File file = new File(fileName);
        return file.getPath();
    }

    // TODO To utils
    private static boolean isRemoteWebElement(WebElement element) {
        return element.getClass().equals(RemoteWebElement.class);
    }

    // TODO To utils
    private static boolean existsInClasspath(final String fileName) {
        return getResourceFromClasspath(fileName) != null;
    }

    // TODO To utils
    private static URL getResourceFromClasspath(final String fileName) {
        return Thread.currentThread().getContextClassLoader().getResource(fileName);
    }
}